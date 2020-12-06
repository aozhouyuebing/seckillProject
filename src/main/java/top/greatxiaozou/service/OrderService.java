package top.greatxiaozou.service;

import org.springframework.stereotype.Service;
import top.greatxiaozou.error.BusinessException;
import top.greatxiaozou.service.model.OrderModel;

@Service
public interface OrderService {
    OrderModel createOrder(Integer userId,Integer itemId,Integer amount) throws BusinessException;
}
