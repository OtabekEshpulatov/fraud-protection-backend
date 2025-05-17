package com.otabekjan.fraud_protection.controller;

import com.otabekjan.fraud_protection.dto.NotificationsDto;
import com.otabekjan.fraud_protection.service.NotificationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class AppNotificationsController {

    private final NotificationsService notificationsService;

    @GetMapping
    public ResponseEntity<Collection<NotificationsDto>> getAll() {
        return ResponseEntity.ok(notificationsService.getAll());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getCount() {
        return ResponseEntity.ok(notificationsService.countUnReadNotifications());
    }

    @PostMapping("/read")
    public void readNotification(@RequestParam UUID id) {
        notificationsService.read(id);
    }
}
