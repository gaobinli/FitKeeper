package com.nova.common.convertor;

import com.nova.domain.dto.req.ClassificationReqDto;
import com.nova.domain.model.ClassificationEntity;
import com.nova.domain.po.ClassificationPo;
import com.baomidou.mybatisplus.ext.IConvertor;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public abstract class ClassificationReqDtoConvertor implements IConvertor<ClassificationReqDto,ClassificationEntity,ClassificationPo> {

}








