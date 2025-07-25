package com.likelion.hackathon.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.hackathon.dto.OAuthDto.KakaoUserDto;
import com.likelion.hackathon.entity.User;
import com.likelion.hackathon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KakaoOAuthService {

    private final UserRepository userRepository;

    @Value("${OAUTH_KAKAO_CLIENT_ID}")
    private String clientId;

    @Value("${OAUTH_KAKAO_REDIRECT_URI}")
    private String redirectUri;

    @Value("${OAUTH_KAKAO_TOKEN_URI}")
    private String tokenUri;

    @Value("${OAUTH_KAKAO_USER_INFO_URI}")
    private String userInfoUri;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getKakaoAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(tokenUri, request, String.class);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response.getBody());
            return root.path("access_token").asText();
        } catch (Exception e) {
            throw new RuntimeException("카카오 Access Token 파싱 실패", e);
        }
    }


    public KakaoUserDto kakaoLogin(String code) {
        String accessToken = getKakaoAccessToken(code);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                request,
                String.class
        );

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response.getBody());

            String kakaoId = root.path("id").asText();
            String nickname = root.path("properties").path("nickname").asText();
            String email = root.path("kakao_account").path("email").asText(null);

            Optional<User> optionalUser = userRepository.findByUsername(kakaoId);

            User user = optionalUser.orElseGet(() -> {
                User newUser = new User();
                newUser.setUsername(kakaoId); // ID를 username으로
                newUser.setPassword("");
                newUser.setProvider("kakao");
                newUser.setEmail(email); // 추가 가능
                return userRepository.save(newUser);
            });

            return new KakaoUserDto(email, nickname, accessToken);

        } catch (Exception e) {
            throw new RuntimeException("카카오 사용자 정보 파싱 실패", e);
        }
    }


}

