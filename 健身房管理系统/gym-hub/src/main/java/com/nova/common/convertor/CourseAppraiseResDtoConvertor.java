package com.nova.common.convertor;

import com.nova.domain.dto.res.CourseAppraiseResDto;
import com.nova.domain.model.CourseAppraiseEntity;
import com.nova.domain.po.CourseAppraisePo;
import com.baomidou.mybatisplus.ext.IConvertor;
import com.baomidou.mybatisplus.ext.Pager;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public abstract class CourseAppraiseResDtoConvertor implements IConvertor<CourseAppraiseResDto,CourseAppraiseEntity,CourseAppraisePo> {

    public  Pager<CourseAppraiseResDto> convertPoPager2ResDtoPager(Pager<CourseAppraisePo> poPager) {
        if (poPager == null) {
            return null;
        }

        Pager<CourseAppraiseResDto> resDtoPager = new Pager();
        resDtoPager.setTotalCount(poPager.getTotalCount());
        resDtoPager.setPageSize(poPager.getPageSize());
        resDtoPager.setTotalPage(poPager.getTotalPage());
        resDtoPager.setCurrentPage(poPager.getCurrentPage());
        resDtoPager.setList(poList2DtoList(poPager.getList()));

        return resDtoPager;
    }
}








