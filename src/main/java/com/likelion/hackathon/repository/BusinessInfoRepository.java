package com.likelion.hackathon.repository;

import com.likelion.hackathon.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessInfoRepository extends JpaRepository<User, Long> {

}