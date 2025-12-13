package com.nova.service.impl;


import com.nova.common.convertor.CoachReqDtoConvertor;
import com.nova.common.convertor.CoachResDtoConvertor;
import com.nova.common.util.ConditionUtil;
import com.nova.common.util.SecurityUtil;
import com.nova.domain.dto.req.CoachReqDto;
import com.nova.domain.dto.res.CoachResDto;
import com.nova.domain.dto.res.SysUserResDto;
import com.nova.domain.model.SysUserEntity;
import com.nova.domain.po.CoachPo;
import com.nova.domain.po.SysUserPo;
import com.nova.mapper.CoachMapper;
import com.nova.service.CoachService;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Slf4j
@Service("coachAppService")
@Transactional(rollbackFor = Exception.class)
public class CoachServiceImpl implements CoachService {

    @Autowired
    private CoachReqDtoConvertor coachReqDtoConvertor;

    @Autowired
    private CoachResDtoConvertor coachResDtoConvertor;

    @Autowired
    private CoachMapper coachMapper;

    private IPService<CoachPo> coachPoService;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    public CoachServiceImpl(CoachMapper coachMapper) {
        this.coachPoService = new BasePService(coachMapper);
    }


    @Override
    public CoachResDto save(CoachReqDto reqDto) {
        List<CoachPo> coachPos = coachMapper.selectList(Wrappers.<CoachPo>lambdaQuery()
                .eq(CoachPo::getUsername, reqDto.getUsername()));
        ConditionUtil.validCollectionIsEmpty(coachPos, "该账号已存在");
        if (StringUtils.isBlank(reqDto.getPassword())) {
            reqDto.setPassword(SecurityUtil.encryptPassword("123456"));
        } else {
            reqDto.setPassword(SecurityUtil.encryptPassword(reqDto.getPassword()));
        }
        CoachPo po = coachReqDtoConvertor.dto2Po(reqDto);
        coachPoService.save(po);
        CoachResDto resDto = coachResDtoConvertor.po2Dto(po);
        return resDto;
    }

    @Override
    public List<CoachResDto> getList() {
        List<CoachPo> list = coachPoService.list();
        return coachResDtoConvertor.poList2DtoList(list);
    }

    @Override
    public boolean deleteByIds(String ids) {
        try {
        List<Long> idLongList =
        Arrays.asList(ids.split(",")).stream().map(s -> NumberUtils.createLong(s.trim())).collect(Collectors.toList());
        return coachPoService.removeByIds(idLongList);
        } catch (Exception e) {
        throw new AppException("参数错误：" + ids, ErrorCode.sys("417"), e);
        }
     }

    @Override
    public Pager<CoachResDto> doPager(Map<String, Object> params) {
        Pager<CoachPo> poPager = coachPoService.queryPage(params, CoachPo.class);
        Pager<CoachResDto> resDtoPager = coachResDtoConvertor.convertPoPager2ResDtoPager(poPager);
        return resDtoPager;
    }

    @Override
    public CoachResDto selectOne(Long id) {
        CoachPo po = coachPoService.getById(id);
        CoachResDto resDto = coachResDtoConvertor.po2Dto(po);
        return resDto;
    }

    @Override
    public CoachResDto selectOne(Map<String, Object> params) {
        QueryWrapper queryWrapper = QueryParamUtils.queryWrapper4eq(CoachPo::new, params);
        CoachPo po = coachPoService.getOne(queryWrapper);
        CoachResDto resDto = coachResDtoConvertor.po2Dto(po);
        return resDto;
    }

    @Override
    public boolean updateProps(Long id, Map<String, Object> params) {
        UpdateWrapper<CoachPo> updateWrapper = QueryParamUtils.updateWrapper4Map(CoachPo::new, id, params);
        return coachPoService.update(new CoachPo(), updateWrapper);
        }

    @Override
    public boolean updateProps(Long id, CoachReqDto reqDto) {
        if (StringUtils.isNotBlank(reqDto.getPassword())) {
            reqDto.setPassword(SecurityUtil.encryptPassword(reqDto.getPassword()));
        }
        CoachPo po = coachReqDtoConvertor.dto2Po(reqDto);
        po.setId(id);

        return coachPoService.updateById(po);
    }


    @Override
    public boolean updateAllProps(Long id, CoachReqDto reqDto) {
        UpdateWrapper<CoachPo> updateWrapper = Wrappers.update();
        updateWrapper.eq("id", id);
        Field[] fields = CoachReqDto.class.getDeclaredFields();
        Arrays.stream(fields).filter(f -> !Modifier.isStatic(f.getModifiers())).forEach(field -> {
        try {
        PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), CoachReqDto.class);
        Method getMethod = propertyDescriptor.getReadMethod();
        String fileNameCamel = getMethod.getName().substring(3);
        String fileNameUnderline = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fileNameCamel);
        updateWrapper.set(fileNameUnderline, getMethod.invoke(reqDto));
        } catch (Exception ex) {
        log.warn("属性不存在get方法："+field.getName(),ex);
        }
        });
        return coachPoService.update(new CoachPo(), updateWrapper);
    }

    @Override
    public CoachResDto info() {
        SysUserEntity sysUser = SecurityUtil.getLoginUser().getSysUser();
        CoachPo coachPo = coachMapper.selectOne(Wrappers.<CoachPo>lambdaQuery().eq(CoachPo::getUsername, sysUser.getUsername()));
        CoachResDto coachResDto = coachResDtoConvertor.po2Dto(coachPo);
        return coachResDto;
    }

}