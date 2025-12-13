package com.nova.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.ext.BasePo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.time.*;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "classification")
public class ClassificationPo extends BasePo<ClassificationPo> implements Serializable{

    private static final long serialVersionUID = 1L;

    public ClassificationPo(){

    }
    public ClassificationPo(Long id){
        this.id = id;
    }

    /**
     * 分类 VARCHAR
     */
    private String name;

    /**
     * 排序 INT
     */
    private Integer sort;

}






