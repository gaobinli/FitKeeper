package com.nova.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.ext.BasePo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sku")
public class SkuPo extends BasePo<SkuPo> implements Serializable{

    private static final long serialVersionUID = 1L;

    public SkuPo(){

    }
    public SkuPo(Long id){
        this.id = id;
    }

    /**
     * 名称 VARCHAR
     */
    private String name;

    /**
     * 分类 INT
     */
    private Long categoryId;

    /**
     * 封面 VARCHAR
     */
    private String url;

    /**
     * 价格 DECIMAL
     */
    private Long price;

    /**
     * 简单描述 VARCHAR
     */
    private String simpleDesc;

    /**
     * 库存量 INT
     */
    private Integer inventory;

    /**
     * 课程内容 TEXT
     */
    private String content;

    /**
     * 课程介绍 TEXT
     */
    private String detail;

}






