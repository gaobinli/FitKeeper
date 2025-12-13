package com.nova.mapper;

import com.nova.domain.po.PostPo;
import com.baomidou.mybatisplus.ext.BaseExtMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostMapper extends BaseExtMapper<PostPo> {
	
}
