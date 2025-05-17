package com.otabekjan.fraud_protection.mvc;

import com.otabekjan.fraud_protection.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Locale;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class MVCPostController {


    private final PostService postService;

    @GetMapping("/web-posts/{id}")
    public String hello(Model model, @PathVariable UUID id) {
        model.addAttribute("postDto", postService.getPostById(id));
        model.addAttribute("related", postService.getRelatedPosts(id, Locale.ENGLISH, true));

        return "post-detail"; // refers to hello.html or hello.jsp
    }
}
