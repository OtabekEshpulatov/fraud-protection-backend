package com.otabekjan.fraud_protection.security;

import com.otabekjan.fraud_protection.entity.*;
import io.jmix.security.model.EntityAttributePolicyAction;
import io.jmix.security.model.EntityPolicyAction;
import io.jmix.security.role.annotation.EntityAttributePolicy;
import io.jmix.security.role.annotation.EntityPolicy;
import io.jmix.security.role.annotation.ResourceRole;

@ResourceRole(name = "UserRole", code = UserRole.CODE)
public interface UserRole {
    String CODE = "user-role";


    @EntityAttributePolicy(entityClass = Post.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = Post.class, actions = {EntityPolicyAction.READ})
    void posts();

    @EntityAttributePolicy(entityClass = Region.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = Region.class, actions = {EntityPolicyAction.READ})
    void regions();

    @EntityAttributePolicy(entityClass = TranslateEntity.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = TranslateEntity.class, actions = {EntityPolicyAction.READ})
    void translateEntities();

    @EntityPolicy(entityClass = PostRequest.class, actions = {EntityPolicyAction.CREATE})
    void postRequest();

    @EntityPolicy(entityClass = PostRequestMedia.class, actions = {EntityPolicyAction.CREATE})
    void postRequestMedia();

    @EntityAttributePolicy(entityClass = AppNotification.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = AppNotification.class, actions = {EntityPolicyAction.ALL})
    void appNotifications();

}