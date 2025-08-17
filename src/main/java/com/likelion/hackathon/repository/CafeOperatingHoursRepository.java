package com.likelion.hackathon.repository;

import com.likelion.hackathon.entity.Cafe;
import com.likelion.hackathon.entity.CafeOperatingHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CafeOperatingHoursRepository extends JpaRepository<CafeOperatingHours, Long> {

    // 특정 카페의 운영시간 조회
    Optional<CafeOperatingHours> findByCafe(Cafe cafe);
}
