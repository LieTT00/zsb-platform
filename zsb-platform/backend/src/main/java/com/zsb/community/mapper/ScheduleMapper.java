package com.zsb.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsb.community.entity.Schedule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ScheduleMapper extends BaseMapper<Schedule> {

    @Select("SELECT * FROM t_schedule WHERE user_id = #{userId} AND date >= #{startDate} AND date <= #{endDate} ORDER BY date, time_slot")
    List<Schedule> selectByUserIdAndDateRange(@Param("userId") Long userId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
