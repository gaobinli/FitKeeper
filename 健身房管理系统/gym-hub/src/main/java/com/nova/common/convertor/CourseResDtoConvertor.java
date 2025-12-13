package com.nova.common.convertor;

import com.nova.domain.dto.res.CourseResDto;
import com.nova.domain.model.CourseEntity;
import com.nova.domain.po.CoursePo;
import com.baomidou.mybatisplus.ext.IConvertor;
import com.baomidou.mybatisplus.ext.Pager;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public abstract class CourseResDtoConvertor implements IConvertor<CourseResDto,CourseEntity,CoursePo> {

    public  Pager<CourseResDto> convertPoPager2ResDtoPager(Pager<CoursePo> poPager) {
        if (poPager == null) {
            return null;
        }

        Pager<CourseResDto> resDtoPager = new Pager();
        resDtoPager.setTotalCount(poPager.getTotalCount());
        resDtoPager.setPageSize(poPager.getPageSize());
        resDtoPager.setTotalPage(poPager.getTotalPage());
        resDtoPager.setCurrentPage(poPager.getCurrentPage());
        resDtoPager.setList(poList2DtoList(poPager.getList()));

        return resDtoPager;
    }
}








