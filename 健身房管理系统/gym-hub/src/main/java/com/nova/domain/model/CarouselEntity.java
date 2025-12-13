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
public class CarouselEntity {


        /**
         * 主键 INT
         */
        private Integer id;
        /**
         * 轮播图名称 VARCHAR
         */
        private String name;
        /**
         * 图片 VARCHAR
         */
        private String url;
        /**
         * 排序 INT
         */
        private Integer sort;



}






