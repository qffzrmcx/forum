package com.qffz.service;

import com.qffz.dto.CommentView;
import com.qffz.dto.PageResult;
import com.qffz.exception.BusinessException;
import com.qffz.mapper.CommentMapper;
import com.qffz.mapper.LikeMapper;
import com.qffz.mapper.PostMapper;
import com.qffz.pojo.Comment;
import com.qffz.utils.StringUtil;
import com.qffz.utils.UserContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final CommentMapper commentMapper;
    private final PostMapper postMapper;
    private final LikeMapper likeMapper;

    public CommentService(CommentMapper commentMapper, PostMapper postMapper, LikeMapper likeMapper) {
        this.commentMapper = commentMapper;
        this.postMapper = postMapper;
        this.likeMapper = likeMapper;
    }

    public List<CommentView> list(Long postId) {
        List<CommentView> comments = commentMapper.listByPost(postId);
        Long userId = UserContext.userId();
        if (userId != null) {
            comments.forEach(comment -> comment.setLiked(likeMapper.exists(userId, comment.getId(), "COMMENT") > 0));
        }
        return comments;
    }

    public void add(Comment comment) {
        if (comment.getPostId() == null || StringUtil.isBlank(comment.getContent())) {
            throw new BusinessException("评论内容不能为空");
        }
        if (comment.getContent().length() > 500) {
            throw new BusinessException("评论最多500字");
        }
        comment.setUserId(UserContext.userId());
        commentMapper.insert(comment);
        postMapper.changeComment(comment.getPostId(), 1);
    }

    public void delete(Long id) {
        Comment comment = commentMapper.findById(id);
        if (comment == null) throw new BusinessException("评论不存在");
        if (!UserContext.isAdmin() && !UserContext.userId().equals(comment.getUserId())) {
            throw new BusinessException("只能删除自己的评论");
        }
        commentMapper.softDelete(id);
        postMapper.changeComment(comment.getPostId(), -1);
    }

    public boolean toggleLike(Long id) {
        Long userId = UserContext.userId();
        if (likeMapper.exists(userId, id, "COMMENT") > 0) {
            likeMapper.delete(userId, id, "COMMENT");
            commentMapper.changeLike(id, -1);
            return false;
        }
        likeMapper.insert(userId, id, "COMMENT");
        commentMapper.changeLike(id, 1);
        return true;
    }

    public PageResult<CommentView> adminList(String keyword, int page, int size) {
        int offset = (page - 1) * size;
        return new PageResult<>(commentMapper.adminCount(keyword), commentMapper.adminList(keyword, offset, size));
    }
}
