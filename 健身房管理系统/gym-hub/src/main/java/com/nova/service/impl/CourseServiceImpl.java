package com.nova.service.impl;


import com.nova.common.convertor.CourseReqDtoConvertor;
import com.nova.common.convertor.CourseResDtoConvertor;
import com.nova.domain.dto.req.CourseReqDto;
import com.nova.domain.dto.res.CourseResDto;
import com.nova.domain.po.CoursePo;
import com.nova.mapper.CourseMapper;
import com.nova.service.CourseService;
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
@Service("courseAppService")
@Transactional(rollbackFor = Exception.class)
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseReqDtoConvertor courseReqDtoConvertor;

    @Autowired
    private CourseResDtoConvertor courseResDtoConvertor;

    private IPService<CoursePo> coursePoService;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    public CourseServiceImpl(CourseMapper courseMapper) {
        this.coursePoService = new BasePService(courseMapper);
    }


    @Override
    public CourseResDto save(CourseReqDto reqDto) {
        CoursePo po = courseReqDtoConvertor.dto2Po(reqDto);
        coursePoService.save(po);
        CourseResDto resDto = courseResDtoConvertor.po2Dto(po);
        return resDto;
    }

    @Override
    public List<CourseResDto> getList(Map<String, Object> params) {
        if (!params.containsKey("pageSize")) {
            params.put("pageSize", 10000);
        }
        Pager<CoursePo> poPager = coursePoService.queryPage(params, CoursePo.class);
        Pager<CourseResDto> resDtoPager = courseResDtoConvertor.convertPoPager2ResDtoPager(poPager);
        return resDtoPager.getList();
    }

    @Override
    public boolean deleteByIds(String ids) {
        try {
        List<Long> idLongList =
        Arrays.asList(ids.split(",")).stream().map(s -> NumberUtils.createLong(s.trim())).collect(Collectors.toList());
        return coursePoService.removeByIds(idLongList);
        } catch (Exception e) {
        throw new AppException("参数错误：" + ids, ErrorCode.sys("417"), e);
        }
     }

    @Override
    public Pager<CourseResDto> doPager(Map<String, Object> params) {
        Pager<CoursePo> poPager = coursePoService.queryPage(params, CoursePo.class);
        Pager<CourseResDto> resDtoPager = courseResDtoConvertor.convertPoPager2ResDtoPager(poPager);
        return resDtoPager;
    }

    @Override
    public CourseResDto selectOne(Long id) {
        CoursePo po = coursePoService.getById(id);
        CourseResDto resDto = courseResDtoConvertor.po2Dto(po);
        return resDto;
    }

    @Override
    public CourseResDto selectOne(Map<String, Object> params) {
        QueryWrapper queryWrapper = QueryParamUtils.queryWrapper4eq(CoursePo::new, params);
        CoursePo po = coursePoService.getOne(queryWrapper);
        CourseResDto resDto = courseResDtoConvertor.po2Dto(po);
        return resDto;
    }

    @Override
    public boolean updateProps(Long id, Map<String, Object> params) {
        UpdateWrapper<CoursePo> updateWrapper = QueryParamUtils.updateWrapper4Map(CoursePo::new, id, params);
        return coursePoService.update(new CoursePo(), updateWrapper);
        }

    @Override
    public boolean updateProps(Long id, CourseReqDto reqDto) {
        CoursePo po = courseReqDtoConvertor.dto2Po(reqDto);
        po.setId(id);
        return coursePoService.updateById(po);
    }


    @Override
    public boolean updateAllProps(Long id, CourseReqDto reqDto) {
        UpdateWrapper<CoursePo> updateWrapper = Wrappers.update();
        updateWrapper.eq("id", id);
        Field[] fields = CourseReqDto.class.getDeclaredFields();
        Arrays.stream(fields).filter(f -> !Modifier.isStatic(f.getModifiers())).forEach(field -> {
        try {
        PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), CourseReqDto.class);
        Method getMethod = propertyDescriptor.getReadMethod();
        String fileNameCamel = getMethod.getName().substring(3);
        String fileNameUnderline = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fileNameCamel);
        updateWrapper.set(fileNameUnderline, getMethod.invoke(reqDto));
        } catch (Exception ex) {
        log.warn("属性不存在get方法："+field.getName(),ex);
        }
        });
        return coursePoService.update(new CoursePo(), updateWrapper);
    }

}