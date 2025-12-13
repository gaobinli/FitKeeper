package com.nova.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nova.common.util.ConditionUtil;
import com.nova.common.convertor.SysUserResDtoConvertor;
import com.nova.domain.model.LoginUserEntity;
import com.nova.domain.model.SysUserEntity;
import com.nova.domain.po.CoachPo;
import com.nova.domain.po.SysUserPo;
import com.nova.domain.po.UserPo;
import com.nova.mapper.CoachMapper;
import com.nova.mapper.SysUserMapper;
import com.nova.mapper.UserMapper;
import com.nova.service.SysMenuService;
import com.nova.service.SysPermissionService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


@Data
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SysMenuService sysMenuService;
    private final SysUserMapper sysUserMapper;
    private final UserMapper userMapper;
    private final SysPermissionService sysPermissionService;
    private final CoachMapper coachMapper;

    @Autowired
    private SysUserResDtoConvertor sysUserResDtoConvertor;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        SysUserEntity sysUserEntity = new SysUserEntity();;
        if (username.contains("admin")) {
            if (username.split("-").length > 2 && "2".equals(username.split("-")[1])) {
                username = username.split("-")[2];
                CoachPo coachPo = coachMapper.selectOne(Wrappers.<CoachPo>lambdaQuery().eq(CoachPo::getUsername, username));
                ConditionUtil.checkNotNull(coachPo, "亲！登录用户：" + username + " 不存在");
                sysUserEntity.setName(coachPo.getName());
                sysUserEntity.setPassword(coachPo.getPassword());
                sysUserEntity.setUsername(coachPo.getUsername());
                sysUserEntity.setEnable(coachPo.getEnable());
                sysUserEntity.setId(coachPo.getId());
            } else {
                if (username.split("-").length > 2) {
                    username = username.split("-")[2];
                } else {
                    username = username.split("-")[1];
                }
                SysUserPo sysUserPo = sysUserMapper.selectOne(Wrappers.<SysUserPo>lambdaQuery().eq(SysUserPo::getUsername, username));
                sysUserEntity = sysUserResDtoConvertor.po2Entity(sysUserPo);
            }
            ConditionUtil.checkNotNull(sysUserEntity, "亲！登录用户：" + username + " 不存在");
            ConditionUtil.checkArgument(sysUserEntity.getEnable(), "亲！账号未审核通过");
        } else {
            username = username.split("-")[1];
            UserPo userPo = userMapper.selectOne(Wrappers.<UserPo>lambdaQuery().eq(UserPo::getPhone, username));
            ConditionUtil.checkNotNull(userPo, "亲！登录用户：" + username + " 不存在");
            sysUserEntity.setName(userPo.getName());
            sysUserEntity.setPassword(userPo.getPassword());
            sysUserEntity.setUsername(userPo.getPhone());
            sysUserEntity.setEnable(userPo.getEnable());
            sysUserEntity.setId(userPo.getId());
        }

        ConditionUtil.checkNotNull(sysUserEntity, "亲！登录用户：" + username + " 不存在");
        ConditionUtil.checkArgument(sysUserEntity.getEnable(), "亲！账号已被禁用");

        return createLoginUser(sysUserEntity);
    }

    public UserDetails createLoginUser(SysUserEntity sysUserEntity) {
        Set<String> perms = new HashSet<>();
        perms.add("*:*:*");
//        return new LoginUserEntity(sysUserEntity, sysPermissionService.getMenuPermission(sysUserEntity.getCode()));
        return new LoginUserEntity(sysUserEntity, perms);
    }
}
