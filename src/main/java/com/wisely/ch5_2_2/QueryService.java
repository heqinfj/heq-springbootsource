package com.wisely.ch5_2_2;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: HeQin
 * @Date: 2018/10/26 10:50
 * @Description:
 */
@Service
public class QueryService {
    /**
     * 请求查询多少就返回多少ID，但执行的时间不同奇偶数分别睡眠200或400毫秒;
     *
     * @param itemId
     * @return
     */
    public List<ItemInfoMode> queryItemById(Integer itemId) {
        List<ItemInfoMode> list = new ArrayList<>();
        ItemInfoMode itemInfoMode = new ItemInfoMode();
        if (itemId % 2 == 1) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        itemInfoMode.setNum("商品ID：" + itemId);
        list.add(itemInfoMode);
        return list;
    }
}
