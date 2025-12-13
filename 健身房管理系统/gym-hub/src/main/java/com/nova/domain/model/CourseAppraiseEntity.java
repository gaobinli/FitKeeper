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
public class CourseAppraiseEntity {


        /**
         * 主键 INT
         */
        private Integer id;
        /**
         * 课程 INT
         */
        private Long courseId;
        /**
         * 用户 INT
         */
        private Long userId;

        /**
         * 评价 TEXT
         */
        private String content;
        /**
         * 评分 INT
         */
        private Integer courseAppraise;

        private String replyContent;

}






