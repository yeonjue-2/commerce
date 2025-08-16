package hello.commerce.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@AllArgsConstructor
@Builder
public class UserJoinRequestV1 {
    @NotNull
    private String userId;

    @NotNull
    private String password;

    @NotNull
    private String email;
}
