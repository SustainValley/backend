package com.likelion.hackathon.repository;

import com.likelion.hackathon.entity.Cafe;
import com.likelion.hackathon.entity.enums.CafeReservationStatus;
import com.likelion.hackathon.entity.enums.SpaceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CafeRepository extends JpaRepository<Cafe, Long> {

    // 예약 가능상태만 리스트에 표시
    List<Cafe> findByReservationStatus(CafeReservationStatus status);

    // 검색어 검색
    List<Cafe> findByNameContainingIgnoreCase(String keyword);

    // 필터 검색
    List<Cafe> findBySpaceType(SpaceType spaceType);
    List<Cafe> findByMaxSeatsGreaterThanEqual(Long maxSeats);
    List<Cafe> findBySpaceTypeAndMaxSeatsGreaterThanEqual(SpaceType spaceType, Long maxSeats);
}
