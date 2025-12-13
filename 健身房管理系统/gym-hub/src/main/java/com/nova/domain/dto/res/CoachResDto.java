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
public class CoachResDto implements Serializable {

    private static final long serialVersionUID = 1L;

/**
 * 主键 INT
 */
@ApiModelProperty(value = "主键")
@JsonProperty(index = 0)

    private Integer id;
/**
 * 姓名 VARCHAR
 */
@ApiModelProperty(value = "姓名")
@JsonProperty(index = 10)

    private String name;
/**
 * 账号 VARCHAR
 */
@ApiModelProperty(value = "账号")
@JsonProperty(index = 20)

    private String username;

/**
 * 手机号 VARCHAR
 */
@ApiModelProperty(value = "手机号")
@JsonProperty(index = 40)

    private String phone;
/**
 * 封面 VARCHAR
 */
@ApiModelProperty(value = "封面")
@JsonProperty(index = 50)

    private String url;
/**
 * 出生日期 DATETIME
 */
@ApiModelProperty(value = "出生日期")
@JsonProperty(index = 60)
@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime birthday;
/**
 * 地址 VARCHAR
 */
@ApiModelProperty(value = "地址")
@JsonProperty(index = 70)

    private String address;
/**
 * 性别，1 男 2 女 TINYINT
 */
@ApiModelProperty(value = "性别，1 男 2 女")
@JsonProperty(index = 80)

    private Integer sex;
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
/**
 * 是否启用，1启用 TINYINT
 */
@ApiModelProperty(value = "是否启用，1启用")
@JsonProperty(index = 140)

    private Boolean enable;

    private String introduceContent;

}






