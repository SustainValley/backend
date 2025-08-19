package com.likelion.hackathon.controller;

import com.likelion.hackathon.dto.CafeDto.*;
import com.likelion.hackathon.dto.MessageResponseDto;
import com.likelion.hackathon.entity.Cafe;
import com.likelion.hackathon.entity.CafeImage;
import com.likelion.hackathon.entity.CafeOperatingHours;
import com.likelion.hackathon.entity.enums.SpaceType;
import com.likelion.hackathon.repository.CafeRepository;
import com.likelion.hackathon.service.CafeOperatingService;
import com.likelion.hackathon.service.CafeService;
import io.swagger.v3.oas.annotations.Operation;
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
    private final CafeOperatingService cafeOperatingService;

    // 카페 이름 조회
    @GetMapping("/{cafeId}/name")
    @Operation(summary = "카페 이름 조회", description = "카페 id로 카페 이름을 반환합니다.")
    public ResponseEntity<CafeNameDto> getCafeName(@PathVariable Long cafeId) {
        return ResponseEntity.ok(cafeService.getCafeNameById(cafeId));
    }

    @Operation(summary = "특정 카페 정보 조회", description = "특정 카페의 모든 정보를 반환합니다")
    @GetMapping("/{cafeId}")
    public ResponseEntity<CafeResponseDto> getCafe(
            @PathVariable Long cafeId
    ) {
        Cafe cafe = cafeService.getCafeById(cafeId);
        return ResponseEntity.ok(new CafeResponseDto(cafe));
    }

    @Operation(summary = "특정 카페 정보 수정", description = "특정 카페의 정보를 수정하여 업데이트 후 모든 정보를 반환합니다")
    @PatchMapping("/{cafeId}/update")
    public ResponseEntity<CafeResponseDto> updateCafe(
            @PathVariable Long cafeId,
            @RequestBody CafeUpdateRequestDto request
    ) {
        Cafe updatedCafe = cafeService.updateCafe(cafeId, request);
        return ResponseEntity.ok(new CafeResponseDto(updatedCafe));
    }

    @Operation(summary = "특정 카페에 사진 추가", description = "특정 카페에 사진을 등록합니다.")
    @PostMapping("/{cafeId}/images")
    public ResponseEntity<MessageResponseDto> addCafeImage(
            @PathVariable Long cafeId,
            @RequestParam("image") MultipartFile imageFile) throws IOException {

        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카페입니다."));

        String uploadDir = System.getProperty("user.dir") + "/uploads/cafe/";
        String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);

        Files.createDirectories(filePath.getParent());
        Files.write(filePath, imageFile.getBytes());

        CafeImage cafeImage = new CafeImage();
        cafeImage.setImageUrl("/uploads/cafe/" + fileName);
        cafeImage.setCafe(cafe);
        cafe.getImages().add(cafeImage);

        cafeRepository.save(cafe);

        return ResponseEntity.ok(new MessageResponseDto("카페 이미지가 추가되었습니다."));
    }

    @Operation(summary = "특정 카페의 특정 사진 삭제", description = "특정 카페의 특정 사진 삭제")
    @DeleteMapping("/{cafeId}/images/{imageId}/delete")
    public ResponseEntity<MessageResponseDto> deleteCafeImage(
            @PathVariable Long cafeId,
            @PathVariable Long imageId) {

        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카페입니다."));

        CafeImage image = cafe.getImages().stream()
                .filter(img -> img.getId().equals(imageId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이미지입니다."));

        cafe.getImages().remove(image);
        cafeRepository.save(cafe);

        return ResponseEntity.ok(new MessageResponseDto("카페 이미지가 삭제되었습니다."));
    }


    @Operation(summary = "특정 카페의 운영시간 반환", description = "특정 카페의 운영시간을 반환합니다")
    @GetMapping("/{cafeId}/operating")
    public ResponseEntity<CafeOperatingDto> getOperatingHours(@PathVariable Long cafeId) {
        CafeOperatingHours hours = operatingService.getOperatingHours(cafeId);
        return ResponseEntity.ok(CafeOperatingDto.fromEntity(hours));
    }

    @Operation(summary = "특정 카페의 운영시간 수정", description = "특정 카페의 운영시간 정보를 수정하여 업데이트 후 다시 반환합니다")
    @PatchMapping("/{cafeId}/operating/update")
    public ResponseEntity<CafeOperatingDto> updateOperating(
            @PathVariable Long cafeId,
            @RequestBody CafeOperatingDto request
    ) {
        CafeOperatingHours updated = operatingService.updateOperatingHours(cafeId, request);
        return ResponseEntity.ok(CafeOperatingDto.fromEntity(updated));
    }

    // 카페 예약 가능 시간 설정/수정/막기
    @PatchMapping("/{cafe_id}/abletime/update")
    @Operation(summary = "카페 예약 가능 시간/상태 수정", description = "예약 가능 시작/종료 시간 및 상태를 수정합니다.")
    public ResponseEntity<MessageResponseDto> updateAbleTime(
            @PathVariable("cafe_id") Long cafeId,
            @RequestBody CafeAbleTimeRequestDto dto) {

        cafeOperatingService.updateAbleTime(cafeId, dto);
        return ResponseEntity.ok(new MessageResponseDto("예약 가능 정보가 성공적으로 업데이트되었습니다."));
    }

    @Operation(summary = "모든 카페 리스트 반환", description = "모든 카페를 특정 정보와 함께 리스트로 반환합니다")
    @GetMapping("/cafelist")
    public ResponseEntity<List<CafeListDto>> getCafeList() {
        List<CafeListDto> cafeList = cafeService.getCafeList();
        return ResponseEntity.ok(cafeList);
    }

    // 검색어 검색 API
    @GetMapping("/search")
    @Operation(summary = "키워드로 검색", description = "뒤에 ?keyword={keyword}")
    public ResponseEntity<List<CafeListDto>> searchByKeyword(@RequestParam String keyword) {
        List<CafeListDto> result = cafeService.searchByKeyword(keyword).stream()
                .map(CafeListDto::fromEntity)
                .toList();
        return ResponseEntity.ok(result);
    }

    // 필터 검색 API
    @GetMapping("/filter")
    @Operation(summary = "필터 검색", description = "뒤에 ?spaceType={}, ?maxSeats={} 따로도 ?spaceType={}&maxSeats={} 같이도 가능")
    public ResponseEntity<List<CafeListDto>> filterCafes(
            @RequestParam(required = false) SpaceType spaceType,
            @RequestParam(required = false) Long maxSeats
    ) {
        List<CafeListDto> result = cafeService.filterCafes(spaceType, maxSeats).stream()
                .map(CafeListDto::fromEntity)
                .toList();
        return ResponseEntity.ok(result);
    }


}
