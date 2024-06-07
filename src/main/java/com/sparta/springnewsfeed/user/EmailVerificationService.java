package com.sparta.springnewsfeed.user;

import com.sparta.springnewsfeed.common.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class EmailVerificationService {

    private final UserRepository userRepository;
    private final EmailVerificationRepository verificationRepository;
    private final JavaMailSender mailSender;

    @Autowired
    public EmailVerificationService(UserRepository userRepository,
                                    EmailVerificationRepository verificationRepository,
                                    JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.verificationRepository = verificationRepository;
        this.mailSender = mailSender;
    }

    // 인증 코드를 생성하고 이메일로 전송하는 메서드
    @Transactional
    public void createVerificationCode(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일을 가진 사용자를 찾을 수 없습니다."));

        String code = generateVerificationCode();

        // 기존 인증 코드가 있는지 확인
        Optional<EmailVerification> existingVerification = verificationRepository.findByUser(user);
        EmailVerification verification;
        if (existingVerification.isPresent()) {
            // 기존 인증 코드가 있으면 업데이트
            verification = existingVerification.get();
            verification.setCode(code);
            verification.setExpiryDate(LocalDateTime.now().plusMinutes(3)); // 180초간 유효
        } else {
            // 기존 인증 코드가 없으면 새로 생성
            verification = new EmailVerification(user, code);
        }

        verificationRepository.save(verification);

        String recipientAddress = user.getEmail();
        String subject = "이메일 인증";
        String message = "이메일 인증번호는 다음과 같습니다: " + code;

        sendEmail(recipientAddress, subject, message);
    }


    // 이메일을 전송하는 메서드
    private void sendEmail(String to, String subject, String text) {
        // SimpleMailMessage 객체를 생성하고 설정합니다.
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        // 메일을 전송합니다.
        mailSender.send(message);
    }

    // 6자리 인증 코드를 생성하는 메서드
    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 6자리 숫자 생성
        return String.valueOf(code);
    }

    // 사용자가 입력한 인증 코드를 검증하는 메서드
    @Transactional
    public ResponseCode verifyEmail(String code) {
        // 인증 코드로 EmailVerification 객체를 조회합니다. 없으면 예외를 발생시킵니다.
        EmailVerification verification = verificationRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException(ResponseCode.INVALID_INPUT_VALUE.getMessage()));

        // 인증 코드의 유효 기간을 확인합니다. 만료되었으면 예외를 발생시킵니다.
        if (verification.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException(ResponseCode.INVALID_INPUT_VALUE.getMessage());
        }

        // 인증이 성공하면 유저의 상태를 VERIFIED로 변경하고 저장
        User user = verification.getUser();
        user.setStatus(UserStatusEnum.VERIFIED);
        userRepository.save(user);

        // 사용된 인증 코드는 삭제
        verificationRepository.delete(verification);

        // 성공 응답 코드를 반환
        return ResponseCode.SUCCESS;
    }
}
