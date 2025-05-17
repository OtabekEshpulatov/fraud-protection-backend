package com.otabekjan.fraud_protection.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class PostDto implements Serializable {

    private static final long serialVersionUID = 7450376146821683468L;
    private UUID id;
    private String title;
    private String body;
    private String region;
    private PostUserDto user;
    private Long createdDate;

    private Long views;
    private Long comments;

    private String[] tags;
    private String[] mediaUrls;


    public Date getParsedCreatedDate() {
        if (createdDate != null) {
            return new Date(createdDate);
        }
        return null;
    }



}
