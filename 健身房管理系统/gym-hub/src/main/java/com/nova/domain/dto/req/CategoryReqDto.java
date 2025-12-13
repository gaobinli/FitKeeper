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
public class CategoryReqDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 分类 VARCHAR
     */
    @ApiModelProperty(value = "分类")
    @JsonProperty(index = 10)


    private String name;
    /**
     * 排序 INT
     */
    @ApiModelProperty(value = "排序")
    @JsonProperty(index = 20)


    private Integer sort;

}






