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
@TableName(value = "post")
public class PostPo extends BasePo<PostPo> implements Serializable{

    private static final long serialVersionUID = 1L;

    public PostPo(){

    }
    public PostPo(Long id){
        this.id = id;
    }

    /**
     * 标题 VARCHAR
     */
    private String title;

    /**
     * 内容 TEXT
     */
    private String content;

    /**
     * 用户 INT
     */
    private Long userId;

    /**
     * 是否热门，1 热门 INT
     */
    private Integer excellent;

}






