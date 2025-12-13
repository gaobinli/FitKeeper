package com.nova.common.convertor;

import com.nova.domain.dto.req.PostReqDto;
import com.nova.domain.model.PostEntity;
import com.nova.domain.po.PostPo;
import com.baomidou.mybatisplus.ext.IConvertor;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public abstract class PostReqDtoConvertor implements IConvertor<PostReqDto,PostEntity,PostPo> {

}








