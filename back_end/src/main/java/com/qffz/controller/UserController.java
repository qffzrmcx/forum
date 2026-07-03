package com.qffz.controller;

import com.qffz.dto.*;
import com.qffz.pojo.User;
import com.qffz.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Result<Void> register(@RequestBody RegisterRequest request) {
        userService.register(request);
        return Result.ok();
    }

    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest request) {
        return Result.ok(userService.login(request));
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        return Result.ok();
    }

    @GetMapping("/info")
    public Result<User> info() {
        return Result.ok(userService.info());
    }

    @PutMapping("/update")
    public Result<Void> update(@RequestBody User user) {
        userService.update(user);
        return Result.ok();
    }

    @GetMapping("/posts")
    public Result<PageResult<PostView>> myPosts(@RequestParam(defaultValue = "1") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        return Result.ok(userService.myPosts(page, size));
    }

    @GetMapping("/collects")
    public Result<Object> myCollects() {
        return Result.ok(userService.myCollects());
    }
}
