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
@TableName(value = "course")
public class CoursePo extends BasePo<CoursePo> implements Serializable{

    private static final long serialVersionUID = 1L;

    public CoursePo(){

    }
    public CoursePo(Long id){
        this.id = id;
    }

    /**
     * 课程名称 VARCHAR
     */
    private String name;

    /**
     * 教练 INT
     */
    private Long coachId;

    /**
     * 价格 INT
     */
    private Integer price;

    /**
     * 分类 INT
     */
    private Long classificationId;

    /**
     * 课程封面 VARCHAR
     */
    private String coverUrl;

    /**
     * 课程内容 VARCHAR
     */
    private String content;

    /**
     * 课程时间 DATETIME
     */
    private LocalDateTime courseTime;

    /**
     * 限制人数 INT
     */
    private Integer limitNumber;

    /**
     * 审核描述 VARCHAR
     */
    private String auditDesc;

    /**
     * 审核状态：1 待审核、2 审核驳回、3 审核通过 INT
     */
    private Integer auditStatus;

    /**
     * 推荐指数 INT
     */
    private Integer recommendedScore;

    private String simpleDesc;
}






