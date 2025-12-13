package com.nova.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
public class OrderEntity {


        /**
         *  INT
         */
        private Integer id;
        /**
         * 用户编码 INT
         */
        private Integer userId;
        /**
         * 订单编号 VARCHAR
         */
        private String orderNo;

        private Integer status;

        private LocalDateTime sendTime;

        private LocalDateTime receiveTime;
}






