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
@TableName(value = "course_appraise")
public class CourseAppraisePo extends BasePo<CourseAppraisePo> implements Serializable{

    private static final long serialVersionUID = 1L;

    public CourseAppraisePo(){

    }
    public CourseAppraisePo(Long id){
        this.id = id;
    }

    /**
     * 课程 INT
     */
    private Long courseId;

    /**
     * 用户 INT
     */
    private Long userId;

    /**
     * 评价 TEXT
     */
    private String content;

    /**
     * 评分 INT
     */
    private Integer courseAppraise;

    private String replyContent;
}






