package com.nova.common.convertor;

import com.nova.domain.dto.req.CourseAppraiseReqDto;
import com.nova.domain.model.CourseAppraiseEntity;
import com.nova.domain.po.CourseAppraisePo;
import com.baomidou.mybatisplus.ext.IConvertor;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public abstract class CourseAppraiseReqDtoConvertor implements IConvertor<CourseAppraiseReqDto,CourseAppraiseEntity,CourseAppraisePo> {

}








