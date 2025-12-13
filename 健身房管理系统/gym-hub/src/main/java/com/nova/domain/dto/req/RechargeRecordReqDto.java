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
public class RechargeRecordReqDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户 INT
     */
    @ApiModelProperty(value = "用户")
    @JsonProperty(index = 10)
    private Long userId;
    /**
     * 备注 VARCHAR
     */
    @ApiModelProperty(value = "备注")
    @JsonProperty(index = 20)


    private String remark;
    /**
     * 金额 INT
     */
    @ApiModelProperty(value = "金额")
    @JsonProperty(index = 30)


    private Integer money;
    /**
     * 充值渠道 TINYINT
     */
    @ApiModelProperty(value = "充值渠道")
    @JsonProperty(index = 40)


    private Integer channel;

}






