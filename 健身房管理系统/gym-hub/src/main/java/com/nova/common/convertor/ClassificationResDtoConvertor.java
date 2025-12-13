package com.nova.common.convertor;

import com.nova.domain.dto.res.ClassificationResDto;
import com.nova.domain.model.ClassificationEntity;
import com.nova.domain.po.ClassificationPo;
import com.baomidou.mybatisplus.ext.IConvertor;
import com.baomidou.mybatisplus.ext.Pager;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public abstract class ClassificationResDtoConvertor implements IConvertor<ClassificationResDto,ClassificationEntity,ClassificationPo> {

    public  Pager<ClassificationResDto> convertPoPager2ResDtoPager(Pager<ClassificationPo> poPager) {
        if (poPager == null) {
            return null;
        }

        Pager<ClassificationResDto> resDtoPager = new Pager();
        resDtoPager.setTotalCount(poPager.getTotalCount());
        resDtoPager.setPageSize(poPager.getPageSize());
        resDtoPager.setTotalPage(poPager.getTotalPage());
        resDtoPager.setCurrentPage(poPager.getCurrentPage());
        resDtoPager.setList(poList2DtoList(poPager.getList()));

        return resDtoPager;
    }
}








