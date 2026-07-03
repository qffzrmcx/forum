package com.qffz.service;

import com.qffz.dto.PageResult;
import com.qffz.dto.PostView;
import com.qffz.exception.BusinessException;
import com.qffz.mapper.CollectMapper;
import com.qffz.mapper.LikeMapper;
import com.qffz.mapper.PostMapper;
import com.qffz.pojo.Post;
import com.qffz.utils.StringUtil;
import com.qffz.utils.UserContext;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    private final PostMapper postMapper;
    private final LikeMapper likeMapper;
    private final CollectMapper collectMapper;

    public PostService(PostMapper postMapper, LikeMapper likeMapper, CollectMapper collectMapper) {
        this.postMapper = postMapper;
        this.likeMapper = likeMapper;
        this.collectMapper = collectMapper;
    }

    public PageResult<PostView> list(Long sectionId, String keyword, String sort, int page, int size) {
        int offset = (page - 1) * size;
        return new PageResult<>(postMapper.count(sectionId, keyword), postMapper.list(sectionId, keyword, sort, offset, size));
    }

    public PostView detail(Long id) {
        postMapper.increaseView(id);
        PostView post = postMapper.detail(id);
        if (post == null || post.getStatus() == 0) throw new BusinessException("帖子不存在");
        Long userId = UserContext.userId();
        if (userId != null) {
            post.setLiked(likeMapper.exists(userId, id, "POST") > 0);
            post.setCollected(collectMapper.exists(userId, id) > 0);
        }
        return post;
    }

    public Long add(Post post) {
        validate(post);
        post.setUserId(UserContext.userId());
        postMapper.insert(post);
        return post.getId();
    }

    public void update(Long id, Post post) {
        PostView db = postMapper.detail(id);
        if (db == null) throw new BusinessException("帖子不存在");
        if (!UserContext.isAdmin() && !UserContext.userId().equals(db.getUserId())) {
            throw new BusinessException("只能编辑自己的帖子");
        }
        validate(post);
        post.setId(id);
        postMapper.update(post);
    }

    public void delete(Long id) {
        PostView db = postMapper.detail(id);
        if (db == null) throw new BusinessException("帖子不存在");
        if (!UserContext.isAdmin() && !UserContext.userId().equals(db.getUserId())) {
            throw new BusinessException("只能删除自己的帖子");
        }
        postMapper.softDelete(id);
    }

    public boolean toggleLike(Long id) {
        Long userId = UserContext.userId();
        if (likeMapper.exists(userId, id, "POST") > 0) {
            likeMapper.delete(userId, id, "POST");
            postMapper.changeLike(id, -1);
            return false;
        }
        likeMapper.insert(userId, id, "POST");
        postMapper.changeLike(id, 1);
        return true;
    }

    public boolean toggleCollect(Long id) {
        Long userId = UserContext.userId();
        if (collectMapper.exists(userId, id) > 0) {
            collectMapper.delete(userId, id);
            postMapper.changeCollect(id, -1);
            return false;
        }
        collectMapper.insert(userId, id);
        postMapper.changeCollect(id, 1);
        return true;
    }

    public PageResult<PostView> adminList(String keyword, int page, int size) {
        int offset = (page - 1) * size;
        return new PageResult<>(postMapper.adminCount(keyword), postMapper.adminList(keyword, offset, size));
    }

    public void updateTop(Long id, Integer value) { postMapper.updateTop(id, value); }
    public void updateEssence(Long id, Integer value) { postMapper.updateEssence(id, value); }

    private void validate(Post post) {
        if (post.getSectionId() == null || StringUtil.isBlank(post.getTitle()) || StringUtil.isBlank(post.getContent())) {
            throw new BusinessException("标题、板块和内容不能为空");
        }
    }
}
