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
public class PostEntity {


        /**
         * 主键 INT
         */
        private Long id;
        /**
         * 标题 VARCHAR
         */
        private String title;
        /**
         * 内容 TEXT
         */
        private String content;
        /**
         * 用户 INT
         */
        private Long userId;
        /**
         * 是否热门，1 热门 INT
         */
        private Integer excellent;



}






