package com.nova.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.ext.BasePService;
import com.baomidou.mybatisplus.ext.IPService;
import com.baomidou.mybatisplus.ext.Pager;
import com.baomidou.mybatisplus.ext.QueryParamUtils;
import com.nova.common.convertor.OrderResDtoConvertor;
import com.nova.common.exception.AppException;
import com.nova.domain.dto.req.OrderReqDto;
import com.nova.domain.po.OrderDetailPo;
import com.nova.domain.po.OrderPo;
import com.nova.mapper.UserMapper;
import com.nova.common.convertor.OrderReqDtoConvertor;
import com.nova.common.exception.ErrorCode;
import com.nova.common.util.IdGenerateUtil;
import com.nova.domain.dto.req.OrderDetailReqDto;
import com.nova.domain.dto.res.OrderResDto;
import com.nova.domain.po.SkuPo;
import com.nova.domain.po.UserPo;
import com.nova.mapper.OrderDetailMapper;
import com.nova.mapper.OrderMapper;
import com.nova.mapper.SkuMapper;
import com.nova.service.OrderService;
import com.google.common.base.CaseFormat;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Slf4j
@Service("orderAppService")
@Transactional(rollbackFor = Exception.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private OrderReqDtoConvertor orderReqDtoConvertor;

    @Autowired
    private OrderResDtoConvertor orderResDtoConvertor;

    @Autowired
    private UserMapper userMapper;

    private IPService<OrderPo> orderPoService;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    public OrderServiceImpl(OrderMapper orderMapper) {
        this.orderPoService = new BasePService(orderMapper);
    }


    @Override
    public OrderResDto save(OrderReqDto reqDto) {
        String orderNo = IdGenerateUtil.prefix("C");
        OrderPo orderPo = new OrderPo();
        orderPo.setUserId(reqDto.getUserId());
        orderPo.setOrderNo(orderNo);
        orderPo.setStatus(1);
        orderPoService.save(orderPo);
        SkuPo skuPo;
        for (OrderDetailReqDto orderDetail : reqDto.getOrderDetails()) {
            skuPo = skuMapper.selectById(orderDetail.getSkuId());
            skuPo.setInventory(skuPo.getInventory() - orderDetail.getNumber());
            skuMapper.updateById(skuPo);
            OrderDetailPo orderDetailPo = new OrderDetailPo();
            orderDetailPo.setOrderNo(orderNo);
            orderDetailPo.setSkuId(orderDetail.getSkuId());
            orderDetailPo.setNumber(orderDetail.getNumber());
            orderDetailPo.setPrice(orderDetail.getPrice());
            orderDetailMapper.insert(orderDetailPo);
        }

        return null;
    }

    @Override
    public boolean deleteByIds(String ids) {
        try {
        List<Long> idLongList =
        Arrays.asList(ids.split(",")).stream().map(s -> NumberUtils.createLong(s.trim())).collect(Collectors.toList());
        return orderPoService.removeByIds(idLongList);
        } catch (Exception e) {
        throw new AppException("参数错误：" + ids, ErrorCode.sys("417"), e);
        }
     }

    @Override
    public Pager<OrderResDto> doPager(Map<String, Object> params) {
        Pager<OrderPo> poPager = orderPoService.queryPage(params, OrderPo.class);
        Pager<OrderResDto> resDtoPager = orderResDtoConvertor.convertPoPager2ResDtoPager(poPager);
        for (OrderResDto orderResDto : resDtoPager.getList()) {
            List<OrderDetailPo> orderDetailPos = orderDetailMapper.selectList(Wrappers.<OrderDetailPo>
                    lambdaQuery().eq(OrderDetailPo::getOrderNo, orderResDto.getOrderNo()));
            BigDecimal orderPrice = orderDetailPos.stream()
                    .map(detail -> detail.getPrice().multiply(new BigDecimal(detail.getNumber())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            orderResDto.setOrderPrice(orderPrice);
            if (StringUtils.isNotBlank(orderResDto.getUserId())) {
                UserPo userPo = userMapper.selectOne(Wrappers.<UserPo>lambdaQuery().eq(UserPo::getId, orderResDto.getUserId()));
                if (userPo != null) {
                    orderResDto.setBuyerUserName(userPo.getName());
                }
            }
        }
        return resDtoPager;
    }

    @Override
    public OrderResDto selectOne(Long id) {
        OrderPo po = orderPoService.getById(id);
        OrderResDto resDto = orderResDtoConvertor.po2Dto(po);
        return resDto;
    }

    @Override
    public OrderResDto selectOne(Map<String, Object> params) {
        QueryWrapper queryWrapper = QueryParamUtils.queryWrapper4eq(OrderPo::new, params);
        OrderPo po = orderPoService.getOne(queryWrapper);
        OrderResDto resDto = orderResDtoConvertor.po2Dto(po);
        return resDto;
    }

    @Override
    public boolean updateProps(Long id, Map<String, Object> params) {
        UpdateWrapper<OrderPo> updateWrapper = QueryParamUtils.updateWrapper4Map(OrderPo::new, id, params);
        return orderPoService.update(new OrderPo(), updateWrapper);
        }

    @Override
    public boolean updateProps(Long id, OrderReqDto reqDto) {
        OrderPo po = orderReqDtoConvertor.dto2Po(reqDto);
        po.setId(id);
        return orderPoService.updateById(po);
    }


    @Override
    public boolean updateAllProps(Long id, OrderReqDto reqDto) {
        UpdateWrapper<OrderPo> updateWrapper = Wrappers.update();
        updateWrapper.eq("id", id);
        Field[] fields = OrderReqDto.class.getDeclaredFields();
        Arrays.stream(fields).filter(f -> !Modifier.isStatic(f.getModifiers())).forEach(field -> {
        try {
        PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), OrderReqDto.class);
        Method getMethod = propertyDescriptor.getReadMethod();
        String fileNameCamel = getMethod.getName().substring(3);
        String fileNameUnderline = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fileNameCamel);
        updateWrapper.set(fileNameUnderline, getMethod.invoke(reqDto));
        } catch (Exception ex) {
        log.warn("属性不存在get方法："+field.getName(),ex);
        }
        });
        return orderPoService.update(new OrderPo(), updateWrapper);
    }

    @Override
    public Boolean checkBuy(Long skuId, Long userId) {
        List<OrderPo> orderPoList = orderPoService.list(Wrappers.<OrderPo>lambdaQuery()
                .eq(OrderPo::getUserId, userId));
        if (CollectionUtils.isEmpty(orderPoList)) {
            return Boolean.FALSE;
        }
        List<String> orderNoList = orderPoList.stream().map(OrderPo::getOrderNo).collect(Collectors.toList());
        List<OrderDetailPo> orderDetailPos = orderDetailMapper.selectList(Wrappers.<OrderDetailPo>lambdaQuery()
                .in(OrderDetailPo::getOrderNo, orderNoList));
        if (CollectionUtils.isEmpty(orderDetailPos)) {
            return Boolean.FALSE;
        }
        for (OrderDetailPo orderDetailPo : orderDetailPos) {
            if (skuId.equals(Long.valueOf(orderDetailPo.getSkuId()))){
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    @Override
    public void send(Long id) {
        OrderPo orderPo = new OrderPo();
        orderPo.setId(id);
        orderPo.setSendTime(LocalDateTime.now());
        orderPo.setStatus(2);
        orderPoService.updateById(orderPo);
    }

    @Override
    public void receipt(Long id) {
        OrderPo orderPo = new OrderPo();
        orderPo.setId(id);
        orderPo.setReceiveTime(LocalDateTime.now());
        orderPo.setStatus(3);
        orderPoService.updateById(orderPo);
    }
}