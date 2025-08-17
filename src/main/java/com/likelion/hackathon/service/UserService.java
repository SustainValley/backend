package com.likelion.hackathon.service;

import com.likelion.hackathon.apiPayload.code.status.ErrorStatus;
import com.likelion.hackathon.apiPayload.exception.handler.ReservationHandler;
import com.likelion.hackathon.dto.MessageResponseDto;
import com.likelion.hackathon.dto.UserDto.SignupRequestDto;
import com.likelion.hackathon.entity.BusinessInfo;
import com.likelion.hackathon.entity.Cafe;
import com.likelion.hackathon.entity.User;
import com.likelion.hackathon.entity.UserType;
import com.likelion.hackathon.repository.BusinessInfoRepository;
import com.likelion.hackathon.repository.CafeRepository;
import com.likelion.hackathon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BusinessInfoRepository businessInfoRepository;
    private final CafeRepository cafeRepository;
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
        user.setNickname(request.getNickname());

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

            Cafe cafe = new Cafe();
            cafe.setBusinessInfo(businessInfo);
            cafe.setName(request.getBusinessname());
            cafe.setLocation(request.getAddress());
            // cafe.setMaxSeats(0L);
            // cafe.setSeatFee(0L);
            // cafe.setOpenTime(LocalTime.of(9, 0));
            // cafe.setCloseTime(LocalTime.of(18, 0));
            cafe.setContent("");// 빈 소개글

            userRepository.save(user);
            cafeRepository.save(cafe);

        }

        userRepository.save(user);
        return user.getId();
    }

    public MessageResponseDto checkUsername(String username) {
        boolean exists = userRepository.existsByUsername(username);
        if (exists) {
            return new MessageResponseDto("이미 사용중인 아이디 입니다");
        } else {
            return new MessageResponseDto("사용 가능한 아이디 입니다");
        }
    }

    // 사용자 반환 메서드
    public User existUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ReservationHandler(ErrorStatus._USER_NOT_FOUND));
    }
}
