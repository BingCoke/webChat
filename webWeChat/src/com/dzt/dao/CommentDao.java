package com.dzt.dao;

import com.dzt.bean.selectBean.Commenter;
import com.dzt.core.BaseDao;
import com.dzt.entity.Comment;

import java.util.List;

/**
 * @author Z
 */
public interface CommentDao extends BaseDao<Comment> {
    /**
     * 删除朋友圈的相关信息
     * @param id
     */
    void removeByMomentId(Integer id);

    /**
     * 得到一个朋友圈的评论
     * @param momentId
     * @return
     */
    List<Commenter> getMomentComment(Integer momentId);
}
