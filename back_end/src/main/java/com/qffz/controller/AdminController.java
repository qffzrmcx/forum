package com.qffz.controller;

import com.qffz.dto.*;
import com.qffz.pojo.Section;
import com.qffz.pojo.User;
import com.qffz.service.CommentService;
import com.qffz.service.PostService;
import com.qffz.service.SectionService;
import com.qffz.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;
    private final SectionService sectionService;

    public AdminController(UserService userService, PostService postService, CommentService commentService, SectionService sectionService) {
        this.userService = userService;
        this.postService = postService;
        this.commentService = commentService;
        this.sectionService = sectionService;
    }

    @GetMapping("/user/list")
    public Result<PageResult<User>> users(@RequestParam(required = false) String keyword,
                                          @RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        return Result.ok(userService.adminUsers(keyword, page, size));
    }

    @PutMapping("/user/disable/{id}")
    public Result<Void> disableUser(@PathVariable Long id) {
        userService.updateStatus(id, 0);
        return Result.ok();
    }

    @PutMapping("/user/enable/{id}")
    public Result<Void> enableUser(@PathVariable Long id) {
        userService.updateStatus(id, 1);
        return Result.ok();
    }

    @DeleteMapping("/user/delete/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return Result.ok();
    }

    @GetMapping("/post/list")
    public Result<PageResult<PostView>> posts(@RequestParam(required = false) String keyword,
                                              @RequestParam(defaultValue = "1") int page,
                                              @RequestParam(defaultValue = "10") int size) {
        return Result.ok(postService.adminList(keyword, page, size));
    }

    @DeleteMapping("/post/delete/{id}")
    public Result<Void> deletePost(@PathVariable Long id) {
        postService.delete(id);
        return Result.ok();
    }

    @PutMapping("/post/top/{id}")
    public Result<Void> topPost(@PathVariable Long id, @RequestParam Integer value) {
        postService.updateTop(id, value);
        return Result.ok();
    }

    @PutMapping("/post/essence/{id}")
    public Result<Void> essencePost(@PathVariable Long id, @RequestParam Integer value) {
        postService.updateEssence(id, value);
        return Result.ok();
    }

    @GetMapping("/comment/list")
    public Result<PageResult<CommentView>> comments(@RequestParam(required = false) String keyword,
                                                    @RequestParam(defaultValue = "1") int page,
                                                    @RequestParam(defaultValue = "10") int size) {
        return Result.ok(commentService.adminList(keyword, page, size));
    }

    @DeleteMapping("/comment/delete/{id}")
    public Result<Void> deleteComment(@PathVariable Long id) {
        commentService.delete(id);
        return Result.ok();
    }

    @GetMapping("/section/list")
    public Result<List<Section>> sections() {
        return Result.ok(sectionService.list(true));
    }

    @PostMapping("/section/add")
    public Result<Void> addSection(@RequestBody Section section) {
        sectionService.save(section);
        return Result.ok();
    }

    @PutMapping("/section/update/{id}")
    public Result<Void> updateSection(@PathVariable Long id, @RequestBody Section section) {
        section.setId(id);
        sectionService.save(section);
        return Result.ok();
    }

    @DeleteMapping("/section/delete/{id}")
    public Result<Void> deleteSection(@PathVariable Long id) {
        sectionService.delete(id);
        return Result.ok();
    }
}
