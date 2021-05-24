package com.dzt.dao;

import com.dzt.bean.selectBean.LongCount;
import com.dzt.core.BaseDao;
import com.dzt.entity.MomentLike;

import java.util.List;
import java.util.Map;

/**
 * @author Z
 */
public interface MomentLikeDao extends BaseDao<MomentLike> {
    /**
     * 删除朋友圈对应的信息
     * @param id
     */
    void removeByMomentId(Integer id);

    /**
     * 得到朋友圈的点赞数目
     * @param id
     * @return
     */
    List<LongCount> getLikeCount(Integer id);

    /**
     * 用户是否已经点赞了该朋友圈
     * @param momentId
     * @param userId
     * @return
     */
    List<LongCount> isLike(Integer momentId, Integer userId);

    /**
     * 将用户的对某个朋友圈的点赞删除
     * @param momentId
     * @param userId
     */
    void removeLike(Integer momentId, Integer userId);
}
