package com.qffz.mapper;

import com.qffz.dto.PostView;
import com.qffz.pojo.Post;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PostMapper {
    String BASE_SELECT = "select p.*, u.nickname author_name, u.avatar author_avatar, s.name section_name from post p left join `user` u on p.user_id=u.id left join section s on p.section_id=s.id ";

    @Select("<script>" + BASE_SELECT + " where p.status=1 <if test='sectionId!=null'>and p.section_id=#{sectionId}</if><if test='keyword!=null and keyword!=\"\"'>and (p.title like concat('%',#{keyword},'%') or p.content like concat('%',#{keyword},'%') or u.nickname like concat('%',#{keyword},'%') or s.name like concat('%',#{keyword},'%'))</if><choose><when test='sort==\"hot\"'>order by p.is_top desc, (p.view_count+p.like_count*3+p.comment_count*5) desc, p.id desc</when><otherwise>order by p.is_top desc, p.create_time desc</otherwise></choose> limit #{offset},#{size}</script>")
    List<PostView> list(@Param("sectionId") Long sectionId, @Param("keyword") String keyword, @Param("sort") String sort, @Param("offset") int offset, @Param("size") int size);

    @Select("<script>select count(*) from post p left join `user` u on p.user_id=u.id left join section s on p.section_id=s.id where p.status=1 <if test='sectionId!=null'>and p.section_id=#{sectionId}</if><if test='keyword!=null and keyword!=\"\"'>and (p.title like concat('%',#{keyword},'%') or p.content like concat('%',#{keyword},'%') or u.nickname like concat('%',#{keyword},'%') or s.name like concat('%',#{keyword},'%'))</if></script>")
    long count(@Param("sectionId") Long sectionId, @Param("keyword") String keyword);

    @Select(BASE_SELECT + " where p.id=#{id}")
    PostView detail(@Param("id") Long id);

    @Insert("insert into post(user_id,section_id,title,content,images,view_count,like_count,comment_count,collect_count,is_top,is_essence,status,create_time,update_time) values(#{userId},#{sectionId},#{title},#{content},#{images},0,0,0,0,0,0,1,now(),now())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Post post);

    @Update("update post set section_id=#{sectionId}, title=#{title}, content=#{content}, images=#{images}, update_time=now() where id=#{id}")
    int update(Post post);

    @Update("update post set status=0, update_time=now() where id=#{id}")
    int softDelete(@Param("id") Long id);

    @Update("update post set view_count=view_count+1 where id=#{id}")
    int increaseView(@Param("id") Long id);

    @Update("update post set like_count=greatest(like_count+#{delta},0) where id=#{id}")
    int changeLike(@Param("id") Long id, @Param("delta") int delta);

    @Update("update post set collect_count=greatest(collect_count+#{delta},0) where id=#{id}")
    int changeCollect(@Param("id") Long id, @Param("delta") int delta);

    @Update("update post set comment_count=greatest(comment_count+#{delta},0) where id=#{id}")
    int changeComment(@Param("id") Long id, @Param("delta") int delta);

    @Update("update post set is_top=#{value}, update_time=now() where id=#{id}")
    int updateTop(@Param("id") Long id, @Param("value") Integer value);

    @Update("update post set is_essence=#{value}, update_time=now() where id=#{id}")
    int updateEssence(@Param("id") Long id, @Param("value") Integer value);

    @Select("<script>" + BASE_SELECT + " where 1=1 <if test='keyword!=null and keyword!=\"\"'>and (p.title like concat('%',#{keyword},'%') or p.content like concat('%',#{keyword},'%') or u.nickname like concat('%',#{keyword},'%'))</if> order by p.id desc limit #{offset},#{size}</script>")
    List<PostView> adminList(@Param("keyword") String keyword, @Param("offset") int offset, @Param("size") int size);

    @Select("<script>select count(*) from post p left join `user` u on p.user_id=u.id where 1=1 <if test='keyword!=null and keyword!=\"\"'>and (p.title like concat('%',#{keyword},'%') or p.content like concat('%',#{keyword},'%') or u.nickname like concat('%',#{keyword},'%'))</if></script>")
    long adminCount(@Param("keyword") String keyword);

    @Select(BASE_SELECT + " where p.status=1 and p.user_id=#{userId} order by p.create_time desc limit #{offset},#{size}")
    List<PostView> listByUser(@Param("userId") Long userId, @Param("offset") int offset, @Param("size") int size);

    @Select("select count(*) from post where status=1 and user_id=#{userId}")
    long countByUser(@Param("userId") Long userId);
}
