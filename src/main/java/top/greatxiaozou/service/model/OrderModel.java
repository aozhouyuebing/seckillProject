package top.greatxiaozou.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

//用户下单的交易模型
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderModel {

    //订单id
    private String id;

    //下单的用户id
    private Integer userId;

    //下单的物品id
    private Integer itemId;

    //购买商品单价
    private BigDecimal price;

    //购买的数量
    private Integer amount;

    //购买的金额
    private BigDecimal orderPrice;
}
