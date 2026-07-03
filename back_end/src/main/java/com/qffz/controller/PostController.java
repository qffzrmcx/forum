package com.qffz.controller;

import com.qffz.dto.PageResult;
import com.qffz.dto.PostView;
import com.qffz.dto.Result;
import com.qffz.pojo.Post;
import com.qffz.service.PostService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/list")
    public Result<PageResult<PostView>> list(@RequestParam(required = false) Long sectionId,
                                             @RequestParam(required = false) String keyword,
                                             @RequestParam(defaultValue = "new") String sort,
                                             @RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "10") int size) {
        return Result.ok(postService.list(sectionId, keyword, sort, page, size));
    }

    @GetMapping("/search")
    public Result<PageResult<PostView>> search(@RequestParam String keyword,
                                               @RequestParam(defaultValue = "1") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        return Result.ok(postService.list(null, keyword, "new", page, size));
    }

    @GetMapping("/detail/{id}")
    public Result<PostView> detail(@PathVariable Long id) {
        return Result.ok(postService.detail(id));
    }

    @PostMapping("/add")
    public Result<Long> add(@RequestBody Post post) {
        return Result.ok(postService.add(post));
    }

    @PutMapping("/update/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Post post) {
        postService.update(id, post);
        return Result.ok();
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        postService.delete(id);
        return Result.ok();
    }

    @PostMapping("/like/{id}")
    public Result<Boolean> like(@PathVariable Long id) {
        return Result.ok(postService.toggleLike(id));
    }

    @PostMapping("/collect/{id}")
    public Result<Boolean> collect(@PathVariable Long id) {
        return Result.ok(postService.toggleCollect(id));
    }
}
