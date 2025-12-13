package com.nova.common.convertor;

import com.baomidou.mybatisplus.ext.IConvertor;
import com.baomidou.mybatisplus.ext.Pager;
import com.nova.domain.dto.res.OrderDetailResDto;
import com.nova.domain.model.OrderDetailEntity;
import com.nova.domain.po.OrderDetailPo;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public abstract class OrderDetailResDtoConvertor implements IConvertor<OrderDetailResDto, OrderDetailEntity, OrderDetailPo> {

    public  Pager<OrderDetailResDto> convertPoPager2ResDtoPager(Pager<OrderDetailPo> poPager) {
        if (poPager == null) {
            return null;
        }

        Pager<OrderDetailResDto> resDtoPager = new Pager();
        resDtoPager.setTotalCount(poPager.getTotalCount());
        resDtoPager.setPageSize(poPager.getPageSize());
        resDtoPager.setTotalPage(poPager.getTotalPage());
        resDtoPager.setCurrentPage(poPager.getCurrentPage());
        resDtoPager.setList(poList2DtoList(poPager.getList()));

        return resDtoPager;
    }
}








