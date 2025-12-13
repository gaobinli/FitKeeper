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
@TableName(value = "recharge_record")
public class RechargeRecordPo extends BasePo<RechargeRecordPo> implements Serializable{

    private static final long serialVersionUID = 1L;

    public RechargeRecordPo(){

    }
    public RechargeRecordPo(Long id){
        this.id = id;
    }

    /**
     * 用户 INT
     */
    private Long userId;

    /**
     * 备注 VARCHAR
     */
    private String remark;

    /**
     * 金额 INT
     */
    private Integer money;

    /**
     * 充值渠道 TINYINT
     */
    private Integer channel;

}






