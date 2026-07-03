package com.qffz.mapper;

import com.qffz.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select * from `user` where id=#{id}")
    User findById(@Param("id") Long id);

    @Select("select * from `user` where username=#{username}")
    User findByUsername(@Param("username") String username);

    @Insert("insert into `user`(username,password,nickname,avatar,email,role,status,bio,create_time,update_time) values(#{username},#{password},#{nickname},#{avatar},#{email},#{role},#{status},#{bio},now(),now())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Update("<script>update `user` <set><if test='nickname!=null'>nickname=#{nickname},</if><if test='avatar!=null'>avatar=#{avatar},</if><if test='email!=null'>email=#{email},</if><if test='bio!=null'>bio=#{bio},</if><if test='password!=null'>password=#{password},</if>update_time=now()</set> where id=#{id}</script>")
    int update(User user);

    @Select("<script>select * from `user` where 1=1 <if test='keyword!=null and keyword!=\"\"'>and (username like concat('%',#{keyword},'%') or nickname like concat('%',#{keyword},'%') or email like concat('%',#{keyword},'%'))</if> order by id desc limit #{offset},#{size}</script>")
    List<User> list(@Param("keyword") String keyword, @Param("offset") int offset, @Param("size") int size);

    @Select("<script>select count(*) from `user` where 1=1 <if test='keyword!=null and keyword!=\"\"'>and (username like concat('%',#{keyword},'%') or nickname like concat('%',#{keyword},'%') or email like concat('%',#{keyword},'%'))</if></script>")
    long count(@Param("keyword") String keyword);

    @Update("update `user` set status=#{status}, update_time=now() where id=#{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    @Delete("delete from `user` where id=#{id}")
    int delete(@Param("id") Long id);
}
