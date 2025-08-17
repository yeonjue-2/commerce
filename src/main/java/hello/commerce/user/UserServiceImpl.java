package hello.commerce.user;

import hello.commerce.common.exception.BusinessException;
import hello.commerce.common.exception.ErrorCode;
import hello.commerce.user.dto.*;
import hello.commerce.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserResponseV1 getUserById(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        return UserResponseV1.fromEntity(user);
    }

    @Override
    public UserJoinResponseV1 joinUser(UserJoinRequestV1 request) {
        if (userRepository.existsByUserId(request.getUserId())) {
            throw new BusinessException(ErrorCode.USER_ALREADY_EXISTS);
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ErrorCode.USER_ALREADY_EXISTS);
        }

        String encodedPassword = bCryptPasswordEncoder.encode(request.getPassword());
        User user = User.builder()
                .userId(request.getUserId())
                .password(encodedPassword)
                .email(request.getEmail())
                .role("ROLE_USER")
                .build();

        userRepository.save(user);

        return UserJoinResponseV1.fromEntity(user);

    }
}
