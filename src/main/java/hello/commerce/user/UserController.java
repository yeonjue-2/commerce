package hello.commerce.user;

import hello.commerce.user.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/v1/users/{user_id}")
    public ResponseEntity<UserResponseV1> getUserInfo(@PathVariable("user_id") String userId) {
        UserResponseV1 userResponse = userService.getUserById(userId);
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/v1/users/join")
    public ResponseEntity<UserJoinResponseV1> join(@Valid @RequestBody UserJoinRequestV1 userJoinRequest) {
        UserJoinResponseV1 userJoinResponseV1 = userService.joinUser(userJoinRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userJoinResponseV1);
    }
}
