package com.nova.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.ext.BasePo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "category")
public class CategoryPo extends BasePo<CategoryPo> implements Serializable{

    private static final long serialVersionUID = 1L;

    public CategoryPo(){

    }
    public CategoryPo(Long id){
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






