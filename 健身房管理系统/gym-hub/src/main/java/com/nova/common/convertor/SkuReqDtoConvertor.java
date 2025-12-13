package com.nova.common.convertor;

import com.nova.domain.dto.req.SkuReqDto;
import com.nova.domain.model.SkuEntity;
import com.nova.domain.po.SkuPo;
import com.baomidou.mybatisplus.ext.IConvertor;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public abstract class SkuReqDtoConvertor implements IConvertor<SkuReqDto, SkuEntity,SkuPo> {

}








