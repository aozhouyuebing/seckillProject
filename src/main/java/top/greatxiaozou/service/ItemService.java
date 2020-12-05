package top.greatxiaozou.service;

import org.springframework.stereotype.Service;
import top.greatxiaozou.error.BusinessException;
import top.greatxiaozou.service.model.ItemModel;

import java.util.List;
@Service
public interface ItemService {
    //创建商品
    ItemModel createItem(ItemModel itemModel) throws BusinessException;

    //商品列表浏览
    List<ItemModel> listItem();

    //商品详情浏览
    ItemModel getItemById(Integer id);
}
