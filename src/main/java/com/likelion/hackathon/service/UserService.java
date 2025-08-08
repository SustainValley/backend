package com.likelion.hackathon.service;

import com.likelion.hackathon.dto.UserDto.SignupRequestDto;
import com.likelion.hackathon.entity.BusinessInfo;
import com.likelion.hackathon.entity.User;
import com.likelion.hackathon.entity.UserType;
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

    private final UserRepository userRepository;
    private final BusinessInfoRepository businessInfoRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long signup(String type, SignupRequestDto request) {
        UserType userType = UserType.fromString(type);  // 잘못된 값이면 바로 예외

        User user = new User();

        if (userType == UserType.COR) {
            // 사장님은 username = businessnumber
            user.setUsername(request.getBusinessnumber());
        } else {
            user.setUsername(request.getUsername());
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setProvider(request.getProvider());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setType(userType);

        // 사장님(cor)일 경우 BusinessInfo 생성
        if (userType == UserType.COR) {
            BusinessInfo businessInfo = new BusinessInfo();
            businessInfo.setBusinessnumber(request.getBusinessnumber());
            businessInfo.setPresidentname(request.getPresidentname());
            businessInfo.setBusinessname(request.getBusinessname());
            businessInfo.setZipcode(request.getZipcode());
            businessInfo.setAddress(request.getAddress());
            businessInfo.setUser(user);

            user.setBusinessInfo(businessInfo);
        }

        userRepository.save(user);
        return user.getId();
    }
}
