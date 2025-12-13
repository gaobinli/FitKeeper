package com.nova.common.convertor;

import com.nova.domain.dto.req.AnnouncementReqDto;
import com.nova.domain.model.AnnouncementEntity;
import com.nova.domain.po.AnnouncementPo;
import com.baomidou.mybatisplus.ext.IConvertor;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public abstract class AnnouncementReqDtoConvertor implements IConvertor<AnnouncementReqDto, AnnouncementEntity, AnnouncementPo> {

}








