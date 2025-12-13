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
public class SkuEntity {


        /**
         * 主键 INT
         */
        private Integer id;
        /**
         * 名称 VARCHAR
         */
        private String name;
        /**
         * 分类 INT
         */
        private Long categoryId;
        /**
         * 封面 VARCHAR
         */
        private String url;
        /**
         * 价格 DECIMAL
         */
        private Long price;
        /**
         * 简单描述 VARCHAR
         */
        private String simpleDesc;
        /**
         * 库存量 INT
         */
        private Integer inventory;
        /**
         * 课程内容 TEXT
         */
        private String content;
        /**
         * 课程介绍 TEXT
         */
        private String detail;



}






