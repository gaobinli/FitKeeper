package com.nova.mapper;

import com.baomidou.mybatisplus.ext.BaseExtMapper;
import com.nova.domain.po.OrderDetailPo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDetailMapper extends BaseExtMapper<OrderDetailPo> {
	
}
