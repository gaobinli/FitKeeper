package com.nova.service.impl;


import com.nova.common.convertor.CourseAppraiseReqDtoConvertor;
import com.nova.common.convertor.CourseAppraiseResDtoConvertor;
import com.nova.domain.dto.req.CourseAppraiseReqDto;
import com.nova.domain.dto.res.CourseAppraiseResDto;
import com.nova.domain.po.CourseAppraisePo;
import com.nova.mapper.CourseAppraiseMapper;
import com.nova.service.CourseAppraiseService;
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
@Service("courseAppraiseAppService")
@Transactional(rollbackFor = Exception.class)
public class CourseAppraiseServiceImpl implements CourseAppraiseService {

    @Autowired
    private CourseAppraiseReqDtoConvertor courseAppraiseReqDtoConvertor;

    @Autowired
    private CourseAppraiseResDtoConvertor courseAppraiseResDtoConvertor;

    private IPService<CourseAppraisePo> courseAppraisePoService;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    public CourseAppraiseServiceImpl(CourseAppraiseMapper courseAppraiseMapper) {
        this.courseAppraisePoService = new BasePService(courseAppraiseMapper);
    }


    @Override
    public CourseAppraiseResDto save(CourseAppraiseReqDto reqDto) {
        CourseAppraisePo po = courseAppraiseReqDtoConvertor.dto2Po(reqDto);
        courseAppraisePoService.save(po);
        CourseAppraiseResDto resDto = courseAppraiseResDtoConvertor.po2Dto(po);
        return resDto;
    }

    @Override
    public List<CourseAppraiseResDto> getList(Map<String, Object> params) {
        if (!params.containsKey("pageSize")) {
            params.put("pageSize", 10000);
        }
        Pager<CourseAppraisePo> poPager = courseAppraisePoService.queryPage(params, CourseAppraisePo.class);
        Pager<CourseAppraiseResDto> resDtoPager = courseAppraiseResDtoConvertor.convertPoPager2ResDtoPager(poPager);
        return resDtoPager.getList();
    }

    @Override
    public boolean deleteByIds(String ids) {
        try {
        List<Long> idLongList =
        Arrays.asList(ids.split(",")).stream().map(s -> NumberUtils.createLong(s.trim())).collect(Collectors.toList());
        return courseAppraisePoService.removeByIds(idLongList);
        } catch (Exception e) {
        throw new AppException("参数错误：" + ids, ErrorCode.sys("417"), e);
        }
     }

    @Override
    public Pager<CourseAppraiseResDto> doPager(Map<String, Object> params) {
        Pager<CourseAppraisePo> poPager = courseAppraisePoService.queryPage(params, CourseAppraisePo.class);
        Pager<CourseAppraiseResDto> resDtoPager = courseAppraiseResDtoConvertor.convertPoPager2ResDtoPager(poPager);
        return resDtoPager;
    }

    @Override
    public CourseAppraiseResDto selectOne(Long id) {
        CourseAppraisePo po = courseAppraisePoService.getById(id);
        CourseAppraiseResDto resDto = courseAppraiseResDtoConvertor.po2Dto(po);
        return resDto;
    }

    @Override
    public CourseAppraiseResDto selectOne(Map<String, Object> params) {
        QueryWrapper queryWrapper = QueryParamUtils.queryWrapper4eq(CourseAppraisePo::new, params);
        CourseAppraisePo po = courseAppraisePoService.getOne(queryWrapper);
        CourseAppraiseResDto resDto = courseAppraiseResDtoConvertor.po2Dto(po);
        return resDto;
    }

    @Override
    public boolean updateProps(Long id, Map<String, Object> params) {
        UpdateWrapper<CourseAppraisePo> updateWrapper = QueryParamUtils.updateWrapper4Map(CourseAppraisePo::new, id, params);
        return courseAppraisePoService.update(new CourseAppraisePo(), updateWrapper);
        }

    @Override
    public boolean updateProps(Long id, CourseAppraiseReqDto reqDto) {
        CourseAppraisePo po = courseAppraiseReqDtoConvertor.dto2Po(reqDto);
        po.setId(id);
        return courseAppraisePoService.updateById(po);
    }


    @Override
    public boolean updateAllProps(Long id, CourseAppraiseReqDto reqDto) {
        UpdateWrapper<CourseAppraisePo> updateWrapper = Wrappers.update();
        updateWrapper.eq("id", id);
        Field[] fields = CourseAppraiseReqDto.class.getDeclaredFields();
        Arrays.stream(fields).filter(f -> !Modifier.isStatic(f.getModifiers())).forEach(field -> {
        try {
        PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), CourseAppraiseReqDto.class);
        Method getMethod = propertyDescriptor.getReadMethod();
        String fileNameCamel = getMethod.getName().substring(3);
        String fileNameUnderline = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fileNameCamel);
        updateWrapper.set(fileNameUnderline, getMethod.invoke(reqDto));
        } catch (Exception ex) {
        log.warn("属性不存在get方法："+field.getName(),ex);
        }
        });
        return courseAppraisePoService.update(new CourseAppraisePo(), updateWrapper);
    }

}