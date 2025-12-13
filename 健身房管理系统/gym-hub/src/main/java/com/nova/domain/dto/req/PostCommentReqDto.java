package com.nova.domain.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;


@Validated
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel
public class PostCommentReqDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 帖子 INT
     */
    @ApiModelProperty(value = "帖子")
    @JsonProperty(index = 10)
    private Long postId;
    /**
     * 评论内容 TEXT
     */
    @ApiModelProperty(value = "评论内容")
    @JsonProperty(index = 20)


    private String content;
    /**
     * 评论人 INT
     */
    @ApiModelProperty(value = "评论人")
    @JsonProperty(index = 30)
    private Long userId;
}






