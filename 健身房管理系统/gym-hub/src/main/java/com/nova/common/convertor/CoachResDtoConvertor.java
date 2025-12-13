package com.nova.common.convertor;

import com.nova.domain.dto.res.CoachResDto;
import com.nova.domain.model.CoachEntity;
import com.nova.domain.po.CoachPo;
import com.baomidou.mybatisplus.ext.IConvertor;
import com.baomidou.mybatisplus.ext.Pager;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public abstract class CoachResDtoConvertor implements IConvertor<CoachResDto,CoachEntity,CoachPo> {

    public  Pager<CoachResDto> convertPoPager2ResDtoPager(Pager<CoachPo> poPager) {
        if (poPager == null) {
            return null;
        }

        Pager<CoachResDto> resDtoPager = new Pager();
        resDtoPager.setTotalCount(poPager.getTotalCount());
        resDtoPager.setPageSize(poPager.getPageSize());
        resDtoPager.setTotalPage(poPager.getTotalPage());
        resDtoPager.setCurrentPage(poPager.getCurrentPage());
        resDtoPager.setList(poList2DtoList(poPager.getList()));

        return resDtoPager;
    }
}








