package hello.commerce.user;

import hello.commerce.user.dto.UserJoinRequestV1;
import hello.commerce.user.dto.UserJoinResponseV1;
import hello.commerce.user.dto.UserLoginRequestV1;
import hello.commerce.user.dto.UserLoginResponseV1;
import hello.commerce.user.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    /**
     * 사용자 상세 조회
     * @param userId
     * @return 주문 단건 응답
     */
    User getUserById(Long userId);

    /**
     * User 회원가입
     * @param request
     * @return
     */
    UserJoinResponseV1 joinUser(UserJoinRequestV1 request);

    /**
     * User 로그인
     * @param request
     * @return
     */
    UserLoginResponseV1 loginUser(UserLoginRequestV1 request);
}
