package com.nova.domain.dto.res;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;

@Validated
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel
public class SysMenuMetaResDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String i18nKey;

    private Integer order;

    private String icon;

    private Boolean constant;

    private Boolean hideInMenu;

    private String title;
}






