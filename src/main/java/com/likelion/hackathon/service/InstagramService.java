package com.likelion.hackathon.service;

import com.likelion.hackathon.apiPayload.code.status.ErrorStatus;
import com.likelion.hackathon.apiPayload.exception.handler.InstagramHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class InstagramService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${instagram.api-url}")
    private String apiUrl;

    @Value("${instagram.access-token}")
    private String accessToken;

    @Value("${instagram.user-id}")
    private String userId;

    private final S3Service s3Service;


    // 메인 메서드: 사진 URL을 받아 스토리로 업로드
    public String uploadStory(MultipartFile file) {
        // 이미지를 s3에 업로드
        String imageUrl = s3Service.uploadImage(file, "instagram");

        // 미디어 컨테이너 생성 (STORIES 모드)
        String containerId = createContainer(imageUrl);
        log.info("컨테이너 생성 완료 ID: {}", containerId);


        try {
            log.info("이미지 처리 대기 중... (10초)");
            Thread.sleep(10000); // 10초 대기
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new InstagramHandler(ErrorStatus._INSTA_UPLOAD_ERROR);
        }

        // 미디어 게시 (Publish)
        String resultId = publishContainer(containerId);
        log.info("스토리 게시 완료 ID: {}", resultId);

        // 스토리 링크 반환
        return "https://www.instagram.com/stories/mocacafe_swu/" + resultId + "/";
    }

    public String createContainer(String imageUrl) {
        String url = String.format("%s/%s/media", apiUrl, userId);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image_url", imageUrl);
        body.add("media_type", "STORIES"); // 스토리 필수 옵션
        body.add("access_token", accessToken);

        // API 요청 보내기
        ResponseEntity<Map> response = restTemplate.postForEntity(url, body, Map.class);

        // ID 추출
        return (String) response.getBody().get("id");
    }

    public String publishContainer(String creationId) {
        String url = String.format("%s/%s/media_publish", apiUrl, userId);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("creation_id", creationId);
        body.add("access_token", accessToken);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, body, Map.class);

        return (String) response.getBody().get("id");
    }
}
