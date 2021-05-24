package com.dzt.service;

import com.dzt.bean.selectBean.Publisher;
import com.dzt.entity.Moment;
import com.dzt.entity.User;

import java.util.List;

/**
 * @author Z
 */
public interface MomentService {
    /**
     * 增加一个朋友圈并设置部分好友不可看
     * @param s
     * @param friends
     * @param user
     */
    void addMoment(String s, String[] friends, User user);


    /**
     * 删除一条朋友圈
     * @param id
     */
    void remove(Integer id);

    /**
     * 根据id得到一条朋友圈
     * @param id
     */
    Moment get(Integer id);


}
