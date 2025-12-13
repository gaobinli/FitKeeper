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
public class CourseReserveReqDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户 INT
     */
    @ApiModelProperty(value = "用户")
    @JsonProperty(index = 10)
    private Long userId;
    /**
     * 课程 INT
     */
    @ApiModelProperty(value = "课程")
    @JsonProperty(index = 20)
    private Long courseId;
    /**
     * 价格 INT
     */
    @ApiModelProperty(value = "价格")
    @JsonProperty(index = 30)


    private Integer price;
    /**
     * 审核描述 VARCHAR
     */
    @ApiModelProperty(value = "审核描述")
    @JsonProperty(index = 40)


    private String auditDesc;
    /**
     * 预约状态：1 待审核、2 审核驳回、3 审核通过 INT
     */
    @ApiModelProperty(value = "预约状态：1 待审核、2 审核驳回、3 审核通过")
    @JsonProperty(index = 50)


    private Integer auditStatus;

}






