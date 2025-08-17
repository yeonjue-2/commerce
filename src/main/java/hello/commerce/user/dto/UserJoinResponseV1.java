package hello.commerce.user.dto;

import hello.commerce.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class UserJoinResponseV1 {
    private String userId;
    private String email;

    public static UserJoinResponseV1 fromEntity(User user) {
        return UserJoinResponseV1.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .build();
    }
}
