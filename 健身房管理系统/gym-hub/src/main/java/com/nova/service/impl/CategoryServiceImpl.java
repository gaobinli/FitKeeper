package com.nova.service.impl;


import com.nova.common.convertor.CategoryReqDtoConvertor;
import com.nova.common.convertor.CategoryResDtoConvertor;
import com.nova.domain.dto.req.CategoryReqDto;
import com.nova.domain.dto.res.CategoryResDto;
import com.nova.domain.po.CategoryPo;
import com.nova.mapper.CategoryMapper;
import com.nova.service.CategoryService;
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
@Service("categoryAppService")
@Transactional(rollbackFor = Exception.class)
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryReqDtoConvertor categoryReqDtoConvertor;

    @Autowired
    private CategoryResDtoConvertor categoryResDtoConvertor;

    private IPService<CategoryPo> categoryPoService;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryPoService = new BasePService(categoryMapper);
    }


    @Override
    public CategoryResDto save(CategoryReqDto reqDto) {
        CategoryPo po = categoryReqDtoConvertor.dto2Po(reqDto);
        categoryPoService.save(po);
        CategoryResDto resDto = categoryResDtoConvertor.po2Dto(po);
        return resDto;
    }

    @Override
    public List<CategoryResDto> getList(Map<String, Object> params) {
        params.put("pageSize", 1000);
        params.put("currentPage", 1);
        Pager<CategoryResDto> categoryResDtoPager = doPager(params);
        return categoryResDtoPager.getList();
    }

    @Override
    public boolean deleteByIds(String ids) {
        try {
        List<Long> idLongList =
        Arrays.asList(ids.split(",")).stream().map(s -> NumberUtils.createLong(s.trim())).collect(Collectors.toList());
        return categoryPoService.removeByIds(idLongList);
        } catch (Exception e) {
        throw new AppException("参数错误：" + ids, ErrorCode.sys("417"), e);
        }
     }

    @Override
    public Pager<CategoryResDto> doPager(Map<String, Object> params) {
        Pager<CategoryPo> poPager = categoryPoService.queryPage(params, CategoryPo.class);
        Pager<CategoryResDto> resDtoPager = categoryResDtoConvertor.convertPoPager2ResDtoPager(poPager);
        return resDtoPager;
    }

    @Override
    public CategoryResDto selectOne(Long id) {
        CategoryPo po = categoryPoService.getById(id);
        CategoryResDto resDto = categoryResDtoConvertor.po2Dto(po);
        return resDto;
    }

    @Override
    public CategoryResDto selectOne(Map<String, Object> params) {
        QueryWrapper queryWrapper = QueryParamUtils.queryWrapper4eq(CategoryPo::new, params);
        CategoryPo po = categoryPoService.getOne(queryWrapper);
        CategoryResDto resDto = categoryResDtoConvertor.po2Dto(po);
        return resDto;
    }

    @Override
    public boolean updateProps(Long id, Map<String, Object> params) {
        UpdateWrapper<CategoryPo> updateWrapper = QueryParamUtils.updateWrapper4Map(CategoryPo::new, id, params);
        return categoryPoService.update(new CategoryPo(), updateWrapper);
        }

    @Override
    public boolean updateProps(Long id, CategoryReqDto reqDto) {
        CategoryPo po = categoryReqDtoConvertor.dto2Po(reqDto);
        po.setId(id);
        return categoryPoService.updateById(po);
    }


    @Override
    public boolean updateAllProps(Long id, CategoryReqDto reqDto) {
        UpdateWrapper<CategoryPo> updateWrapper = Wrappers.update();
        updateWrapper.eq("id", id);
        Field[] fields = CategoryReqDto.class.getDeclaredFields();
        Arrays.stream(fields).filter(f -> !Modifier.isStatic(f.getModifiers())).forEach(field -> {
        try {
        PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), CategoryReqDto.class);
        Method getMethod = propertyDescriptor.getReadMethod();
        String fileNameCamel = getMethod.getName().substring(3);
        String fileNameUnderline = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fileNameCamel);
        updateWrapper.set(fileNameUnderline, getMethod.invoke(reqDto));
        } catch (Exception ex) {
        log.warn("属性不存在get方法："+field.getName(),ex);
        }
        });
        return categoryPoService.update(new CategoryPo(), updateWrapper);
    }

}