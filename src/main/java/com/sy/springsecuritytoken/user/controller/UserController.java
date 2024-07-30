package com.sy.springsecuritytoken.user.controller;

import com.sy.springsecuritytoken.annotation.AnonymousCallable;
import com.sy.springsecuritytoken.response.Response;
import com.sy.springsecuritytoken.user.controller.command.SignUpCommand;
import com.sy.springsecuritytoken.user.controller.response.UserListResponse;
import com.sy.springsecuritytoken.user.service.UserService;
import com.sy.springsecuritytoken.user.service.dto.SignUpDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/user/login")
    public Response<Void> authenticateUser() {
        return Response.OK;
    }

    @AnonymousCallable
    @PostMapping(value = "/user/sign-up")
    public Response<String> signUp(@RequestBody SignUpCommand command) {
        final SignUpDto dto = userService.signUp(command);
        return new Response<>(dto.getEmail());
    }

    @GetMapping("/user")
    public Response<List<UserListResponse>> findAll() {
        List<UserListResponse> response = userService.findAll().stream()
            .map(UserListResponse::from)
            .toList();
        return new Response<>(response);
    }
}
