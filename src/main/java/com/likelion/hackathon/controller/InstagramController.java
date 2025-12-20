package com.likelion.hackathon.controller;

import com.likelion.hackathon.apiPayload.ApiResponse;
import com.likelion.hackathon.service.InstagramService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/instagram")
@RequiredArgsConstructor
public class InstagramController {

    private final InstagramService instagramService;


    @Operation(summary = "스토리 업로드", description = "사진을 모카 인스타그램 계정 스토리에 업로드합니다.")
    @PostMapping(value = "/story", consumes = "multipart/form-data")
    public ApiResponse<String> uploadStory(@RequestParam("image") MultipartFile image) {
        String storyUrl = instagramService.uploadStory(image);
        return ApiResponse.onSuccess(storyUrl);
    }
}
