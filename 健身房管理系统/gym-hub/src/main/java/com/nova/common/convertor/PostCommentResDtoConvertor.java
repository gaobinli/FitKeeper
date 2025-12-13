package com.nova.common.convertor;

import com.nova.domain.dto.res.PostCommentResDto;
import com.nova.domain.model.PostCommentEntity;
import com.nova.domain.po.PostCommentPo;
import com.baomidou.mybatisplus.ext.IConvertor;
import com.baomidou.mybatisplus.ext.Pager;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public abstract class PostCommentResDtoConvertor implements IConvertor<PostCommentResDto,PostCommentEntity,PostCommentPo> {

    public  Pager<PostCommentResDto> convertPoPager2ResDtoPager(Pager<PostCommentPo> poPager) {
        if (poPager == null) {
            return null;
        }

        Pager<PostCommentResDto> resDtoPager = new Pager();
        resDtoPager.setTotalCount(poPager.getTotalCount());
        resDtoPager.setPageSize(poPager.getPageSize());
        resDtoPager.setTotalPage(poPager.getTotalPage());
        resDtoPager.setCurrentPage(poPager.getCurrentPage());
        resDtoPager.setList(poList2DtoList(poPager.getList()));

        return resDtoPager;
    }
}








