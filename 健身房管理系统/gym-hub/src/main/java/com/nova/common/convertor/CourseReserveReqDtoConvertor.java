package com.nova.common.convertor;

import com.nova.domain.dto.req.CourseReserveReqDto;
import com.nova.domain.model.CourseReserveEntity;
import com.nova.domain.po.CourseReservePo;
import com.baomidou.mybatisplus.ext.IConvertor;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public abstract class CourseReserveReqDtoConvertor implements IConvertor<CourseReserveReqDto,CourseReserveEntity,CourseReservePo> {

}








