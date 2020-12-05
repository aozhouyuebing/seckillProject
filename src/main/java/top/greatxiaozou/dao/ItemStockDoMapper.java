package top.greatxiaozou.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.greatxiaozou.dataobject.ItemStockDo;

@Mapper
@Repository
public interface ItemStockDoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ItemStockDo record);

    int insertSelective(ItemStockDo record);

    ItemStockDo selectByPrimaryKey(Integer id);

    ItemStockDo selectByItemId(Integer itemId);


    int updateByPrimaryKeySelective(ItemStockDo record);

    int updateByPrimaryKey(ItemStockDo record);
}