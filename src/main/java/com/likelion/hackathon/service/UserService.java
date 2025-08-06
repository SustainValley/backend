package com.likelion.hackathon.service;

import com.likelion.hackathon.dto.UserDto.SignupRequestDto;
import com.likelion.hackathon.entity.BusinessInfo;
import com.likelion.hackathon.entity.User;
import com.likelion.hackathon.repository.BusinessInfoRepository;
import com.likelion.hackathon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private final BusinessInfoRepository businessInfoRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long signup(String type, SignupRequestDto request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setProvider(request.getProvider());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setType(type);

        // 사장님(cor)일 경우 BusinessInfo 생성
        if ("cor".equals(type)) {
            BusinessInfo businessInfo = new BusinessInfo();
            businessInfo.setBusinessnumber(request.getBusinessnumber());
            businessInfo.setPresidentname(request.getPresidentname());
            businessInfo.setBusinessname(request.getBusinessname());
            businessInfo.setZipcode(request.getZipcode());
            businessInfo.setAddress(request.getAddress());
            businessInfo.setUser(user); // FK 연결

            user.setBusinessInfo(businessInfo);
        }

        userRepository.save(user);
        return user.getId();
    }
}
