package com.nova.domain.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
public class SysUserEntity {


        /**
         *  INT
         */
        private Long id;
        /**
         * 用户名 VARCHAR
         */
        private String username;
        /**
         * 密码 VARCHAR
         */
        private String password;
        /**
         * 姓名 VARCHAR
         */
        private String name;
        /**
         * 手机号 VARCHAR
         */
        private String phone;
        /**
         * 邮箱 VARCHAR
         */
        private String email;
        /**
         * 账号是否启用，1 启用 BIT
         */
        private Boolean enable;



}






