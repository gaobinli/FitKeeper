package com.nova.domain.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class CourseAppraiseReqDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 课程 INT
     */
    @ApiModelProperty(value = "课程")
    @JsonProperty(index = 10)
    private Long courseId;
    /**
     * 用户 INT
     */
    @ApiModelProperty(value = "用户")
    @JsonProperty(index = 20)
    private Long userId;

    /**
     * 评价 TEXT
     */
    @ApiModelProperty(value = "评价")
    @JsonProperty(index = 40)


    private String content;
    /**
     * 评分 INT
     */
    @ApiModelProperty(value = "评分")
    @JsonProperty(index = 50)


    private Integer courseAppraise;

    private String replyContent;
}






