package com.nova.domain.dto.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Validated
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel
public class CourseReserveResDto implements Serializable {

    private static final long serialVersionUID = 1L;

/**
 *  INT
 */
@ApiModelProperty(value = "")
@JsonProperty(index = 0)

    private Integer id;
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
/**
 * 创建人 VARCHAR
 */
@ApiModelProperty(value = "创建人")
@JsonProperty(index = 70)

    private String createUser;
/**
 * 创建时间 DATETIME
 */
@ApiModelProperty(value = "创建时间")
@JsonProperty(index = 80)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
/**
 * 修改人 VARCHAR
 */
@ApiModelProperty(value = "修改人")
@JsonProperty(index = 90)

    private String modifyUser;
/**
 * 修改时间 DATETIME
 */
@ApiModelProperty(value = "修改时间")
@JsonProperty(index = 100)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyTime;



}






