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
public class CoachEntity {


        /**
         * 主键 INT
         */
        private Integer id;
        /**
         * 姓名 VARCHAR
         */
        private String name;
        /**
         * 账号 VARCHAR
         */
        private String username;
        /**
         * 密码 VARCHAR
         */
        private String password;
        /**
         * 手机号 VARCHAR
         */
        private String phone;
        /**
         * 封面 VARCHAR
         */
        private String url;
        /**
         * 出生日期 DATETIME
         */
        private LocalDateTime birthday;
        /**
         * 地址 VARCHAR
         */
        private String address;
        /**
         * 性别，1 男 2 女 TINYINT
         */
        private Integer sex;
        /**
         * 是否启用，1启用 TINYINT
         */
        private Boolean enable;

        private String introduceContent;

}






