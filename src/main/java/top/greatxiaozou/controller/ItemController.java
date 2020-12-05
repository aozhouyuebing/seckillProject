package top.greatxiaozou.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.greatxiaozou.controller.viewobject.ItemVO;
import top.greatxiaozou.error.BusinessException;
import top.greatxiaozou.response.CommonReturnType;
import top.greatxiaozou.service.ItemService;
import top.greatxiaozou.service.model.ItemModel;

import java.math.BigDecimal;

@Controller
@RequestMapping("/item")
@CrossOrigin(origins = {"*"},allowCredentials = "true")
public class ItemController extends BaseController  {
    @Autowired
    private ItemService itemService;

    //创建商品的接口
    @ResponseBody
    @RequestMapping(method = {RequestMethod.POST},value = "/create",consumes = {CONTENT_TYPE_FORMED})
    public CommonReturnType creatItem(@RequestParam(name = "title")String title,
                                      @RequestParam(name = "description")String description,
                                      @RequestParam(name = "price")BigDecimal price,
                                      @RequestParam(name = "stock")Integer stock,
                                      @RequestParam(name = "imgUrl")String imgUrl) throws BusinessException {
        //封装service请求用来创建商品
        ItemModel itemModel = new ItemModel();
        itemModel.setTitle(title);
        itemModel.setPrice(price);
        itemModel.setDescription(description);
        itemModel.setStock(stock);
        itemModel.setImgUrl(imgUrl);

        ItemModel item = itemService.createItem(itemModel);
        ItemVO itemVO = convertVOFromModel(item);

        return CommonReturnType.create(itemVO);

    }

    private ItemVO convertVOFromModel(ItemModel itemModel){
        if (itemModel == null){
            return null;
        }
        ItemVO itemVO = new ItemVO();
        BeanUtils.copyProperties(itemModel,itemVO);
        return itemVO;
    }
}
