package com.dzt.dao;

import com.dzt.bean.selectBean.LongCount;
import com.dzt.bean.selectBean.Publisher;
import com.dzt.core.BaseDao;
import com.dzt.entity.Moment;

import java.util.List;

/**
 * @author Z
 */
public interface MomentDao extends BaseDao<Moment> {
    /**
     * 得到用户能看到的朋友圈信息
     * @param id
     * @param id1
     * @param id2
     * @param start
     * @param end
     * @return
     */
    List<Publisher> getFriendsMoment(int id, int id1,int id2,int start,int end);

    /**
     * 得到用户所有朋友圈的条数
     * @param id
     * @param id1
     * @param id2
     * @return
     */
    List<LongCount> getFriendsMomentCount(int id,int id1,int id2);

    /**
     * 得到一个用户的朋友圈
     * @return
     */
    List<Publisher> getUserMoment(int userId,int start,int end);

    /**
     * 得到用户的朋友圈的条数
     * @param id
     * @return
     */
    List<LongCount> getUserMomentCount(int id);
}
