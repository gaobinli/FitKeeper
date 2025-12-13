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
public class CourseResDto implements Serializable {

    private static final long serialVersionUID = 1L;

/**
 * 主键 INT
 */
@ApiModelProperty(value = "主键")
@JsonProperty(index = 0)

    private Integer id;
/**
 * 课程名称 VARCHAR
 */
@ApiModelProperty(value = "课程名称")
@JsonProperty(index = 10)

    private String name;
/**
 * 教练 INT
 */
@ApiModelProperty(value = "教练")
@JsonProperty(index = 20)
    private Long coachId;
/**
 * 价格 INT
 */
@ApiModelProperty(value = "价格")
@JsonProperty(index = 30)

    private Integer price;
/**
 * 分类 INT
 */
@ApiModelProperty(value = "分类")
@JsonProperty(index = 40)
    private Long classificationId;
/**
 * 课程封面 VARCHAR
 */
@ApiModelProperty(value = "课程封面")
@JsonProperty(index = 50)

    private String coverUrl;
/**
 * 课程内容 VARCHAR
 */
@ApiModelProperty(value = "课程内容")
@JsonProperty(index = 60)

    private String content;
/**
 * 课程时间 DATETIME
 */
@ApiModelProperty(value = "课程时间")
@JsonProperty(index = 70)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime courseTime;
/**
 * 限制人数 INT
 */
@ApiModelProperty(value = "限制人数")
@JsonProperty(index = 80)

    private Integer limitNumber;
/**
 * 审核描述 VARCHAR
 */
@ApiModelProperty(value = "审核描述")
@JsonProperty(index = 90)

    private String auditDesc;
/**
 * 审核状态：1 待审核、2 审核驳回、3 审核通过 INT
 */
@ApiModelProperty(value = "审核状态：1 待审核、2 审核驳回、3 审核通过")
@JsonProperty(index = 100)

    private Integer auditStatus;
/**
 * 推荐指数 INT
 */
@ApiModelProperty(value = "推荐指数")
@JsonProperty(index = 110)

    private Integer recommendedScore;
/**
 * 创建人 VARCHAR
 */
@ApiModelProperty(value = "创建人")
@JsonProperty(index = 130)

    private String createUser;
/**
 * 创建时间 DATETIME
 */
@ApiModelProperty(value = "创建时间")
@JsonProperty(index = 140)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
/**
 * 修改人 VARCHAR
 */
@ApiModelProperty(value = "修改人")
@JsonProperty(index = 150)

    private String modifyUser;
/**
 * 修改时间 DATETIME
 */
@ApiModelProperty(value = "修改时间")
@JsonProperty(index = 160)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyTime;

    private String simpleDesc;

}






