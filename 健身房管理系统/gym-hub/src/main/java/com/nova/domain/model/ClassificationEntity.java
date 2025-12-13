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
public class ClassificationEntity {


        /**
         * 主键 INT
         */
        private Integer id;
        /**
         * 分类 VARCHAR
         */
        private String name;
        /**
         * 排序 INT
         */
        private Integer sort;



}






