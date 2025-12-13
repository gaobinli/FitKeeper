package com.nova.service.impl;


import com.nova.common.convertor.ClassificationReqDtoConvertor;
import com.nova.common.convertor.ClassificationResDtoConvertor;
import com.nova.domain.dto.req.ClassificationReqDto;
import com.nova.domain.dto.res.ClassificationResDto;
import com.nova.domain.po.ClassificationPo;
import com.nova.mapper.ClassificationMapper;
import com.nova.service.ClassificationService;
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
@Service("classificationAppService")
@Transactional(rollbackFor = Exception.class)
public class ClassificationServiceImpl implements ClassificationService {

    @Autowired
    private ClassificationReqDtoConvertor classificationReqDtoConvertor;

    @Autowired
    private ClassificationResDtoConvertor classificationResDtoConvertor;

    private IPService<ClassificationPo> classificationPoService;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    public ClassificationServiceImpl(ClassificationMapper classificationMapper) {
        this.classificationPoService = new BasePService(classificationMapper);
    }


    @Override
    public ClassificationResDto save(ClassificationReqDto reqDto) {
        ClassificationPo po = classificationReqDtoConvertor.dto2Po(reqDto);
        classificationPoService.save(po);
        ClassificationResDto resDto = classificationResDtoConvertor.po2Dto(po);
        return resDto;
    }

    @Override
    public List<ClassificationResDto> getList(Map<String, Object> params) {
        if (!params.containsKey("pageSize")) {
            params.put("pageSize", 10000);
        }
        Pager<ClassificationPo> poPager = classificationPoService.queryPage(params, ClassificationPo.class);
        Pager<ClassificationResDto> resDtoPager = classificationResDtoConvertor.convertPoPager2ResDtoPager(poPager);
        return resDtoPager.getList();
    }

    @Override
    public boolean deleteByIds(String ids) {
        try {
        List<Long> idLongList =
        Arrays.asList(ids.split(",")).stream().map(s -> NumberUtils.createLong(s.trim())).collect(Collectors.toList());
        return classificationPoService.removeByIds(idLongList);
        } catch (Exception e) {
        throw new AppException("参数错误：" + ids, ErrorCode.sys("417"), e);
        }
     }

    @Override
    public Pager<ClassificationResDto> doPager(Map<String, Object> params) {
        Pager<ClassificationPo> poPager = classificationPoService.queryPage(params, ClassificationPo.class);
        Pager<ClassificationResDto> resDtoPager = classificationResDtoConvertor.convertPoPager2ResDtoPager(poPager);
        return resDtoPager;
    }

    @Override
    public ClassificationResDto selectOne(Long id) {
        ClassificationPo po = classificationPoService.getById(id);
        ClassificationResDto resDto = classificationResDtoConvertor.po2Dto(po);
        return resDto;
    }

    @Override
    public ClassificationResDto selectOne(Map<String, Object> params) {
        QueryWrapper queryWrapper = QueryParamUtils.queryWrapper4eq(ClassificationPo::new, params);
        ClassificationPo po = classificationPoService.getOne(queryWrapper);
        ClassificationResDto resDto = classificationResDtoConvertor.po2Dto(po);
        return resDto;
    }

    @Override
    public boolean updateProps(Long id, Map<String, Object> params) {
        UpdateWrapper<ClassificationPo> updateWrapper = QueryParamUtils.updateWrapper4Map(ClassificationPo::new, id, params);
        return classificationPoService.update(new ClassificationPo(), updateWrapper);
        }

    @Override
    public boolean updateProps(Long id, ClassificationReqDto reqDto) {
        ClassificationPo po = classificationReqDtoConvertor.dto2Po(reqDto);
        po.setId(id);
        return classificationPoService.updateById(po);
    }


    @Override
    public boolean updateAllProps(Long id, ClassificationReqDto reqDto) {
        UpdateWrapper<ClassificationPo> updateWrapper = Wrappers.update();
        updateWrapper.eq("id", id);
        Field[] fields = ClassificationReqDto.class.getDeclaredFields();
        Arrays.stream(fields).filter(f -> !Modifier.isStatic(f.getModifiers())).forEach(field -> {
        try {
        PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), ClassificationReqDto.class);
        Method getMethod = propertyDescriptor.getReadMethod();
        String fileNameCamel = getMethod.getName().substring(3);
        String fileNameUnderline = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fileNameCamel);
        updateWrapper.set(fileNameUnderline, getMethod.invoke(reqDto));
        } catch (Exception ex) {
        log.warn("属性不存在get方法："+field.getName(),ex);
        }
        });
        return classificationPoService.update(new ClassificationPo(), updateWrapper);
    }

}