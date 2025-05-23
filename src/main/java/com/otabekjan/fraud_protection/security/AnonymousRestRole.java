package com.otabekjan.fraud_protection.security;

import com.otabekjan.fraud_protection.entity.Post;
import com.otabekjan.fraud_protection.entity.Region;
import com.otabekjan.fraud_protection.entity.TranslateEntity;
import io.jmix.security.model.EntityAttributePolicyAction;
import io.jmix.security.model.EntityPolicyAction;
import io.jmix.security.role.annotation.EntityAttributePolicy;
import io.jmix.security.role.annotation.EntityPolicy;
import io.jmix.security.role.annotation.ResourceRole;

@ResourceRole(name = "AnonymousRestRole", code = AnonymousRestRole.CODE, scope = "API")
public interface AnonymousRestRole {

    String CODE = "anonymous-rest-role";

    @EntityAttributePolicy(entityClass = Post.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = Post.class, actions = {EntityPolicyAction.READ})
    void posts();

    @EntityAttributePolicy(entityClass = Region.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = Region.class, actions = {EntityPolicyAction.READ})
    void regions();

    @EntityAttributePolicy(entityClass = TranslateEntity.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = TranslateEntity.class, actions = {EntityPolicyAction.READ})
    void translateEntities();
}