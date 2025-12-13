package com.nova.common.convertor;

import com.nova.domain.dto.req.PostCommentReqDto;
import com.nova.domain.model.PostCommentEntity;
import com.nova.domain.po.PostCommentPo;
import com.baomidou.mybatisplus.ext.IConvertor;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public abstract class PostCommentReqDtoConvertor implements IConvertor<PostCommentReqDto,PostCommentEntity,PostCommentPo> {

}








