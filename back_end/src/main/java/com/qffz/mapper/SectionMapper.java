package com.qffz.mapper;

import com.qffz.pojo.Section;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SectionMapper {
    @Select("select * from section where status=1 order by sort asc, id asc")
    List<Section> enabledList();

    @Select("select * from section order by sort asc, id asc")
    List<Section> allList();

    @Select("select * from section where id=#{id}")
    Section findById(@Param("id") Long id);

    @Insert("insert into section(name,description,sort,status,create_time) values(#{name},#{description},#{sort},#{status},now())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Section section);

    @Update("update section set name=#{name}, description=#{description}, sort=#{sort}, status=#{status} where id=#{id}")
    int update(Section section);

    @Delete("delete from section where id=#{id}")
    int delete(@Param("id") Long id);
}
