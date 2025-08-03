package hello.commerce.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
public class UserJoinResponseV1 {
    private String userId;
    private String email;
    private LocalDateTime createdAt;
}
