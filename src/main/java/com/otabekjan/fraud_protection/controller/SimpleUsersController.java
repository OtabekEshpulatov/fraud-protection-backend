package com.otabekjan.fraud_protection.controller;

import com.otabekjan.fraud_protection.entity.User;
import io.jmix.core.DataManager;
import io.jmix.core.EntitySerialization;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class SimpleUsersController {


    private final DataManager dataManager;
    private final EntitySerialization entitySerialization;


    @GetMapping
    public ResponseEntity<?> getUsers() {
        List<User> users = dataManager.load(User.class).query("select e from User e")
                .list();

        String json = entitySerialization.toJson(users);
        return ResponseEntity.ok(json);
    }
}
