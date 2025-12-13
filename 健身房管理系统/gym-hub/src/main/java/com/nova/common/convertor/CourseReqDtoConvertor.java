package com.nova.common.convertor;

import com.nova.domain.dto.req.CourseReqDto;
import com.nova.domain.model.CourseEntity;
import com.nova.domain.po.CoursePo;
import com.baomidou.mybatisplus.ext.IConvertor;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public abstract class CourseReqDtoConvertor implements IConvertor<CourseReqDto,CourseEntity,CoursePo> {

}








