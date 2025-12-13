package com.nova.service.impl;


import com.nova.common.convertor.RechargeRecordReqDtoConvertor;
import com.nova.common.convertor.RechargeRecordResDtoConvertor;
import com.nova.domain.dto.req.RechargeRecordReqDto;
import com.nova.domain.dto.res.RechargeRecordResDto;
import com.nova.domain.po.RechargeRecordPo;
import com.nova.domain.po.UserPo;
import com.nova.mapper.RechargeRecordMapper;
import com.nova.mapper.UserMapper;
import com.nova.service.RechargeRecordService;
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
@Service("rechargeRecordAppService")
@Transactional(rollbackFor = Exception.class)
public class RechargeRecordServiceImpl implements RechargeRecordService {

    @Autowired
    private RechargeRecordReqDtoConvertor rechargeRecordReqDtoConvertor;

    @Autowired
    private RechargeRecordResDtoConvertor rechargeRecordResDtoConvertor;

    @Autowired
    private UserMapper userMapper;

    private IPService<RechargeRecordPo> rechargeRecordPoService;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    public RechargeRecordServiceImpl(RechargeRecordMapper rechargeRecordMapper) {
        this.rechargeRecordPoService = new BasePService(rechargeRecordMapper);
    }


    @Override
    public RechargeRecordResDto save(RechargeRecordReqDto reqDto) {
        Long userId = reqDto.getUserId();
        UserPo userPo = userMapper.selectById(userId);
        userPo.setBalance(reqDto.getMoney() + (null == userPo.getBalance() ? 0 : userPo.getBalance()));
        userMapper.updateById(userPo);

        RechargeRecordPo po = rechargeRecordReqDtoConvertor.dto2Po(reqDto);
        rechargeRecordPoService.save(po);
        RechargeRecordResDto resDto = rechargeRecordResDtoConvertor.po2Dto(po);
        return resDto;
    }

    @Override
    public List<RechargeRecordResDto> getList(Map<String, Object> params) {
        if (!params.containsKey("pageSize")) {
            params.put("pageSize", 10000);
        }
        Pager<RechargeRecordPo> poPager = rechargeRecordPoService.queryPage(params, RechargeRecordPo.class);
        Pager<RechargeRecordResDto> resDtoPager = rechargeRecordResDtoConvertor.convertPoPager2ResDtoPager(poPager);
        return resDtoPager.getList();
    }

    @Override
    public boolean deleteByIds(String ids) {
        try {
        List<Long> idLongList =
        Arrays.asList(ids.split(",")).stream().map(s -> NumberUtils.createLong(s.trim())).collect(Collectors.toList());
        return rechargeRecordPoService.removeByIds(idLongList);
        } catch (Exception e) {
        throw new AppException("参数错误：" + ids, ErrorCode.sys("417"), e);
        }
     }

    @Override
    public Pager<RechargeRecordResDto> doPager(Map<String, Object> params) {
        Pager<RechargeRecordPo> poPager = rechargeRecordPoService.queryPage(params, RechargeRecordPo.class);
        Pager<RechargeRecordResDto> resDtoPager = rechargeRecordResDtoConvertor.convertPoPager2ResDtoPager(poPager);
        return resDtoPager;
    }

    @Override
    public RechargeRecordResDto selectOne(Long id) {
        RechargeRecordPo po = rechargeRecordPoService.getById(id);
        RechargeRecordResDto resDto = rechargeRecordResDtoConvertor.po2Dto(po);
        return resDto;
    }

    @Override
    public RechargeRecordResDto selectOne(Map<String, Object> params) {
        QueryWrapper queryWrapper = QueryParamUtils.queryWrapper4eq(RechargeRecordPo::new, params);
        RechargeRecordPo po = rechargeRecordPoService.getOne(queryWrapper);
        RechargeRecordResDto resDto = rechargeRecordResDtoConvertor.po2Dto(po);
        return resDto;
    }

    @Override
    public boolean updateProps(Long id, Map<String, Object> params) {
        UpdateWrapper<RechargeRecordPo> updateWrapper = QueryParamUtils.updateWrapper4Map(RechargeRecordPo::new, id, params);
        return rechargeRecordPoService.update(new RechargeRecordPo(), updateWrapper);
        }

    @Override
    public boolean updateProps(Long id, RechargeRecordReqDto reqDto) {
        RechargeRecordPo po = rechargeRecordReqDtoConvertor.dto2Po(reqDto);
        po.setId(id);
        return rechargeRecordPoService.updateById(po);
    }


    @Override
    public boolean updateAllProps(Long id, RechargeRecordReqDto reqDto) {
        UpdateWrapper<RechargeRecordPo> updateWrapper = Wrappers.update();
        updateWrapper.eq("id", id);
        Field[] fields = RechargeRecordReqDto.class.getDeclaredFields();
        Arrays.stream(fields).filter(f -> !Modifier.isStatic(f.getModifiers())).forEach(field -> {
        try {
        PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), RechargeRecordReqDto.class);
        Method getMethod = propertyDescriptor.getReadMethod();
        String fileNameCamel = getMethod.getName().substring(3);
        String fileNameUnderline = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fileNameCamel);
        updateWrapper.set(fileNameUnderline, getMethod.invoke(reqDto));
        } catch (Exception ex) {
        log.warn("属性不存在get方法："+field.getName(),ex);
        }
        });
        return rechargeRecordPoService.update(new RechargeRecordPo(), updateWrapper);
    }

}