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
public class AnnouncementEntity {


        /**
         * 主键 INT
         */
        private Integer id;
        /**
         * 标题 VARCHAR
         */
        private String title;
        /**
         * 类型：1 系统资讯、2 系统公告 INT
         */
        private Integer type;
        /**
         * 内容 TEXT
         */
        private String content;


        private Long coachId;
}






