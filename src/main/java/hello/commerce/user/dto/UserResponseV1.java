package hello.commerce.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class UserResponseV1 {

    private Long id;
    private String userId;
    private String email;
    private String role;
}
