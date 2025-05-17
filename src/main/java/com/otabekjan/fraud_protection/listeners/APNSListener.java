package com.otabekjan.fraud_protection.listeners;

import com.eatthepath.pushy.apns.ApnsClient;
import com.eatthepath.pushy.apns.ApnsClientBuilder;
import com.eatthepath.pushy.apns.PushNotificationResponse;
import com.eatthepath.pushy.apns.util.ApnsPayloadBuilder;
import com.eatthepath.pushy.apns.util.SimpleApnsPayloadBuilder;
import com.eatthepath.pushy.apns.util.SimpleApnsPushNotification;
import com.eatthepath.pushy.apns.util.concurrent.PushNotificationFuture;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.otabekjan.fraud_protection.dto.APNSPojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyStore;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutionException;


@Component
public class APNSListener {


    private static final Gson GSON = new GsonBuilder().create();
    private static final Logger log = LoggerFactory.getLogger(APNSListener.class);

    private static final String p12FilePath = "fraud_shield_push_sandbox.p12";

    @Value("${apns.bundleId}")
    private String bundleId;

    @Value("${apns.p12Password}")
    private String apnsP12Password;

    @Value("${apns.url}")
    private String url;


    @RabbitListener(queues = "${spring.rabbitmq.template.apns-queue}")
    public void listen(APNSPojo pojo) {
        sendNative(pojo);
//        pushy lib had problems while pushing
//        sendViaLib(pojo);
    }

    private void sendNative(APNSPojo pojo) {
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            try (InputStream keyInput = this.getClass()
                    .getClassLoader()
                    .getResourceAsStream(p12FilePath)) {
                keyStore.load(keyInput, apnsP12Password.toCharArray());
            }


            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(keyStore, apnsP12Password.toCharArray());
            KeyManager[] keyManagers = kmf.getKeyManagers();


            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagers, null, null);

            HttpClient client = HttpClient.newBuilder()
                    .sslContext(sslContext)
                    .version(HttpClient.Version.HTTP_2)
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "/3/device/" + pojo.getDeviceToken()))
                    .header("apns-topic", bundleId)
                    .header("apns-expiration", String.valueOf(OffsetDateTime.now().plusDays(1).toEpochSecond()))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(toJson(pojo).toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


            log.info("Status: {}", response.statusCode());
            log.info("Body: {}", response.body());

        } catch (Exception ex) {
            log.warn("APNS error", ex);
        }

    }


    private JsonObject toJson(APNSPojo pojo) {
        JsonObject aps = new JsonObject();
        aps.addProperty("alert", pojo.getTitle());

        JsonObject payload = new JsonObject();
        payload.add("aps", aps);

        if (pojo.getType() != null) {
            payload.addProperty("type", pojo.getType().getId());
        }

        if (pojo.getPayload() != null) {
            payload.addProperty("payload", pojo.getPayload().toString());
        }

        return payload;
    }

    private void sendViaLib(APNSPojo pojo) {
        try (InputStream inputStream = this.getClass().getResourceAsStream(p12FilePath)) {
            final ApnsClient apnsClient = new ApnsClientBuilder()
                    .setApnsServer(ApnsClientBuilder.DEVELOPMENT_APNS_HOST)
                    .setClientCredentials(inputStream, apnsP12Password)
                    .build();

            final SimpleApnsPushNotification pushNotification;

            {
                final ApnsPayloadBuilder payloadBuilder = new SimpleApnsPayloadBuilder();
                payloadBuilder.setAlertTitle(pojo.getTitle());
                payloadBuilder.setAlertBody(pojo.getBody());

                payloadBuilder.setAttributes(Map.of("type", pojo.getType(),
                        "payload", GSON.toJson(pojo.getPayload())));
                payloadBuilder.setBadgeNumber(pojo.getBadge());

                pushNotification = new SimpleApnsPushNotification(pojo.getDeviceToken(), bundleId, payloadBuilder.build());
            }

            final PushNotificationFuture<SimpleApnsPushNotification, PushNotificationResponse<SimpleApnsPushNotification>>
                    sendNotificationFuture = apnsClient.sendNotification(pushNotification);


            try {
                final PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse =
                        sendNotificationFuture.get();

                if (pushNotificationResponse.isAccepted()) {
                    System.out.println("Push notification accepted by APNs gateway.");
                } else {
                    System.out.println("Notification rejected by the APNs gateway: " +
                            pushNotificationResponse.getRejectionReason());

                    pushNotificationResponse.getTokenInvalidationTimestamp().ifPresent(timestamp -> {
                        System.out.println("\t…and the token is invalid as of " + timestamp);
                    });
                }
            } catch (final ExecutionException e) {
                log.warn("Failed to send push notification.", e);

            }
        } catch (Exception ex) {
            log.warn("APNS error", ex);
        }
    }
}
