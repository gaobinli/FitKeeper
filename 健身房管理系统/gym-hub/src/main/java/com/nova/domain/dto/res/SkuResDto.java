package com.nova.domain.dto.res;

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
public class SkuResDto implements Serializable {

    private static final long serialVersionUID = 1L;

/**
 * 主键 INT
 */
@ApiModelProperty(value = "主键")
@JsonProperty(index = 0)

    private Integer id;
/**
 * 名称 VARCHAR
 */
@ApiModelProperty(value = "名称")
@JsonProperty(index = 10)

    private String name;
/**
 * 分类 INT
 */
@ApiModelProperty(value = "分类")
@JsonProperty(index = 20)
    private Long categoryId;
/**
 * 封面 VARCHAR
 */
@ApiModelProperty(value = "封面")
@JsonProperty(index = 30)

    private String url;
/**
 * 价格 DECIMAL
 */
@ApiModelProperty(value = "价格")
@JsonProperty(index = 40)

    private Long price;
/**
 * 简单描述 VARCHAR
 */
@ApiModelProperty(value = "简单描述")
@JsonProperty(index = 50)

    private String simpleDesc;
/**
 * 库存量 INT
 */
@ApiModelProperty(value = "库存量")
@JsonProperty(index = 60)

    private Integer inventory;
/**
 * 课程内容 TEXT
 */
@ApiModelProperty(value = "课程内容")
@JsonProperty(index = 70)

    private String content;
/**
 * 课程介绍 TEXT
 */
@ApiModelProperty(value = "课程介绍")
@JsonProperty(index = 80)

    private String detail;
/**
 * 创建人 VARCHAR
 */
@ApiModelProperty(value = "创建人")
@JsonProperty(index = 100)

    private String createUser;
/**
 * 创建时间 DATETIME
 */
@ApiModelProperty(value = "创建时间")
@JsonProperty(index = 110)
    private LocalDateTime createTime;
/**
 * 修改人 VARCHAR
 */
@ApiModelProperty(value = "修改人")
@JsonProperty(index = 120)

    private String modifyUser;
/**
 * 修改时间 DATETIME
 */
@ApiModelProperty(value = "修改时间")
@JsonProperty(index = 130)
    private LocalDateTime modifyTime;



}






