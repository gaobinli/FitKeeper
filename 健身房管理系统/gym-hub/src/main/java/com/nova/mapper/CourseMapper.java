package com.nova.mapper;

import com.nova.domain.po.CoursePo;
import com.baomidou.mybatisplus.ext.BaseExtMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CourseMapper extends BaseExtMapper<CoursePo> {
	
}
