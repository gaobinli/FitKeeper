package com.nova.common.convertor;

import com.nova.domain.dto.req.SkuAppraiseReqDto;
import com.nova.domain.model.SkuAppraiseEntity;
import com.nova.domain.po.SkuAppraisePo;
import com.baomidou.mybatisplus.ext.IConvertor;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public abstract class SkuAppraiseReqDtoConvertor implements IConvertor<SkuAppraiseReqDto, SkuAppraiseEntity, SkuAppraisePo> {

}








