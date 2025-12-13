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
@TableName(value = "course_reserve")
public class CourseReservePo extends BasePo<CourseReservePo> implements Serializable{

    private static final long serialVersionUID = 1L;

    public CourseReservePo(){

    }
    public CourseReservePo(Long id){
        this.id = id;
    }

    /**
     * 用户 INT
     */
    private Long userId;

    /**
     * 课程 INT
     */
    private Long courseId;

    /**
     * 价格 INT
     */
    private Integer price;

    /**
     * 审核描述 VARCHAR
     */
    private String auditDesc;

    /**
     * 预约状态：1 待审核、2 审核驳回、3 审核通过 INT
     */
    private Integer auditStatus;

}






