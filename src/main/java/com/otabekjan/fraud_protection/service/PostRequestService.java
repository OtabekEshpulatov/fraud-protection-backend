package com.otabekjan.fraud_protection.service;

import com.otabekjan.fraud_protection.$;
import com.otabekjan.fraud_protection.dto.PostRequestDto;
import com.otabekjan.fraud_protection.entity.PostRequest;
import com.otabekjan.fraud_protection.entity.PostRequestMedia;
import com.otabekjan.fraud_protection.entity.Region;
import com.otabekjan.fraud_protection.entity.User;
import com.otabekjan.fraud_protection.exceptions.FriendlyException;
import io.jmix.core.DataManager;
import io.jmix.core.FileRef;
import io.jmix.core.Messages;
import io.jmix.core.security.CurrentAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostRequestService {


    private final DataManager dataManager;
    private final EntityService entityService;
    private final CurrentAuthentication currentAuthentication;
    private final FileService fileService;
    private final Messages messages;

    public PostRequest makeRequest(PostRequestDto dto) {
        Objects.requireNonNull(dto);

        PostRequest request = dataManager.create(PostRequest.class);
        request.setUser((User) currentAuthentication.getUser());
        request.setTitle(dto.getTitle());
        request.setBody(dto.getBody());

        // setting region
        Region region = entityService.loadById(Region.class, dto.getRegionId());
        if (region == null) {
            throw new FriendlyException(messages.getMessage("region-not-found"));
        }
        request.setRegion(region);

        // saving media files
        List<PostRequestMedia> postRequestMediaList = new LinkedList<>();
        List<FileRef> fileRefs = $.nonNull(loadMediaFiles(dto.getMediaIds()));
        for (int i = 0; i < fileRefs.size(); i++) {
            PostRequestMedia postRequestMedia = dataManager.create(PostRequestMedia.class);
            postRequestMedia.setMedia(fileRefs.get(i));
            postRequestMedia.setSort(i);
            postRequestMedia.setRequest(request);

            postRequestMediaList.add(postRequestMedia);
        }
        request.setMedias(postRequestMediaList);

        return dataManager.save(request);
    }

    private List<FileRef> loadMediaFiles(List<String> fileIds) {
        return $.nonNull(fileIds).stream().map(fileService::decode)
                .filter(Objects::nonNull)
                .toList();
    }
}
