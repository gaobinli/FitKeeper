package com.nova.common.convertor;

import com.nova.domain.dto.res.CategoryResDto;
import com.nova.domain.model.CategoryEntity;
import com.nova.domain.po.CategoryPo;
import com.baomidou.mybatisplus.ext.IConvertor;
import com.baomidou.mybatisplus.ext.Pager;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public abstract class CategoryResDtoConvertor implements IConvertor<CategoryResDto, CategoryEntity, CategoryPo> {

    public  Pager<CategoryResDto> convertPoPager2ResDtoPager(Pager<CategoryPo> poPager) {
        if (poPager == null) {
            return null;
        }

        Pager<CategoryResDto> resDtoPager = new Pager();
        resDtoPager.setTotalCount(poPager.getTotalCount());
        resDtoPager.setPageSize(poPager.getPageSize());
        resDtoPager.setTotalPage(poPager.getTotalPage());
        resDtoPager.setCurrentPage(poPager.getCurrentPage());
        resDtoPager.setList(poList2DtoList(poPager.getList()));

        return resDtoPager;
    }
}








