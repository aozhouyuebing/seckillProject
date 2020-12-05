package top.greatxiaozou.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.greatxiaozou.dataobject.ItemDo;

@Repository
@Mapper
public interface ItemDoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ItemDo record);

    int insertSelective(ItemDo record);

    ItemDo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ItemDo record);

    int updateByPrimaryKey(ItemDo record);
}