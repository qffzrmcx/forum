package com.qffz.mapper;

import com.qffz.dto.CommentView;
import com.qffz.pojo.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {
    @Select("select c.*, u.nickname author_name, u.avatar author_avatar from `comment` c left join `user` u on c.user_id=u.id where c.post_id=#{postId} and c.status=1 order by c.create_time asc")
    List<CommentView> listByPost(@Param("postId") Long postId);

    @Select("select * from `comment` where id=#{id}")
    Comment findById(@Param("id") Long id);

    @Insert("insert into `comment`(post_id,user_id,parent_id,content,like_count,status,create_time) values(#{postId},#{userId},#{parentId},#{content},0,1,now())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Comment comment);

    @Update("update `comment` set status=0 where id=#{id}")
    int softDelete(@Param("id") Long id);

    @Update("update `comment` set like_count=greatest(like_count+#{delta},0) where id=#{id}")
    int changeLike(@Param("id") Long id, @Param("delta") int delta);

    @Select("<script>select c.*, u.nickname author_name, u.avatar author_avatar from `comment` c left join `user` u on c.user_id=u.id where 1=1 <if test='keyword!=null and keyword!=\"\"'>and (c.content like concat('%',#{keyword},'%') or u.nickname like concat('%',#{keyword},'%'))</if> order by c.id desc limit #{offset},#{size}</script>")
    List<CommentView> adminList(@Param("keyword") String keyword, @Param("offset") int offset, @Param("size") int size);

    @Select("<script>select count(*) from `comment` c left join `user` u on c.user_id=u.id where 1=1 <if test='keyword!=null and keyword!=\"\"'>and (c.content like concat('%',#{keyword},'%') or u.nickname like concat('%',#{keyword},'%'))</if></script>")
    long adminCount(@Param("keyword") String keyword);
}
