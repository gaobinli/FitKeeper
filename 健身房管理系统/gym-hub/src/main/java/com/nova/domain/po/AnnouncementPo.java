package com.nova.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.ext.BasePo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "announcement")
public class AnnouncementPo extends BasePo<AnnouncementPo> implements Serializable{

    private static final long serialVersionUID = 1L;

    public AnnouncementPo(){

    }
    public AnnouncementPo(Long id){
        this.id = id;
    }

    /**
     * 标题 VARCHAR
     */
    private String title;

    /**
     * 类型：1 系统资讯、2 系统公告 INT
     */
    private Integer type;

    /**
     * 内容 TEXT
     */
    private String content;

    private Long coachId;
}






