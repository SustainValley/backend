package com.likelion.hackathon.service;

import com.likelion.hackathon.dto.CafeDto.CafeUpdateRequestDto;
import com.likelion.hackathon.entity.Cafe;
import com.likelion.hackathon.repository.CafeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CafeService {

    private final CafeRepository cafeRepository;

    @Transactional
    public Cafe updateCafe(Long cafeId, CafeUpdateRequestDto request) {
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카페입니다."));

        if(request.getMinOrder() != null) cafe.setMinOrder(request.getMinOrder());
        if(request.getMaxCapacity() != null) cafe.setMaxSeats(request.getMaxCapacity().longValue());
        if(request.getSpaceType() != null) cafe.setSpaceType(request.getSpaceType());

        return cafeRepository.save(cafe);
    }

    @Transactional(readOnly = true)
    public Cafe getCafeById(Long cafeId) {
        return cafeRepository.findById(cafeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카페입니다."));
    }

}
