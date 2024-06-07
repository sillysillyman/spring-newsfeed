package com.sparta.springnewsfeed.user;

import com.sparta.springnewsfeed.common.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public ResponseCode signup(SignupRequestDto requestDto) {
        String userid = requestDto.getUserid();
        String password = requestDto.getPassword();
        String email = requestDto.getEmail();

        // 중복된 사용자 확인
        if (userRepository.existsByUserid(userid)) {
            throw new IllegalArgumentException(ResponseCode.DUPLICATE_ENTITY.getMessage());
        }

        // 탈퇴한 사용자 확인
        if (userRepository.existsByUseridAndStatus(userid, UserStatusEnum.DELETED)) {
            throw new IllegalArgumentException(ResponseCode.INVALID_INPUT_VALUE.getMessage());
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(password);

        // 사용자 생성 및 저장
        User user = new User();
        user.setUserid(userid);
        user.setPassword(encodedPassword); // 암호화된 비밀번호 저장
        user.setEmail(email);
        user.setStatus(UserStatusEnum.UNVERIFIED); // 초기 상태를 UNVERIFIED로 설정

        userRepository.save(user);

        return ResponseCode.CREATED;
    }
}
