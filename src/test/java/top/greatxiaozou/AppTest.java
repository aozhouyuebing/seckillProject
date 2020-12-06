package top.greatxiaozou;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.greatxiaozou.dao.ItemDoMapper;
import top.greatxiaozou.dao.SequenceDoMapper;
import top.greatxiaozou.dataobject.ItemDo;
import top.greatxiaozou.dataobject.SequenceDo;
import top.greatxiaozou.service.ItemService;
import top.greatxiaozou.service.model.ItemModel;

/**
 * Unit test for simple App.
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class AppTest {
    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemDoMapper itemDoMapper;

    @Autowired
    private SequenceDoMapper sequenceDoMapper;

    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }


    @Test
    public void test01(){
//        ItemModel itemModel = itemService.getItemById(2);
        ItemDo itemDo = itemDoMapper.selectByPrimaryKey(9);
        System.out.println(itemDo);
    }

    @Test
    public void test02(){
        SequenceDo sequenceDo = sequenceDoMapper.getSequenceByName("order_info");
        System.out.println(sequenceDo);
    }
}
