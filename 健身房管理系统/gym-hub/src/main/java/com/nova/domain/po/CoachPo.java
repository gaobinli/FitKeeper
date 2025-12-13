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
@TableName(value = "coach")
public class CoachPo extends BasePo<CoachPo> implements Serializable{

    private static final long serialVersionUID = 1L;

    public CoachPo(){

    }
    public CoachPo(Long id){
        this.id = id;
    }

    /**
     * 姓名 VARCHAR
     */
    private String name;

    /**
     * 账号 VARCHAR
     */
    private String username;

    /**
     * 密码 VARCHAR
     */
    private String password;

    /**
     * 手机号 VARCHAR
     */
    private String phone;

    /**
     * 封面 VARCHAR
     */
    private String url;

    /**
     * 出生日期 DATETIME
     */
    private LocalDateTime birthday;

    /**
     * 地址 VARCHAR
     */
    private String address;

    /**
     * 性别，1 男 2 女 TINYINT
     */
    private Integer sex;

    /**
     * 是否启用，1启用 TINYINT
     */
    private Boolean enable;

    private String introduceContent;
}






