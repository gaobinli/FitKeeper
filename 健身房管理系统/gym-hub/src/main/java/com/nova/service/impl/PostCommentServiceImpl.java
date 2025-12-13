package com.nova.service.impl;


import com.nova.common.convertor.PostCommentReqDtoConvertor;
import com.nova.common.convertor.PostCommentResDtoConvertor;
import com.nova.domain.dto.req.PostCommentReqDto;
import com.nova.domain.dto.res.PostCommentResDto;
import com.nova.domain.po.PostCommentPo;
import com.nova.mapper.PostCommentMapper;
import com.nova.service.PostCommentService;
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
@Service("postCommentAppService")
@Transactional(rollbackFor = Exception.class)
public class PostCommentServiceImpl implements PostCommentService {

    @Autowired
    private PostCommentReqDtoConvertor postCommentReqDtoConvertor;

    @Autowired
    private PostCommentResDtoConvertor postCommentResDtoConvertor;

    private IPService<PostCommentPo> postCommentPoService;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    public PostCommentServiceImpl(PostCommentMapper postCommentMapper) {
        this.postCommentPoService = new BasePService(postCommentMapper);
    }


    @Override
    public PostCommentResDto save(PostCommentReqDto reqDto) {
        PostCommentPo po = postCommentReqDtoConvertor.dto2Po(reqDto);
        postCommentPoService.save(po);
        PostCommentResDto resDto = postCommentResDtoConvertor.po2Dto(po);
        return resDto;
    }

    @Override
    public List<PostCommentResDto> getList() {
        List<PostCommentPo> list = postCommentPoService.list();
        return postCommentResDtoConvertor.poList2DtoList(list);
    }

    @Override
    public boolean deleteByIds(String ids) {
        try {
        List<Long> idLongList =
        Arrays.asList(ids.split(",")).stream().map(s -> NumberUtils.createLong(s.trim())).collect(Collectors.toList());
        return postCommentPoService.removeByIds(idLongList);
        } catch (Exception e) {
        throw new AppException("参数错误：" + ids, ErrorCode.sys("417"), e);
        }
     }

    @Override
    public Pager<PostCommentResDto> doPager(Map<String, Object> params) {
        Pager<PostCommentPo> poPager = postCommentPoService.queryPage(params, PostCommentPo.class);
        Pager<PostCommentResDto> resDtoPager = postCommentResDtoConvertor.convertPoPager2ResDtoPager(poPager);
        return resDtoPager;
    }

    @Override
    public PostCommentResDto selectOne(Long id) {
        PostCommentPo po = postCommentPoService.getById(id);
        PostCommentResDto resDto = postCommentResDtoConvertor.po2Dto(po);
        return resDto;
    }

    @Override
    public PostCommentResDto selectOne(Map<String, Object> params) {
        QueryWrapper queryWrapper = QueryParamUtils.queryWrapper4eq(PostCommentPo::new, params);
        PostCommentPo po = postCommentPoService.getOne(queryWrapper);
        PostCommentResDto resDto = postCommentResDtoConvertor.po2Dto(po);
        return resDto;
    }

    @Override
    public boolean updateProps(Long id, Map<String, Object> params) {
        UpdateWrapper<PostCommentPo> updateWrapper = QueryParamUtils.updateWrapper4Map(PostCommentPo::new, id, params);
        return postCommentPoService.update(new PostCommentPo(), updateWrapper);
        }

    @Override
    public boolean updateProps(Long id, PostCommentReqDto reqDto) {
        PostCommentPo po = postCommentReqDtoConvertor.dto2Po(reqDto);
        po.setId(id);
        return postCommentPoService.updateById(po);
    }


    @Override
    public boolean updateAllProps(Long id, PostCommentReqDto reqDto) {
        UpdateWrapper<PostCommentPo> updateWrapper = Wrappers.update();
        updateWrapper.eq("id", id);
        Field[] fields = PostCommentReqDto.class.getDeclaredFields();
        Arrays.stream(fields).filter(f -> !Modifier.isStatic(f.getModifiers())).forEach(field -> {
        try {
        PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), PostCommentReqDto.class);
        Method getMethod = propertyDescriptor.getReadMethod();
        String fileNameCamel = getMethod.getName().substring(3);
        String fileNameUnderline = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fileNameCamel);
        updateWrapper.set(fileNameUnderline, getMethod.invoke(reqDto));
        } catch (Exception ex) {
        log.warn("属性不存在get方法："+field.getName(),ex);
        }
        });
        return postCommentPoService.update(new PostCommentPo(), updateWrapper);
    }

}