package com.nova.service.impl;


import com.nova.common.convertor.PostReqDtoConvertor;
import com.nova.common.convertor.PostResDtoConvertor;
import com.nova.domain.dto.req.PostReqDto;
import com.nova.domain.dto.res.PostResDto;
import com.nova.domain.po.PostPo;
import com.nova.mapper.PostMapper;
import com.nova.service.PostService;
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
@Service("postAppService")
@Transactional(rollbackFor = Exception.class)
public class PostServiceImpl implements PostService {

    @Autowired
    private PostReqDtoConvertor postReqDtoConvertor;

    @Autowired
    private PostResDtoConvertor postResDtoConvertor;

    private IPService<PostPo> postPoService;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    public PostServiceImpl(PostMapper postMapper) {
        this.postPoService = new BasePService(postMapper);
    }


    @Override
    public PostResDto save(PostReqDto reqDto) {
        PostPo po = postReqDtoConvertor.dto2Po(reqDto);
        postPoService.save(po);
        PostResDto resDto = postResDtoConvertor.po2Dto(po);
        return resDto;
    }

    @Override
    public List<PostResDto> getList() {
        List<PostPo> list = postPoService.list();
        return postResDtoConvertor.poList2DtoList(list);
    }

    @Override
    public boolean deleteByIds(String ids) {
        try {
        List<Long> idLongList =
        Arrays.asList(ids.split(",")).stream().map(s -> NumberUtils.createLong(s.trim())).collect(Collectors.toList());
        return postPoService.removeByIds(idLongList);
        } catch (Exception e) {
        throw new AppException("参数错误：" + ids, ErrorCode.sys("417"), e);
        }
     }

    @Override
    public Pager<PostResDto> doPager(Map<String, Object> params) {
        Pager<PostPo> poPager = postPoService.queryPage(params, PostPo.class);
        Pager<PostResDto> resDtoPager = postResDtoConvertor.convertPoPager2ResDtoPager(poPager);
        return resDtoPager;
    }

    @Override
    public PostResDto selectOne(Long id) {
        PostPo po = postPoService.getById(id);
        PostResDto resDto = postResDtoConvertor.po2Dto(po);
        return resDto;
    }

    @Override
    public PostResDto selectOne(Map<String, Object> params) {
        QueryWrapper queryWrapper = QueryParamUtils.queryWrapper4eq(PostPo::new, params);
        PostPo po = postPoService.getOne(queryWrapper);
        PostResDto resDto = postResDtoConvertor.po2Dto(po);
        return resDto;
    }

    @Override
    public boolean updateProps(Long id, Map<String, Object> params) {
        UpdateWrapper<PostPo> updateWrapper = QueryParamUtils.updateWrapper4Map(PostPo::new, id, params);
        return postPoService.update(new PostPo(), updateWrapper);
        }

    @Override
    public boolean updateProps(Long id, PostReqDto reqDto) {
        PostPo po = postReqDtoConvertor.dto2Po(reqDto);
        po.setId(id);
        return postPoService.updateById(po);
    }


    @Override
    public boolean updateAllProps(Long id, PostReqDto reqDto) {
        UpdateWrapper<PostPo> updateWrapper = Wrappers.update();
        updateWrapper.eq("id", id);
        Field[] fields = PostReqDto.class.getDeclaredFields();
        Arrays.stream(fields).filter(f -> !Modifier.isStatic(f.getModifiers())).forEach(field -> {
        try {
        PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), PostReqDto.class);
        Method getMethod = propertyDescriptor.getReadMethod();
        String fileNameCamel = getMethod.getName().substring(3);
        String fileNameUnderline = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fileNameCamel);
        updateWrapper.set(fileNameUnderline, getMethod.invoke(reqDto));
        } catch (Exception ex) {
        log.warn("属性不存在get方法："+field.getName(),ex);
        }
        });
        return postPoService.update(new PostPo(), updateWrapper);
    }

}