package com.qffz.mapper;

import com.qffz.dto.PostView;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CollectMapper {
    @Select("select count(*) from collect where user_id=#{userId} and post_id=#{postId}")
    int exists(@Param("userId") Long userId, @Param("postId") Long postId);

    @Insert("insert into collect(user_id,post_id,create_time) values(#{userId},#{postId},now())")
    int insert(@Param("userId") Long userId, @Param("postId") Long postId);

    @Delete("delete from collect where user_id=#{userId} and post_id=#{postId}")
    int delete(@Param("userId") Long userId, @Param("postId") Long postId);

    @Select("select p.*, u.nickname author_name, u.avatar author_avatar, s.name section_name from collect c left join post p on c.post_id=p.id left join `user` u on p.user_id=u.id left join section s on p.section_id=s.id where c.user_id=#{userId} and p.status=1 order by c.id desc")
    List<PostView> listByUser(@Param("userId") Long userId);
}
