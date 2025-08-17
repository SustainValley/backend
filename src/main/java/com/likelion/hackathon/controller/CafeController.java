package com.likelion.hackathon.controller;

import com.likelion.hackathon.dto.CafeDto.CafeResponseDto;
import com.likelion.hackathon.dto.CafeDto.CafeUpdateRequestDto;
import com.likelion.hackathon.entity.Cafe;
import com.likelion.hackathon.service.CafeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cafe")
@RequiredArgsConstructor
public class CafeController {

    private final CafeService cafeService;

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

}
