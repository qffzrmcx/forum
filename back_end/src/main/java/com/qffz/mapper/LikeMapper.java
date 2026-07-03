package com.qffz.mapper;

import org.apache.ibatis.annotations.*;

@Mapper
public interface LikeMapper {
    @Select("select count(*) from like_record where user_id=#{userId} and target_id=#{targetId} and target_type=#{targetType}")
    int exists(@Param("userId") Long userId, @Param("targetId") Long targetId, @Param("targetType") String targetType);

    @Insert("insert into like_record(user_id,target_id,target_type,create_time) values(#{userId},#{targetId},#{targetType},now())")
    int insert(@Param("userId") Long userId, @Param("targetId") Long targetId, @Param("targetType") String targetType);

    @Delete("delete from like_record where user_id=#{userId} and target_id=#{targetId} and target_type=#{targetType}")
    int delete(@Param("userId") Long userId, @Param("targetId") Long targetId, @Param("targetType") String targetType);
}
