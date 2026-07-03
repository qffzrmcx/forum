package com.qffz.controller;

import com.qffz.dto.CommentView;
import com.qffz.dto.Result;
import com.qffz.pojo.Comment;
import com.qffz.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/list")
    public Result<List<CommentView>> list(@RequestParam Long postId) {
        return Result.ok(commentService.list(postId));
    }

    @PostMapping("/add")
    public Result<Void> add(@RequestBody Comment comment) {
        commentService.add(comment);
        return Result.ok();
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        commentService.delete(id);
        return Result.ok();
    }

    @PostMapping("/like/{id}")
    public Result<Boolean> like(@PathVariable Long id) {
        return Result.ok(commentService.toggleLike(id));
    }
}
