package top.greatxiaozou.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.greatxiaozou.dao.ItemDoMapper;
import top.greatxiaozou.dao.ItemStockDoMapper;
import top.greatxiaozou.dataobject.ItemDo;
import top.greatxiaozou.dataobject.ItemStockDo;
import top.greatxiaozou.error.BusinessException;
import top.greatxiaozou.error.EmBusinessError;
import top.greatxiaozou.service.ItemService;
import top.greatxiaozou.service.model.ItemModel;
import top.greatxiaozou.validator.ValidationResult;
import top.greatxiaozou.validator.ValidatorImpl;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private ItemDoMapper itemDoMapper;

    @Autowired
    private ItemStockDoMapper itemStockDoMapper;

    @Override
    @Transactional
    public ItemModel createItem(ItemModel itemModel) throws BusinessException {
        //校验入参
        ValidationResult result = validator.validate(itemModel);
        if (result.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }

        //转化--->dataobject
        ItemDo itemDo = convertItemDoFromModel(itemModel);
        itemDoMapper.insertSelective(itemDo);

        itemModel.setId(itemDo.getId());
        ItemStockDo stockDo = convertItemStockDoFromModel(itemModel);

        itemStockDoMapper.insertSelective(stockDo);

        //返回写入完成的对象
        return this.getItemById(itemModel.getId());
    }

    @Override
    public List<ItemModel> listItem() {
        return null;
    }

    @Override
    public ItemModel getItemById(Integer id) {
        ItemDo itemDo = itemDoMapper.selectByPrimaryKey(id);
        if (itemDo == null){
            return null;
        }
        //获得库存数量
        ItemStockDo itemStockDo = itemStockDoMapper.selectByItemId(itemDo.getId());

        //dataObject --->model

        ItemModel itemModel = convertItemModelFromDo(itemDo,itemStockDo);

        return itemModel;
    }


    //将model对象转化为itemdo对象
    public ItemDo convertItemDoFromModel(ItemModel itemModel){
        if (itemModel==null) return null;

        ItemDo itemDo = new ItemDo();
        BeanUtils.copyProperties(itemModel,itemDo);
        itemDo.setPrice(itemModel.getPrice().doubleValue());

        return itemDo;
    }

    //将itemModel转化为ItemStockDo对象
    public ItemStockDo convertItemStockDoFromModel(ItemModel itemModel){
        if (itemModel == null ) return null;

        ItemStockDo itemStockDo= new ItemStockDo();
        itemStockDo.setItemId(itemModel.getId());
        itemStockDo.setStock(itemModel.getStock());

        return itemStockDo;
    }

    //将do对象转换为itemModel对象
    public ItemModel convertItemModelFromDo(ItemDo itemDo,ItemStockDo itemStockDo){
        if (itemDo == null || itemStockDo == null) return null;

        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(itemDo,itemModel);
        itemModel.setPrice(new BigDecimal(itemDo.getPrice()));
        BeanUtils.copyProperties(itemStockDo,itemModel);

        return itemModel;
    }
}
