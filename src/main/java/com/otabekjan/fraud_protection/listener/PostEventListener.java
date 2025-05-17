package com.otabekjan.fraud_protection.listener;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.otabekjan.fraud_protection.$;
import com.otabekjan.fraud_protection.dto.APNSPojo;
import com.otabekjan.fraud_protection.entity.AppNotification;
import com.otabekjan.fraud_protection.entity.Post;
import com.otabekjan.fraud_protection.entity.Region;
import com.otabekjan.fraud_protection.entity.User;
import com.otabekjan.fraud_protection.enums.NotificationType;
import com.otabekjan.fraud_protection.service.NotificationsService;
import com.otabekjan.fraud_protection.service.PostService;
import io.jmix.core.DataManager;
import io.jmix.core.FetchPlan;
import io.jmix.core.Id;
import io.jmix.core.SaveContext;
import io.jmix.core.event.EntityChangedEvent;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PostEventListener {

    private static final Logger log = LoggerFactory.getLogger(PostEventListener.class);
    private final DataManager dataManager;
    private final RabbitTemplate rabbitTemplate;
    private final NotificationsService notificationsService;

    private final Gson GSON = new GsonBuilder().create();
    private final Environment environment;


    @Value("${spring.rabbitmq.template.exchange}")
    private String exchange;
    @Value("${spring.rabbitmq.template.apns.routing-key}")
    private String apnsRouting;
    @Value("${tg-channel.bot-token}")
    private String botToken;

    @TransactionalEventListener
    public void onPostChangedAfterCommit(final EntityChangedEvent<Post> event) {
        if (event.getType() == EntityChangedEvent.Type.CREATED) {
            createAppEvent(event.getEntityId());
            sendEvents(event.getEntityId());
            postSocialMedia(event.getEntityId());
        }

    }

    private void postSocialMedia(Id<Post> entityId) {
        dataManager.load(entityId)
                .fetchPlan(FetchPlan.BASE)
                .fetchPlanProperties("region")
                .joinTransaction(false)
                .optional()
                .ifPresent(post -> {
                    String chatId = "@fraud_shield"; // or use channel ID like "-1001234567890"
                    String message = """
                            <b>%s</b>
                            
                            More — %s
                            
                            @fraud_shield | fraud-shield.com
                            """.formatted(post.getTitle(),
                            environment.getProperty("app.url") + "/web-posts/" + post.getId());

                    try {

                        String urlString = "https://api.telegram.org/bot" + botToken + "/sendMessage";

                        String urlParameters = "chat_id=" + URLEncoder.encode(chatId, "UTF-8") +
                                "&text=" + URLEncoder.encode(message, "UTF-8") +
                                "&parse_mode=HTML";

                        URL url = new URL(urlString);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                        conn.setDoOutput(true);
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                        OutputStream os = conn.getOutputStream();
                        os.write(urlParameters.getBytes());
                        os.flush();
                        os.close();

                        int responseCode = conn.getResponseCode();
                        System.out.println("POST response code: " + responseCode);

                        conn.disconnect();
                    } catch (Exception e) {
                        log.warn("TG posting error", e);
                    }
                });

    }

    private void createAppEvent(Id<Post> entityId) {
        dataManager.load(entityId)
                .fetchPlan(FetchPlan.BASE)
                .fetchPlanProperties("region")
                .joinTransaction(false)
                .optional()
                .ifPresent(post -> {
                    Region region = post.getRegion();
                    if (region != null) {
                        List<User> allUsers = dataManager.load(User.class)
                                .query("select e from User e where e.region = :region and e.appleDeviceToken is not null ")
                                .parameter("region", region)
                                .joinTransaction(false)
                                .list();

                        for (User user : allUsers) {

                            var appNotification = dataManager.create(AppNotification.class);
                            appNotification.setUser(user);
                            appNotification.setRead(false);
                            appNotification.setNotificationType(NotificationType.POST);
                            appNotification.setTitle(post.getTitle());

                            if (!$.isEmpty(post.getBody())) {
                                String abbreviated = StringUtils.abbreviate(post.getBody(), 100);
                                appNotification.setBody(abbreviated);
                            }
                            appNotification.setPayload(GSON.toJson(Map.of("postId", post.getId())));

                            var saveContext = new SaveContext().setJoinTransaction(false).saving(appNotification);
                            dataManager.save(saveContext);
                        }
                    }
                });
    }

    void sendEvents(Id<Post> id) {
        dataManager.load(id)
                .fetchPlan(FetchPlan.BASE)
                .fetchPlanProperties("region")
                .joinTransaction(false)
                .optional()
                .ifPresent(post -> {
                    Region region = post.getRegion();
                    if (region != null) {
                        List<User> allUsers = dataManager.load(User.class)
                                .query("select e from User e where e.region = :region and e.appleDeviceToken is not null ")
                                .parameter("region", region)
                                .joinTransaction(false)
                                .list();

                        for (User user : allUsers) {
                            APNSPojo payload = new APNSPojo(post.getTitle(), NotificationType.POST,
                                    post.getId(),
                                    notificationsService.countUnReadNotifications(user).intValue(),
                                    user.getAppleDeviceToken());
                            rabbitTemplate.convertAndSend(exchange, apnsRouting, payload);
                        }
                    }
                });
    }
}