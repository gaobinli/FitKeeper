package com.nova.service.impl;


import com.nova.common.convertor.SkuReqDtoConvertor;
import com.nova.common.convertor.SkuResDtoConvertor;
import com.nova.domain.dto.req.SkuReqDto;
import com.nova.domain.dto.res.SkuResDto;
import com.nova.domain.po.SkuPo;
import com.nova.mapper.SkuMapper;
import com.nova.service.SkuService;
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
@Service("skuAppService")
@Transactional(rollbackFor = Exception.class)
public class SkuServiceImpl implements SkuService {

    @Autowired
    private SkuReqDtoConvertor skuReqDtoConvertor;

    @Autowired
    private SkuResDtoConvertor skuResDtoConvertor;

    private IPService<SkuPo> skuPoService;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    public SkuServiceImpl(SkuMapper skuMapper) {
        this.skuPoService = new BasePService(skuMapper);
    }


    @Override
    public SkuResDto save(SkuReqDto reqDto) {
        SkuPo po = skuReqDtoConvertor.dto2Po(reqDto);
        skuPoService.save(po);
        SkuResDto resDto = skuResDtoConvertor.po2Dto(po);
        return resDto;
    }

    @Override
    public List<SkuResDto> getList() {
        List<SkuPo> list = skuPoService.list();
        return skuResDtoConvertor.poList2DtoList(list);
    }

    @Override
    public boolean deleteByIds(String ids) {
        try {
        List<Long> idLongList =
        Arrays.asList(ids.split(",")).stream().map(s -> NumberUtils.createLong(s.trim())).collect(Collectors.toList());
        return skuPoService.removeByIds(idLongList);
        } catch (Exception e) {
        throw new AppException("参数错误：" + ids, ErrorCode.sys("417"), e);
        }
     }

    @Override
    public Pager<SkuResDto> doPager(Map<String, Object> params) {
        Pager<SkuPo> poPager = skuPoService.queryPage(params, SkuPo.class);
        Pager<SkuResDto> resDtoPager = skuResDtoConvertor.convertPoPager2ResDtoPager(poPager);
        return resDtoPager;
    }

    @Override
    public SkuResDto selectOne(Long id) {
        SkuPo po = skuPoService.getById(id);
        SkuResDto resDto = skuResDtoConvertor.po2Dto(po);
        return resDto;
    }

    @Override
    public SkuResDto selectOne(Map<String, Object> params) {
        QueryWrapper queryWrapper = QueryParamUtils.queryWrapper4eq(SkuPo::new, params);
        SkuPo po = skuPoService.getOne(queryWrapper);
        SkuResDto resDto = skuResDtoConvertor.po2Dto(po);
        return resDto;
    }

    @Override
    public boolean updateProps(Long id, Map<String, Object> params) {
        UpdateWrapper<SkuPo> updateWrapper = QueryParamUtils.updateWrapper4Map(SkuPo::new, id, params);
        return skuPoService.update(new SkuPo(), updateWrapper);
        }

    @Override
    public boolean updateProps(Long id, SkuReqDto reqDto) {
        SkuPo po = skuReqDtoConvertor.dto2Po(reqDto);
        po.setId(id);
        return skuPoService.updateById(po);
    }


    @Override
    public boolean updateAllProps(Long id, SkuReqDto reqDto) {
        UpdateWrapper<SkuPo> updateWrapper = Wrappers.update();
        updateWrapper.eq("id", id);
        Field[] fields = SkuReqDto.class.getDeclaredFields();
        Arrays.stream(fields).filter(f -> !Modifier.isStatic(f.getModifiers())).forEach(field -> {
        try {
        PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), SkuReqDto.class);
        Method getMethod = propertyDescriptor.getReadMethod();
        String fileNameCamel = getMethod.getName().substring(3);
        String fileNameUnderline = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fileNameCamel);
        updateWrapper.set(fileNameUnderline, getMethod.invoke(reqDto));
        } catch (Exception ex) {
        log.warn("属性不存在get方法："+field.getName(),ex);
        }
        });
        return skuPoService.update(new SkuPo(), updateWrapper);
    }

}