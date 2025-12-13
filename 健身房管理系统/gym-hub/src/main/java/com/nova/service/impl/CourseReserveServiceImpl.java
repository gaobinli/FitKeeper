package com.nova.service.impl;


import com.nova.common.convertor.CourseReserveReqDtoConvertor;
import com.nova.common.convertor.CourseReserveResDtoConvertor;
import com.nova.domain.dto.req.CourseReserveReqDto;
import com.nova.domain.dto.res.CourseReserveResDto;
import com.nova.domain.po.CourseReservePo;
import com.nova.mapper.CourseReserveMapper;
import com.nova.service.CourseReserveService;
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
@Service("courseReserveAppService")
@Transactional(rollbackFor = Exception.class)
public class CourseReserveServiceImpl implements CourseReserveService {

    @Autowired
    private CourseReserveReqDtoConvertor courseReserveReqDtoConvertor;

    @Autowired
    private CourseReserveResDtoConvertor courseReserveResDtoConvertor;

    private IPService<CourseReservePo> courseReservePoService;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    public CourseReserveServiceImpl(CourseReserveMapper courseReserveMapper) {
        this.courseReservePoService = new BasePService(courseReserveMapper);
    }


    @Override
    public CourseReserveResDto save(CourseReserveReqDto reqDto) {
        CourseReservePo po = courseReserveReqDtoConvertor.dto2Po(reqDto);
        courseReservePoService.save(po);
        CourseReserveResDto resDto = courseReserveResDtoConvertor.po2Dto(po);
        return resDto;
    }

    @Override
    public List<CourseReserveResDto> getList(Map<String, Object> params) {
        if (!params.containsKey("pageSize")) {
            params.put("pageSize", 10000);
        }
        Pager<CourseReservePo> poPager = courseReservePoService.queryPage(params, CourseReservePo.class);
        Pager<CourseReserveResDto> resDtoPager = courseReserveResDtoConvertor.convertPoPager2ResDtoPager(poPager);
        return resDtoPager.getList();
    }

    @Override
    public boolean deleteByIds(String ids) {
        try {
        List<Long> idLongList =
        Arrays.asList(ids.split(",")).stream().map(s -> NumberUtils.createLong(s.trim())).collect(Collectors.toList());
        return courseReservePoService.removeByIds(idLongList);
        } catch (Exception e) {
        throw new AppException("参数错误：" + ids, ErrorCode.sys("417"), e);
        }
     }

    @Override
    public Pager<CourseReserveResDto> doPager(Map<String, Object> params) {
        Pager<CourseReservePo> poPager = courseReservePoService.queryPage(params, CourseReservePo.class);
        Pager<CourseReserveResDto> resDtoPager = courseReserveResDtoConvertor.convertPoPager2ResDtoPager(poPager);
        return resDtoPager;
    }

    @Override
    public CourseReserveResDto selectOne(Long id) {
        CourseReservePo po = courseReservePoService.getById(id);
        CourseReserveResDto resDto = courseReserveResDtoConvertor.po2Dto(po);
        return resDto;
    }

    @Override
    public CourseReserveResDto selectOne(Map<String, Object> params) {
        QueryWrapper queryWrapper = QueryParamUtils.queryWrapper4eq(CourseReservePo::new, params);
        CourseReservePo po = courseReservePoService.getOne(queryWrapper);
        CourseReserveResDto resDto = courseReserveResDtoConvertor.po2Dto(po);
        return resDto;
    }

    @Override
    public boolean updateProps(Long id, Map<String, Object> params) {
        UpdateWrapper<CourseReservePo> updateWrapper = QueryParamUtils.updateWrapper4Map(CourseReservePo::new, id, params);
        return courseReservePoService.update(new CourseReservePo(), updateWrapper);
        }

    @Override
    public boolean updateProps(Long id, CourseReserveReqDto reqDto) {
        CourseReservePo po = courseReserveReqDtoConvertor.dto2Po(reqDto);
        po.setId(id);
        return courseReservePoService.updateById(po);
    }


    @Override
    public boolean updateAllProps(Long id, CourseReserveReqDto reqDto) {
        UpdateWrapper<CourseReservePo> updateWrapper = Wrappers.update();
        updateWrapper.eq("id", id);
        Field[] fields = CourseReserveReqDto.class.getDeclaredFields();
        Arrays.stream(fields).filter(f -> !Modifier.isStatic(f.getModifiers())).forEach(field -> {
        try {
        PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), CourseReserveReqDto.class);
        Method getMethod = propertyDescriptor.getReadMethod();
        String fileNameCamel = getMethod.getName().substring(3);
        String fileNameUnderline = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fileNameCamel);
        updateWrapper.set(fileNameUnderline, getMethod.invoke(reqDto));
        } catch (Exception ex) {
        log.warn("属性不存在get方法："+field.getName(),ex);
        }
        });
        return courseReservePoService.update(new CourseReservePo(), updateWrapper);
    }

}