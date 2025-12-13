package com.nova.common.convertor;

import com.nova.domain.dto.res.PostResDto;
import com.nova.domain.model.PostEntity;
import com.nova.domain.po.PostPo;
import com.baomidou.mybatisplus.ext.IConvertor;
import com.baomidou.mybatisplus.ext.Pager;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public abstract class PostResDtoConvertor implements IConvertor<PostResDto,PostEntity,PostPo> {

    public  Pager<PostResDto> convertPoPager2ResDtoPager(Pager<PostPo> poPager) {
        if (poPager == null) {
            return null;
        }

        Pager<PostResDto> resDtoPager = new Pager();
        resDtoPager.setTotalCount(poPager.getTotalCount());
        resDtoPager.setPageSize(poPager.getPageSize());
        resDtoPager.setTotalPage(poPager.getTotalPage());
        resDtoPager.setCurrentPage(poPager.getCurrentPage());
        resDtoPager.setList(poList2DtoList(poPager.getList()));

        return resDtoPager;
    }
}








