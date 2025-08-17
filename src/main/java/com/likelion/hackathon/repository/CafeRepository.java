package com.likelion.hackathon.repository;

import com.likelion.hackathon.entity.Cafe;
import com.likelion.hackathon.entity.enums.SpaceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CafeRepository extends JpaRepository<Cafe, Long> {

    // 검색어 검색
    List<Cafe> findByNameContainingIgnoreCase(String keyword);

    // 필터 검색
    List<Cafe> findBySpaceType(SpaceType spaceType);
    List<Cafe> findByMaxSeatsGreaterThanEqual(Long maxSeats);
    List<Cafe> findBySpaceTypeAndMaxSeatsGreaterThanEqual(SpaceType spaceType, Long maxSeats);
}
