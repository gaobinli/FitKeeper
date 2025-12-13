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
public class CourseReserveEntity {


        /**
         *  INT
         */
        private Integer id;
        /**
         * 用户 INT
         */
        private Long userId;
        /**
         * 课程 INT
         */
        private Long courseId;
        /**
         * 价格 INT
         */
        private Integer price;
        /**
         * 审核描述 VARCHAR
         */
        private String auditDesc;
        /**
         * 预约状态：1 待审核、2 审核驳回、3 审核通过 INT
         */
        private Integer auditStatus;



}






