package com.dzt.service.impl;

import com.dzt.bean.selectBean.LongCount;
import com.dzt.core.DaoFacory;
import com.dzt.dao.MomentDao;
import com.dzt.dao.MomentLikeDao;
import com.dzt.entity.Moment;
import com.dzt.entity.MomentLike;
import com.dzt.service.MomentLikeService;
import com.sun.javafx.iio.common.SmoothMinifier;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

/**
 * @author Z
 */
public class MomentLikeServiceImpl implements MomentLikeService {
    private MomentLikeDao momentLikeDao = DaoFacory.getWebChatDao(MomentLikeDao.class);
    @Override
    public void removeByMomentId(Integer id) {
        momentLikeDao.removeByMomentId(id);
    }

    @Override
    public Integer getLikeCount(Integer id) {
        LongCount likeCount = momentLikeDao.getLikeCount(id).get(0);
        return likeCount.getCount().intValue();
    }

    @Override
    public boolean isLike(Integer momentId, Integer userId){
        List<LongCount> like = momentLikeDao.isLike(momentId, userId);
        if (like.get(0).getCount() == 0){
            return false;
        } else {
            return true;
        }

    }

    @Override
    public void addLike(Integer momentId, Integer userId){
        momentLikeDao.add(new MomentLike(momentId,userId));
    }

    @Override
    public void removeLike(Integer momentId, Integer userId){
        momentLikeDao.removeLike(momentId,userId);
    }
}
