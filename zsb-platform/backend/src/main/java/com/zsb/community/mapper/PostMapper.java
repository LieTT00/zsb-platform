package com.zsb.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsb.community.entity.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Map;

/**
 * 帖子 Mapper
 */
@Mapper
public interface PostMapper extends BaseMapper<Post> {

    @Select("SELECT target_school, COUNT(*) AS cnt FROM t_post WHERE deleted = 0 AND target_school IS NOT NULL AND target_school != '' GROUP BY target_school ORDER BY cnt DESC LIMIT 10")
    List<Map<String, Object>> countBySchool();

    @Select("SELECT category, COUNT(*) AS cnt FROM t_post WHERE deleted = 0 GROUP BY category")
    List<Map<String, Object>> countByCategory();
}
