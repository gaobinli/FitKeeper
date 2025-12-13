package com.nova.common.convertor;

import com.nova.domain.dto.req.RechargeRecordReqDto;
import com.nova.domain.model.RechargeRecordEntity;
import com.nova.domain.po.RechargeRecordPo;
import com.baomidou.mybatisplus.ext.IConvertor;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public abstract class RechargeRecordReqDtoConvertor implements IConvertor<RechargeRecordReqDto,RechargeRecordEntity,RechargeRecordPo> {

}








