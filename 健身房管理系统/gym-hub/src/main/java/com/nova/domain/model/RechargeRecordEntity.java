package com.nova.domain.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
public class RechargeRecordEntity {


        /**
         * 主键 INT
         */
        private Integer id;
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






