package com.nova.domain.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


@Validated
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel
public class OrderReqDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户编码 INT
     */
    @ApiModelProperty(value = "用户手机号")
    @JsonProperty(index = 10)
    private String userId;
    /**
     * 订单编号 VARCHAR
     */
    @ApiModelProperty(value = "订单编号")
    @JsonProperty(index = 20)
    private String orderNo;

    private List<OrderDetailReqDto> orderDetails;

    private Integer status;

    private LocalDateTime sendTime;

    private LocalDateTime receiveTime;
}






