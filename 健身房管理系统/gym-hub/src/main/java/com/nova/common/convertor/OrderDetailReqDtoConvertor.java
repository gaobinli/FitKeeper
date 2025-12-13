package com.nova.common.convertor;

import com.baomidou.mybatisplus.ext.IConvertor;
import com.nova.domain.dto.req.OrderDetailReqDto;
import com.nova.domain.model.OrderDetailEntity;
import com.nova.domain.po.OrderDetailPo;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public abstract class OrderDetailReqDtoConvertor implements IConvertor<OrderDetailReqDto, OrderDetailEntity, OrderDetailPo> {

}








