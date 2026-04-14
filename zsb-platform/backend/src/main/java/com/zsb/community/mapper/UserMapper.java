package com.zsb.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsb.community.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 用户 Mapper
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT COUNT(*) FROM t_user WHERE DATE_FORMAT(last_login_time, '%Y-%m') = DATE_FORMAT(NOW(), '%Y-%m') AND deleted = 0")
    Long countMonthlyActive();

    @Select("SELECT COUNT(*) FROM t_user WHERE DATE_FORMAT(create_time, '%Y-%m') = #{yearMonth} AND deleted = 0")
    Long countByYearMonth(String yearMonth);
}
