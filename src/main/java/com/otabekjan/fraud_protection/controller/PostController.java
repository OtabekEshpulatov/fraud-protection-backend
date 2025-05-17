package com.otabekjan.fraud_protection.controller;

import com.otabekjan.fraud_protection.dto.PostDto;
import com.otabekjan.fraud_protection.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable UUID id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @GetMapping("/latest")
    public ResponseEntity<List<PostDto>> getPosts(@RequestParam(defaultValue = "-1") int limit,
                                                  @RequestParam(defaultValue = "0") int skip,
                                                  HttpServletRequest request) {
        return ResponseEntity.ok(postService.getPosts(limit, skip, request.getLocale()));
    }


    @GetMapping("/get-similar")
    public ResponseEntity<List<PostDto>> getPosts(@RequestParam UUID postId,
                                                  @RequestParam(defaultValue = "5") int limit,
                                                  HttpServletRequest request) {
        var similarPosts = postService.getSimilarPostsAndIncrementView(postId, request.getLocale(), limit, false);

        return ResponseEntity.ok(similarPosts);
    }

}
