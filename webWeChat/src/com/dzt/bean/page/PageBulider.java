package com.dzt.bean.page;

import com.dzt.entity.User;

import java.util.List;
import java.util.Map;

/**
 * 页建造
 * @param <T>
 */
public abstract class PageBulider<T> {
    private Page page;

    public PageBulider() {
        this.page =  new Page<>();
    }

    /**
     * 设置总共的数据的数目
     * @param map
     * @return
     */
    abstract T buildTotalDataSize(Map map);

    /**
     * 设置一页数据量
     * @param i
     * @return
     */
    public T buildDataSize(int i){
        page.setDataSize(i);
        page.setTotalPages((page.getTotalDateSize() % page.getDataSize() == 0 ? page.getTotalDateSize() / page.getDataSize() : page.getTotalDateSize()/page.getDataSize() + 1));
        if (page.getTotalPages() == 0){
            page.setTotalPages(1);
        }
        return (T)this;
    };

    /**
     * 设置当前页
     * @param i
     * @return
     */
     public T buildCurrentPage(int i){
         //后台二次检验
        if(i >= page.getTotalPages()){
            i = page.getTotalPages();
        } else if(i <= 1){
            i = 1;
        }
        page.setCurrentPage(i);
        return (T)this;
    };

    /**
     * 设置当前页的数据
     * @param map
     * @return
     */
    abstract T buildData(Map map);


    public Page getPage(){
        return page;
    }
}
