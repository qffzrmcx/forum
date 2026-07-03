package com.qffz.dto;

import com.qffz.pojo.Post;

public class PostView extends Post {
    private String authorName;
    private String authorAvatar;
    private String sectionName;
    private Boolean liked;
    private Boolean collected;

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }
    public String getAuthorAvatar() { return authorAvatar; }
    public void setAuthorAvatar(String authorAvatar) { this.authorAvatar = authorAvatar; }
    public String getSectionName() { return sectionName; }
    public void setSectionName(String sectionName) { this.sectionName = sectionName; }
    public Boolean getLiked() { return liked; }
    public void setLiked(Boolean liked) { this.liked = liked; }
    public Boolean getCollected() { return collected; }
    public void setCollected(Boolean collected) { this.collected = collected; }
}
