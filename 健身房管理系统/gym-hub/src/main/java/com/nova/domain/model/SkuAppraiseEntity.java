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
public class SkuAppraiseEntity {


        /**
         * 主键 INT
         */
        private Integer id;
        /**
         * 课程 INT
         */
        private Long skuId;
        /**
         * 用户 INT
         */
        private Long userId;
        /**
         * 评价内容 TEXT
         */
        private String content;
        /**
         * 学习体验评分 INT
         */
        private Integer experienceAppraise;
        /**
         * 课程评分 INT
         */
        private Integer skuAppraise;
        /**
         * 订单编号 VARCHAR
         */
        private String orderNo;



}






