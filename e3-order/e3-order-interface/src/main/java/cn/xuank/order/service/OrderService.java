package cn.xuank.order.service;

import cn.xuank.common.utils.E3Result;
import cn.xuank.order.pojo.OrderInfo;

public interface OrderService {

	E3Result createOrder(OrderInfo orderInfo);
}
