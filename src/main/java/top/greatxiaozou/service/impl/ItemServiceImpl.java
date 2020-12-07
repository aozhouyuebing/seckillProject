package top.greatxiaozou.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.greatxiaozou.dao.ItemDoMapper;
import top.greatxiaozou.dao.ItemStockDoMapper;
import top.greatxiaozou.dao.PromoDoMapper;
import top.greatxiaozou.dao.SequenceDoMapper;
import top.greatxiaozou.dataobject.ItemDo;
import top.greatxiaozou.dataobject.ItemStockDo;
import top.greatxiaozou.dataobject.SequenceDo;
import top.greatxiaozou.error.BusinessException;
import top.greatxiaozou.error.EmBusinessError;
import top.greatxiaozou.service.ItemService;
import top.greatxiaozou.service.PromoService;
import top.greatxiaozou.service.model.ItemModel;
import top.greatxiaozou.service.model.PromoModel;
import top.greatxiaozou.validator.ValidationResult;
import top.greatxiaozou.validator.ValidatorImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private ItemDoMapper itemDoMapper;

    @Autowired
    private ItemStockDoMapper itemStockDoMapper;

    @Autowired
    private PromoService promoService;

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
        List<ItemDo> itemDos = itemDoMapper.listItem();
        List<ItemModel> list = itemDos.stream().map(itemDo -> {
            ItemStockDo itemStockDo = itemStockDoMapper.selectByItemId(itemDo.getId());
            ItemModel itemModel = convertItemModelFromDo(itemDo, itemStockDo);

            return itemModel;
        }).collect(Collectors.toList());
        return list;
    }

    @Override
    public ItemModel getItemById(Integer id) {
        ItemDo itemDo = itemDoMapper.selectByPrimaryKey(id);
//        System.out.println(itemDo);

        if (itemDo == null){
            return null;
        }
        //获得库存数量
        ItemStockDo itemStockDo = itemStockDoMapper.selectByItemId(itemDo.getId());

        //dataObject --->model
        ItemModel itemModel = convertItemModelFromDo(itemDo,itemStockDo);

        //获取活动商品信息
        PromoModel promoModel = promoService.getPromoByItemId(itemModel.getId());
        System.out.println(promoModel);

        //将活动聚合到itemModel中
        if (promoModel != null && promoModel.getStatus().intValue() != 3){
            itemModel.setPromoModel(promoModel);
        }



        return itemModel;
    }

    //减库存的方法
    @Override
    @Transactional
    public boolean decreaseStock(Integer itemId, Integer amount) throws BusinessException {
        int affectedRow = itemStockDoMapper.decreaseStock(itemId, amount);
        if (affectedRow > 0){
            //更新库存成功
            return true;
        }else {
            //更新库存失败
            return false;
        }
                
    }

    @Override
    @Transactional
    public void increaseSales(Integer itemId, Integer amount) throws BusinessException {
        itemDoMapper.increaseSales(itemId,amount);
    }

    //========================convert方法======================//
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
        itemModel.setStock(itemStockDo.getStock());
        return itemModel;
    }

    //==========生成订单号方法===========//


}
