package com.nova.common.convertor;

import com.nova.domain.dto.req.CategoryReqDto;
import com.nova.domain.model.CategoryEntity;
import com.nova.domain.po.CategoryPo;
import com.baomidou.mybatisplus.ext.IConvertor;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public abstract class CategoryReqDtoConvertor implements IConvertor<CategoryReqDto, CategoryEntity, CategoryPo> {

}








