package top.greatxiaozou.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.greatxiaozou.error.BusinessException;
import top.greatxiaozou.error.EmBusinessError;
import top.greatxiaozou.response.CommonReturnType;
import top.greatxiaozou.service.OrderService;
import top.greatxiaozou.service.model.OrderModel;
import top.greatxiaozou.service.model.UserModel;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = {"*"},allowCredentials = "true")
@Controller("order")
@RequestMapping("/order")
public class OrderController extends BaseController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    //用户下单接口
    @ResponseBody
    @RequestMapping(method = {RequestMethod.POST},value = "/createorder",consumes = {CONTENT_TYPE_FORMED})
    public CommonReturnType createOrder(@RequestParam("itemId")Integer itemId,
                                        @RequestParam("amount")Integer amount,
                                        @RequestParam(value = "promoId",required = false)Integer promoId) throws BusinessException {
        //获取用户登录信息
        Boolean isLogin = (Boolean) httpServletRequest.getSession().getAttribute("IS_LOGIN");
        if (isLogin == null || isLogin == false ){
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN,"用户未登录！不能下单");
        }

        UserModel userModel = (UserModel) httpServletRequest.getSession().getAttribute("LOGIN_USER");

        OrderModel orderModel = orderService.createOrder(userModel.getId(), promoId,itemId, amount);

        return CommonReturnType.create(null);
    }
}
