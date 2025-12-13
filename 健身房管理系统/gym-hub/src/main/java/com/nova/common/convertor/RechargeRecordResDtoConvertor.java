package com.nova.common.convertor;

import com.nova.domain.dto.res.RechargeRecordResDto;
import com.nova.domain.model.RechargeRecordEntity;
import com.nova.domain.po.RechargeRecordPo;
import com.baomidou.mybatisplus.ext.IConvertor;
import com.baomidou.mybatisplus.ext.Pager;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public abstract class RechargeRecordResDtoConvertor implements IConvertor<RechargeRecordResDto,RechargeRecordEntity,RechargeRecordPo> {

    public  Pager<RechargeRecordResDto> convertPoPager2ResDtoPager(Pager<RechargeRecordPo> poPager) {
        if (poPager == null) {
            return null;
        }

        Pager<RechargeRecordResDto> resDtoPager = new Pager();
        resDtoPager.setTotalCount(poPager.getTotalCount());
        resDtoPager.setPageSize(poPager.getPageSize());
        resDtoPager.setTotalPage(poPager.getTotalPage());
        resDtoPager.setCurrentPage(poPager.getCurrentPage());
        resDtoPager.setList(poList2DtoList(poPager.getList()));

        return resDtoPager;
    }
}








