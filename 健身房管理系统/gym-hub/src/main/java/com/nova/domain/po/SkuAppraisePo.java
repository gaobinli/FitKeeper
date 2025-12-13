package com.nova.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.ext.BasePo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sku_appraise")
public class SkuAppraisePo extends BasePo<SkuAppraisePo> implements Serializable{

    private static final long serialVersionUID = 1L;

    public SkuAppraisePo(){

    }
    public SkuAppraisePo(Long id){
        this.id = id;
    }

    /**
     * 课程 INT
     */
    private Long skuId;

    /**
     * 用户 INT
     */
    private Long userId;

    /**
     * 评价内容 TEXT
     */
    private String content;

    /**
     * 学习体验评分 INT
     */
    private Integer experienceAppraise;

    /**
     * 课程评分 INT
     */
    private Integer skuAppraise;

    /**
     * 订单编号 VARCHAR
     */
    private String orderNo;

}






