package hello.commerce.user;

import hello.commerce.user.dto.*;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    /**
     * 사용자 상세 조회
     * @param userId
     * @return 주문 단건 응답
     */
    UserResponseV1 getUserById(String userId);

    /**
     * User 회원가입
     * @param request
     * @return
     */
    UserJoinResponseV1 joinUser(UserJoinRequestV1 request);
}
