package com.nova.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.ext.BasePService;
import com.baomidou.mybatisplus.ext.IPService;
import com.baomidou.mybatisplus.ext.Pager;
import com.baomidou.mybatisplus.ext.QueryParamUtils;
import com.nova.common.convertor.OrderDetailReqDtoConvertor;
import com.nova.common.convertor.OrderDetailResDtoConvertor;
import com.nova.common.exception.AppException;
import com.nova.common.exception.ErrorCode;
import com.nova.common.util.MapUtil;
import com.nova.domain.dto.req.OrderDetailReqDto;
import com.nova.domain.dto.res.OrderDetailResDto;
import com.nova.domain.po.OrderDetailPo;
import com.nova.domain.po.OrderPo;
import com.nova.domain.po.SkuAppraisePo;
import com.nova.domain.po.SkuPo;
import com.nova.mapper.OrderDetailMapper;
import com.nova.mapper.OrderMapper;
import com.nova.mapper.SkuAppraiseMapper;
import com.nova.mapper.SkuMapper;
import com.nova.service.OrderDetailService;
import com.google.common.base.CaseFormat;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Slf4j
@Service("orderDetailAppService")
@Transactional(rollbackFor = Exception.class)
public class OrderDetailServiceImpl implements OrderDetailService {

    @Autowired
    private SkuMapper skuMapper;
    @Resource
    private SkuAppraiseMapper skuAppraiseMapper;

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailReqDtoConvertor orderDetailReqDtoConvertor;

    @Autowired
    private OrderDetailResDtoConvertor orderDetailResDtoConvertor;

    private IPService<OrderDetailPo> orderDetailPoService;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    public OrderDetailServiceImpl(OrderDetailMapper orderDetailMapper) {
        this.orderDetailPoService = new BasePService(orderDetailMapper);
    }


    @Override
    public OrderDetailResDto save(OrderDetailReqDto reqDto) {
        OrderDetailPo po = orderDetailReqDtoConvertor.dto2Po(reqDto);
        orderDetailPoService.save(po);
        OrderDetailResDto resDto = orderDetailResDtoConvertor.po2Dto(po);
        return resDto;
    }

    @Override
    public boolean deleteByIds(String ids) {
        try {
        List<Long> idLongList =
        Arrays.asList(ids.split(",")).stream().map(s -> NumberUtils.createLong(s.trim())).collect(Collectors.toList());
        return orderDetailPoService.removeByIds(idLongList);
        } catch (Exception e) {
        throw new AppException("参数错误：" + ids, ErrorCode.sys("417"), e);
        }
     }

    @Override
    public Pager<OrderDetailResDto> doPager(Map<String, Object> params) {
        Pager<OrderDetailPo> poPager = orderDetailPoService.queryPage(params, OrderDetailPo.class);
        Pager<OrderDetailResDto> resDtoPager = orderDetailResDtoConvertor.convertPoPager2ResDtoPager(poPager);
        return resDtoPager;
    }

    @Override
    public List<OrderDetailResDto> getList(String orderNo, Integer userId) {
        OrderPo orderPo = orderMapper.selectOne(Wrappers.<OrderPo>lambdaQuery().eq(OrderPo::getOrderNo, orderNo));
        List<OrderDetailPo> list = orderDetailPoService.getList(new MapUtil().put("qp-orderNo-eq", orderNo), OrderDetailPo.class);
        List<OrderDetailResDto> result = orderDetailResDtoConvertor.poList2DtoList(list);
        for (OrderDetailResDto orderDetailResDto : result) {
            SkuPo SkuPo = skuMapper.selectById(orderDetailResDto.getSkuId());
            orderDetailResDto.setSkuName(SkuPo.getName());
            orderDetailResDto.setSkuImg(SkuPo.getUrl());
            if (userId == null) {
                orderDetailResDto.setAppraiseFlag(Boolean.FALSE);
                continue;
            }
            List<SkuAppraisePo> skuAppraisePos = skuAppraiseMapper.selectList(Wrappers.<SkuAppraisePo>lambdaQuery()
                    .eq(SkuAppraisePo::getOrderNo, orderNo)
                    .eq(SkuAppraisePo::getUserId, userId)
                    .eq(SkuAppraisePo::getSkuId, orderDetailResDto.getSkuId()));
            if (!CollectionUtils.isEmpty(skuAppraisePos)) {
                orderDetailResDto.setAppraiseFlag(Boolean.TRUE);
            }
            if (null != orderPo) {
                orderDetailResDto.setStatus(orderPo.getStatus());
            }
        }
        return result;
    }

    @Override
    public OrderDetailResDto selectOne(Long id) {
        OrderDetailPo po = orderDetailPoService.getById(id);
        OrderDetailResDto resDto = orderDetailResDtoConvertor.po2Dto(po);
        return resDto;
    }

    @Override
    public OrderDetailResDto selectOne(Map<String, Object> params) {
        QueryWrapper queryWrapper = QueryParamUtils.queryWrapper4eq(OrderDetailPo::new, params);
        OrderDetailPo po = orderDetailPoService.getOne(queryWrapper);
        OrderDetailResDto resDto = orderDetailResDtoConvertor.po2Dto(po);
        return resDto;
    }

    @Override
    public boolean updateProps(Long id, Map<String, Object> params) {
        UpdateWrapper<OrderDetailPo> updateWrapper = QueryParamUtils.updateWrapper4Map(OrderDetailPo::new, id, params);
        return orderDetailPoService.update(new OrderDetailPo(), updateWrapper);
        }

    @Override
    public boolean updateProps(Long id, OrderDetailReqDto reqDto) {
        OrderDetailPo po = orderDetailReqDtoConvertor.dto2Po(reqDto);
        po.setId(id);
        return orderDetailPoService.updateById(po);
    }


    @Override
    public boolean updateAllProps(Long id, OrderDetailReqDto reqDto) {
        UpdateWrapper<OrderDetailPo> updateWrapper = Wrappers.update();
        updateWrapper.eq("id", id);
        Field[] fields = OrderDetailReqDto.class.getDeclaredFields();
        Arrays.stream(fields).filter(f -> !Modifier.isStatic(f.getModifiers())).forEach(field -> {
        try {
        PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), OrderDetailReqDto.class);
        Method getMethod = propertyDescriptor.getReadMethod();
        String fileNameCamel = getMethod.getName().substring(3);
        String fileNameUnderline = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fileNameCamel);
        updateWrapper.set(fileNameUnderline, getMethod.invoke(reqDto));
        } catch (Exception ex) {
        log.warn("属性不存在get方法："+field.getName(),ex);
        }
        });
        return orderDetailPoService.update(new OrderDetailPo(), updateWrapper);
    }

}