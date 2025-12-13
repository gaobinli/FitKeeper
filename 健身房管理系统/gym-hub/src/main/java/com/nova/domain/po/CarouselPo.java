package com.nova.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.ext.BasePo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "carousel")
public class CarouselPo extends BasePo<CarouselPo> implements Serializable{

    private static final long serialVersionUID = 1L;

    public CarouselPo(){

    }
    public CarouselPo(Long id){
        this.id = id;
    }

    /**
     * 轮播图名称 VARCHAR
     */
    private String name;

    /**
     * 图片 VARCHAR
     */
    private String url;

    /**
     * 排序 INT
     */
    private Integer sort;

}






