package com.nova.service.impl;


import com.nova.common.convertor.SkuAppraiseReqDtoConvertor;
import com.nova.common.convertor.SkuAppraiseResDtoConvertor;
import com.nova.domain.dto.req.SkuAppraiseReqDto;
import com.nova.domain.dto.res.SkuAppraiseResDto;
import com.nova.domain.po.SkuAppraisePo;
import com.nova.mapper.SkuAppraiseMapper;
import com.nova.service.SkuAppraiseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.base.CaseFormat;
import com.nova.common.exception.AppException;
import com.nova.common.exception.ErrorCode;
import com.baomidou.mybatisplus.ext.BasePService;
import com.baomidou.mybatisplus.ext.IPService;
import com.baomidou.mybatisplus.ext.Pager;
import com.baomidou.mybatisplus.ext.QueryParamUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Service("skuAppraiseAppService")
@Transactional(rollbackFor = Exception.class)
public class SkuAppraiseServiceImpl implements SkuAppraiseService {

    @Autowired
    private SkuAppraiseReqDtoConvertor skuAppraiseReqDtoConvertor;

    @Autowired
    private SkuAppraiseResDtoConvertor skuAppraiseResDtoConvertor;

    private IPService<SkuAppraisePo> skuAppraisePoService;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    public SkuAppraiseServiceImpl(SkuAppraiseMapper skuAppraiseMapper) {
        this.skuAppraisePoService = new BasePService(skuAppraiseMapper);
    }


    @Override
    public SkuAppraiseResDto save(SkuAppraiseReqDto reqDto) {
        SkuAppraisePo po = skuAppraiseReqDtoConvertor.dto2Po(reqDto);
        skuAppraisePoService.save(po);
        SkuAppraiseResDto resDto = skuAppraiseResDtoConvertor.po2Dto(po);
        return resDto;
    }

    @Override
    public List<SkuAppraiseResDto> getList() {
        List<SkuAppraisePo> list = skuAppraisePoService.list();
        return skuAppraiseResDtoConvertor.poList2DtoList(list);
    }

    @Override
    public boolean deleteByIds(String ids) {
        try {
        List<Long> idLongList =
        Arrays.asList(ids.split(",")).stream().map(s -> NumberUtils.createLong(s.trim())).collect(Collectors.toList());
        return skuAppraisePoService.removeByIds(idLongList);
        } catch (Exception e) {
        throw new AppException("参数错误：" + ids, ErrorCode.sys("417"), e);
        }
     }

    @Override
    public Pager<SkuAppraiseResDto> doPager(Map<String, Object> params) {
        Pager<SkuAppraisePo> poPager = skuAppraisePoService.queryPage(params, SkuAppraisePo.class);
        Pager<SkuAppraiseResDto> resDtoPager = skuAppraiseResDtoConvertor.convertPoPager2ResDtoPager(poPager);
        return resDtoPager;
    }

    @Override
    public SkuAppraiseResDto selectOne(Long id) {
        SkuAppraisePo po = skuAppraisePoService.getById(id);
        SkuAppraiseResDto resDto = skuAppraiseResDtoConvertor.po2Dto(po);
        return resDto;
    }

    @Override
    public SkuAppraiseResDto selectOne(Map<String, Object> params) {
        QueryWrapper queryWrapper = QueryParamUtils.queryWrapper4eq(SkuAppraisePo::new, params);
        SkuAppraisePo po = skuAppraisePoService.getOne(queryWrapper);
        SkuAppraiseResDto resDto = skuAppraiseResDtoConvertor.po2Dto(po);
        return resDto;
    }

    @Override
    public boolean updateProps(Long id, Map<String, Object> params) {
        UpdateWrapper<SkuAppraisePo> updateWrapper = QueryParamUtils.updateWrapper4Map(SkuAppraisePo::new, id, params);
        return skuAppraisePoService.update(new SkuAppraisePo(), updateWrapper);
        }

    @Override
    public boolean updateProps(Long id, SkuAppraiseReqDto reqDto) {
        SkuAppraisePo po = skuAppraiseReqDtoConvertor.dto2Po(reqDto);
        po.setId(id);
        return skuAppraisePoService.updateById(po);
    }


    @Override
    public boolean updateAllProps(Long id, SkuAppraiseReqDto reqDto) {
        UpdateWrapper<SkuAppraisePo> updateWrapper = Wrappers.update();
        updateWrapper.eq("id", id);
        Field[] fields = SkuAppraiseReqDto.class.getDeclaredFields();
        Arrays.stream(fields).filter(f -> !Modifier.isStatic(f.getModifiers())).forEach(field -> {
        try {
        PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), SkuAppraiseReqDto.class);
        Method getMethod = propertyDescriptor.getReadMethod();
        String fileNameCamel = getMethod.getName().substring(3);
        String fileNameUnderline = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fileNameCamel);
        updateWrapper.set(fileNameUnderline, getMethod.invoke(reqDto));
        } catch (Exception ex) {
        log.warn("属性不存在get方法："+field.getName(),ex);
        }
        });
        return skuAppraisePoService.update(new SkuAppraisePo(), updateWrapper);
    }

}