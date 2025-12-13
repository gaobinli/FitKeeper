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
@TableName(value = "post_comment")
public class PostCommentPo extends BasePo<PostCommentPo> implements Serializable{

    private static final long serialVersionUID = 1L;

    public PostCommentPo(){

    }
    public PostCommentPo(Long id){
        this.id = id;
    }

    /**
     * 帖子 INT
     */
    private Long postId;

    /**
     * 评论内容 TEXT
     */
    private String content;

    /**
     * 评论人 INT
     */
    private Long userId;

}






