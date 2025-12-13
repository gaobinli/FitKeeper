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
public class PostCommentEntity {


        /**
         * 主键 INT
         */
        private Long id;
        /**
         * 帖子 INT
         */
        private Long postId;
        /**
         * 评论内容 TEXT
         */
        private String content;
        /**
         * 评论人 INT
         */
        private Long userId;



}






