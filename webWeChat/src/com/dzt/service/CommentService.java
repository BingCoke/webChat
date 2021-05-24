package com.dzt.service;

import com.dzt.entity.Comment;

/**
 * @author Z
 */
public interface CommentService {
    /**
     * 删除朋友圈对应的评论
     * @param id
     */
    void removeByMomentId(Integer id);

    /**
     * 增加一条评论
     * @param comment
     */
    void add(Comment comment);

    /**
     * 得到用户的评论
     * @param momentId
     * @return
     */
    String getMomentComment(Integer momentId);

    /**
     * 删除一条
     * @param id
     */
    void remove(Integer id);
}
