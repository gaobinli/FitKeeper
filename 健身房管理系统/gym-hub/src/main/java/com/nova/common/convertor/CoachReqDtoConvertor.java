package com.nova.common.convertor;

import com.nova.domain.dto.req.CoachReqDto;
import com.nova.domain.model.CoachEntity;
import com.nova.domain.po.CoachPo;
import com.baomidou.mybatisplus.ext.IConvertor;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public abstract class CoachReqDtoConvertor implements IConvertor<CoachReqDto,CoachEntity,CoachPo> {

}








