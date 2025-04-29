package com.otabekjan.fraud_protection.service;

import com.otabekjan.fraud_protection.dto.RegisterRequestDto;
import com.otabekjan.fraud_protection.entity.User;
import com.otabekjan.fraud_protection.exceptions.FriendlyException;
import com.otabekjan.fraud_protection.security.UserRole;
import io.jmix.core.DataManager;
import io.jmix.core.FileRef;
import io.jmix.core.security.Authenticated;
import io.jmix.security.role.assignment.RoleAssignmentRoleType;
import io.jmix.securitydata.entity.RoleAssignmentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {


    private final DataManager dataManager;
    private final PasswordEncoder passwordEncoder;
    private final FileService fileService;

    @Authenticated
    @Transactional
    public User registerUser(RegisterRequestDto dto) {
        Pattern compile = Pattern.compile("(([a-zA-Z]+)(_*)([0-9]*)){3,10}");
        Matcher matcher = compile.matcher(dto.username());
        if (!matcher.matches()) {
            throw new FriendlyException("Invalid username");
        }

        User user = dataManager.load(User.class)
                .query("select e from User e where lower(e.username) = lower(:username)")
                .parameter("username", dto.username())
                .optional().orElse(null);

        if (user != null) {
            throw new FriendlyException("Username already taken");
        }

        user = dataManager.create(User.class);
        user.setUsername(dto.username());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setLocale(dto.locale());

        FileRef fileRef = fileService.decode(dto.profilePhotoId());
        if (fileRef != null) user.setProfilePhoto(fileRef);

        User newUser = dataManager.save(user);
        assignBasicUserRoles(newUser);

        return newUser;
    }

    @Authenticated
    private void assignBasicUserRoles(User newUser) {
        if (newUser == null) return;

        RoleAssignmentEntity roleAssignment = dataManager.unconstrained().create(RoleAssignmentEntity.class);
        roleAssignment.setRoleCode(UserRole.CODE);
        roleAssignment.setUsername(newUser.getUsername());
        roleAssignment.setRoleType(RoleAssignmentRoleType.RESOURCE);
        dataManager.unconstrained().save(roleAssignment);
    }
}
