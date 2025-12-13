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
public class PostResDto implements Serializable {

    private static final long serialVersionUID = 1L;

/**
 * 主键 INT
 */
@ApiModelProperty(value = "主键")
@JsonProperty(index = 0)

    private Long id;
/**
 * 标题 VARCHAR
 */
@ApiModelProperty(value = "标题")
@JsonProperty(index = 10)

    private String title;
/**
 * 内容 TEXT
 */
@ApiModelProperty(value = "内容")
@JsonProperty(index = 20)

    private String content;
/**
 * 用户 INT
 */
@ApiModelProperty(value = "用户")
@JsonProperty(index = 30)
    private Long userId;
/**
 * 是否热门，1 热门 INT
 */
@ApiModelProperty(value = "是否热门，1 热门")
@JsonProperty(index = 40)

    private Integer excellent;
/**
 * 创建人 VARCHAR
 */
@ApiModelProperty(value = "创建人")
@JsonProperty(index = 60)

    private String createUser;
/**
 * 创建时间 DATETIME
 */
@ApiModelProperty(value = "创建时间")
@JsonProperty(index = 70)
    private LocalDateTime createTime;
/**
 * 修改人 VARCHAR
 */
@ApiModelProperty(value = "修改人")
@JsonProperty(index = 80)

    private String modifyUser;
/**
 * 修改时间 DATETIME
 */
@ApiModelProperty(value = "修改时间")
@JsonProperty(index = 90)
    private LocalDateTime modifyTime;



}






