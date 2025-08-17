package hello.commerce.user.dto;

import hello.commerce.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class UserResponseV1 {

    private Long id;
    private String userId;
    private String email;
    private String role;

    public static UserResponseV1 fromEntity(User user) {
        return UserResponseV1.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
