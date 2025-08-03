package hello.commerce.user;

import hello.commerce.user.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping
public class UserController {

    private final UserService userService;

    @GetMapping("/v1/users/{user_id}")
    public ResponseEntity<UserResponseV1> getUserInfo(@PathVariable("user_id") String userId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @PostMapping("/v1/users/join")
    public ResponseEntity<UserJoinResponseV1> join(@Valid @RequestBody UserJoinRequestV1 userJoinRequest) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @PostMapping("/v1/users/login")
    public ResponseEntity<UserLoginResponseV1> login(@Valid @RequestBody UserLoginRequestV1 userLoginRequest) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
