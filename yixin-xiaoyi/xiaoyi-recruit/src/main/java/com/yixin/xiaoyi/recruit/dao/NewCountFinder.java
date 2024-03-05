package com.yixin.xiaoyi.recruit.dao;

import com.yixin.xiaoyi.recruit.dao.mapper.NewCountMapper;
import com.yixin.xiaoyi.recruit.entity.NewCount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * @author: huangzexin
 * @date: 2023/11/10 13:19
 */
@Repository
@RequiredArgsConstructor
public class NewCountFinder {

    private final NewCountMapper newCountMapper;


    /**
     * 更新计数
     * @param newCount
     * @return
     */
    public int updateCountById(NewCount newCount){
        return newCountMapper.updateById(newCount);
    }

    /**
     * 获取当前计数
     * @return
     */
    public NewCount selectNewCount(){
        return newCountMapper.selectList().stream().findFirst().get();
    }
}
