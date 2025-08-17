package com.likelion.hackathon.controller;

import com.likelion.hackathon.dto.CafeDto.CafeListDto;
import com.likelion.hackathon.dto.CafeDto.CafeOperatingDto;
import com.likelion.hackathon.dto.CafeDto.CafeResponseDto;
import com.likelion.hackathon.dto.CafeDto.CafeUpdateRequestDto;
import com.likelion.hackathon.dto.MessageResponseDto;
import com.likelion.hackathon.entity.Cafe;
import com.likelion.hackathon.entity.CafeOperatingHours;
import com.likelion.hackathon.repository.CafeRepository;
import com.likelion.hackathon.service.CafeOperatingService;
import com.likelion.hackathon.service.CafeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cafe")
@RequiredArgsConstructor
public class CafeController {

    private final CafeService cafeService;
    private final CafeOperatingService operatingService;
    private final CafeRepository cafeRepository;

    @GetMapping("/{cafeId}")
    public ResponseEntity<CafeResponseDto> getCafe(
            @PathVariable Long cafeId
    ) {
        Cafe cafe = cafeService.getCafeById(cafeId);
        return ResponseEntity.ok(new CafeResponseDto(cafe));
    }

    @PatchMapping("/{cafeId}/update")
    public ResponseEntity<CafeResponseDto> updateCafe(
            @PathVariable Long cafeId,
            @RequestBody CafeUpdateRequestDto request
    ) {
        Cafe updatedCafe = cafeService.updateCafe(cafeId, request);
        return ResponseEntity.ok(new CafeResponseDto(updatedCafe));
    }

    @PatchMapping("/{cafeId}/image/update")
    public ResponseEntity<MessageResponseDto> updateCafeImage(
            @PathVariable Long cafeId,
            @RequestParam("image") MultipartFile imageFile) throws IOException {

        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카페입니다."));

        String uploadDir = System.getProperty("user.dir") + "/uploads/cafe/";
        String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);

        Files.createDirectories(filePath.getParent());
        Files.write(filePath, imageFile.getBytes());

        cafe.setImageUrl("/uploads/cafe/" + fileName);
        cafeRepository.save(cafe);

        return ResponseEntity.ok(new MessageResponseDto("카페 이미지가 업데이트되었습니다."));
    }

    @GetMapping("/{cafeId}/operating")
    public ResponseEntity<CafeOperatingDto> getOperatingHours(@PathVariable Long cafeId) {
        CafeOperatingHours hours = operatingService.getOperatingHours(cafeId);
        return ResponseEntity.ok(CafeOperatingDto.fromEntity(hours));
    }

    @PatchMapping("/{cafeId}/operating/update")
    public ResponseEntity<CafeOperatingDto> updateOperating(
            @PathVariable Long cafeId,
            @RequestBody CafeOperatingDto request
    ) {
        CafeOperatingHours updated = operatingService.updateOperatingHours(cafeId, request);
        return ResponseEntity.ok(CafeOperatingDto.fromEntity(updated));
    }


    @GetMapping("/cafelist")
    public ResponseEntity<List<CafeListDto>> getCafeList() {
        List<CafeListDto> cafeList = cafeService.getCafeList();
        return ResponseEntity.ok(cafeList);
    }

}
