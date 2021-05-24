package com.dzt.service.impl;

import com.dzt.bean.MyResult;
import com.dzt.bean.selectBean.Commenter;
import com.dzt.controller.MyServlet;
import com.dzt.core.DaoFacory;
import com.dzt.dao.CommentDao;
import com.dzt.entity.Comment;
import com.dzt.service.CommentService;

import java.util.List;

/**
 * @author Z
 */
public class CommentServiceImpl implements CommentService {
    private CommentDao commentDao = DaoFacory.getWebChatDao(CommentDao.class);
    @Override
    public void removeByMomentId(Integer id) {
        commentDao.removeByMomentId(id);
    }

    @Override
    public void add(Comment comment) {
        commentDao.add(comment);
    }

    @Override
    public String getMomentComment(Integer momentId) {
        List<Commenter> momentComment = commentDao.getMomentComment(momentId);
        return MyResult.build().setData(momentComment).setCode(200).setMsg("ok").toJson();
    }

    @Override
    public void remove(Integer id) {
        commentDao.remove(id);
    }
}
