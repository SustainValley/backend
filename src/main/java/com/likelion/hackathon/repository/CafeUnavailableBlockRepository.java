package com.likelion.hackathon.repository;

import com.likelion.hackathon.entity.Cafe;
import com.likelion.hackathon.entity.CafeUnavailableBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.likelion.hackathon.entity.enums.DayOfWeek;

import java.util.List;

@Repository
public interface CafeUnavailableBlockRepository extends JpaRepository<CafeUnavailableBlock, Long> {
    List<CafeUnavailableBlock> findByCafeAndDayOfWeek(Cafe cafe, DayOfWeek dayOfWeek);
}