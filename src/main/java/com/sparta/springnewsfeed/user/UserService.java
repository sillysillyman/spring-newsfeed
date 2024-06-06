package com.sparta.springnewsfeed.user;

import com.sparta.springnewsfeed.common.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public ResponseCode signup(SignupRequestDto requestDto) {
        String userid = requestDto.getUserid();
        String password = requestDto.getPassword();
        String email = requestDto.getEmail();

        // 중복된 사용자 확인
        if (userRepository.existsByUserid(userid)) {
            return ResponseCode.DUPLICATE_ENTITY;
        }

        // 탈퇴한 사용자 확인
        if (userRepository.existsByUseridAndStatus(userid, UserStatusEnum.DELETED)) {
            return ResponseCode.INVALID_INPUT_VALUE;
        }

        // 사용자 생성 및 저장
        User user = new User();
        user.setUserid(userid);
        user.setPassword(password);
        user.setEmail(email);
        user.setStatus(UserStatusEnum.UNVERIFIED); // 초기 상태를 UNVERIFIED로 설정

        userRepository.save(user);

        return ResponseCode.CREATED;
    }
}
