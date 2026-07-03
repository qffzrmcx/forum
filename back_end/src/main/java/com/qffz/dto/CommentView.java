package com.qffz.dto;

import com.qffz.pojo.Comment;

public class CommentView extends Comment {
    private String authorName;
    private String authorAvatar;
    private Boolean liked;

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }
    public String getAuthorAvatar() { return authorAvatar; }
    public void setAuthorAvatar(String authorAvatar) { this.authorAvatar = authorAvatar; }
    public Boolean getLiked() { return liked; }
    public void setLiked(Boolean liked) { this.liked = liked; }
}
