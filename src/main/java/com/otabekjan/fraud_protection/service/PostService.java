package com.otabekjan.fraud_protection.service;

import com.otabekjan.fraud_protection.$;
import com.otabekjan.fraud_protection.dto.PostDto;
import com.otabekjan.fraud_protection.dto.PostUserDto;
import com.otabekjan.fraud_protection.entity.*;
import io.jmix.core.*;
import io.jmix.core.security.Authenticated;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.core.security.SystemAuthenticator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PostService {


    private final DataManager dataManager;
    private final SystemAuthenticator systemAuthenticator;
    private final CurrentAuthentication currentAuthentication;
    private final TimeSource timeSource;
    private final TranslateService translateService;
    private final EntityService entityService;

    private static String[] split(String tags) {
        if (tags == null) return new String[]{};
        String[] split = tags.split(",");
        for (int i = 0; i < split.length; i++) {
            split[i] = split[i].trim();
        }
        return split;
    }

    public Post approvePost(PostRequest request, String tags) {
        if (request == null) return null;

        Post post = dataManager.create(Post.class);
        post.setActive(true);
        post.setTitle(request.getTitle());
        post.setBody(request.getBody());
        post.setTags(tags);
        post.setRegion(request.getRegion());
        post.setUser(request.getUser());

        List<PostMedia> postMedias = new ArrayList<>();
        for (PostRequestMedia requestMedia : $.nonNull(request.getMedias())) {
            PostMedia postMedia = dataManager.create(PostMedia.class);
            postMedia.setSort(requestMedia.getSort());
            postMedia.setMedia(requestMedia.getMedia());
            postMedia.setPost(post);
            postMedias.add(postMedia);
        }

        post.setMedias(postMedias);

        request.setApprovedBy((User) currentAuthentication.getUser());
        request.setApproveDate(timeSource.now().toOffsetDateTime());
        dataManager.save(request);

        return dataManager.save(post);
    }

    @Transactional
    public List<PostDto> getSimilarPostsAndIncrementView(UUID postId, Locale locale, int limit, boolean mediaInline) {
        List<PostDto> similarPosts = getSimilarPosts(postId, locale, limit, mediaInline);
        incrementViewCount(postId);
        return similarPosts;
    }

    public void incrementViewCount(UUID postId) {
        systemAuthenticator.runWithUser("admin", () -> dataManager.load(Id.of(postId, Post.class)).fetchPlanProperties("views").optional().ifPresent(post -> {
            long incrementedViewsCount = Objects.requireNonNullElse(post.getViews(), 0L) + 1;
            post.setViews(incrementedViewsCount);
            dataManager.save(post);
        }));
    }

    public List<PostDto> getSimilarPosts(UUID postId, Locale locale, int limit, boolean mediaInline) {
        String tags = dataManager.loadValue("select e.tags from Post e where e.id = :id", String.class)
                .parameter("id", postId)
                .optional().orElse("");

        String uniqueTags = new HashSet<>(Arrays.asList(split(tags)))
                .stream().reduce((s, s2) -> s + "," + s2)
                .orElse("");

        List<Post> relevantPosts = dataManager.load(Post.class)
                .query("select e from Post e where e.id <> :postId and function('count_occurrences',e.tags, :matcherTags) > 0 order by function('count_occurrences',e.tags, :matcherTags) desc")
                .parameter("postId", postId)
                .parameter("matcherTags", uniqueTags)
                .maxResults(limit)
                .list();

        return conver2Dto(locale, relevantPosts);
    }

    public List<PostDto> getPosts(int limit, int skip, Locale locale) {
        FluentLoader.ByQuery<Post> loader = dataManager.load(Post.class)
                .query("select e from Post e where e.active = true and e.user.active = true order by e.createdDate desc")
                .fetchPlan("latest-posts");

        if (limit > -1) {
            loader.maxResults(limit);
        }

        if (skip > 0) {
            loader.firstResult(skip);
        }

        List<Post> posts = loader.list();

        return conver2Dto(locale, posts);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PostDto transformPost(Locale locale, Post post) {
        post = entityService.reload(post, "post-apns-plan");
        return conver2Dto(locale, List.of(post)).get(0);
    }

    private List<PostDto> conver2Dto(Locale locale, List<Post> posts) {
        List<PostDto> postDtos = new ArrayList<>(posts.size());
        for (Post post : posts) {
            PostDto dto = PostDto.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .body(post.getBody())
                    .tags(split(post.getTags()))
                    .mediaUrls(getPostMediaUrls(post.getMedias()))
                    .user(getPostUser(post.getUser()))
                    .createdDate(post.getCreatedDate().toEpochSecond())
                    .region(translate(post.getRegion(), locale.getLanguage()))
                    .views(post.getViews())
                    .build();
            postDtos.add(dto);
        }
        return postDtos;
    }

    private <T extends HasName & AppEntity<UUID>> String translate(T hasName, String locale) {
        if (hasName == null) return null;
        return translateService.translate(hasName, "name", locale, hasName.getName());
    }

    private String[] getPostMediaUrls(List<PostMedia> medias) {
        if (medias == null) return null;

        String[] urls = new String[medias.size()];
        for (int i = 0; i < medias.size(); i++) {
            FileRef media = medias.get(i).getMedia();
            urls[i] = $.makeFileUrl(media);
        }

        return urls;

    }

    private PostUserDto getPostUser(User user) {
        if (user == null) return null;
        return PostUserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .verified(user.getVerified())
                .profilePhotoUrl($.makeFileUrl(user.getProfilePhoto()))
                .build();
    }


    @Authenticated
    @Transactional
    public PostDto getPostById(UUID id) {
        Locale locale = null;
        if (currentAuthentication.getUser() instanceof User user) {
            String localeStr = Objects.requireNonNullElse(user.getLocale(), "ru");
            locale = Locale.forLanguageTag(localeStr);
        } else {
            locale = Locale.forLanguageTag("ru");
        }

        Post post = entityService.loadById(Post.class, id);
        return transformPost(locale, post);
    }

    public Collection<PostDto> getRelatedPosts(UUID id, Locale locale, boolean mediaInline) {
        return getSimilarPosts(id, locale, Integer.MAX_VALUE, mediaInline);
    }
}
