package com.nova.common.convertor;

import com.nova.domain.dto.res.CourseReserveResDto;
import com.nova.domain.model.CourseReserveEntity;
import com.nova.domain.po.CourseReservePo;
import com.baomidou.mybatisplus.ext.IConvertor;
import com.baomidou.mybatisplus.ext.Pager;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public abstract class CourseReserveResDtoConvertor implements IConvertor<CourseReserveResDto,CourseReserveEntity,CourseReservePo> {

    public  Pager<CourseReserveResDto> convertPoPager2ResDtoPager(Pager<CourseReservePo> poPager) {
        if (poPager == null) {
            return null;
        }

        Pager<CourseReserveResDto> resDtoPager = new Pager();
        resDtoPager.setTotalCount(poPager.getTotalCount());
        resDtoPager.setPageSize(poPager.getPageSize());
        resDtoPager.setTotalPage(poPager.getTotalPage());
        resDtoPager.setCurrentPage(poPager.getCurrentPage());
        resDtoPager.setList(poList2DtoList(poPager.getList()));

        return resDtoPager;
    }
}








