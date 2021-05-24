package com.dzt.service.impl;

import com.dzt.bean.MyResult;
import com.dzt.bean.selectBean.Publisher;
import com.dzt.core.DaoFacory;
import com.dzt.core.ToJSON;
import com.dzt.dao.MomentDao;
import com.dzt.dao.UnLookDao;
import com.dzt.entity.Moment;
import com.dzt.entity.UnLook;
import com.dzt.entity.User;
import com.dzt.service.MomentService;
import org.junit.Test;

import java.util.List;

/**
 * @author Z
 */
public class MomentServiceImpl implements MomentService {
    private MomentDao momentDao = DaoFacory.getWebChatDao(MomentDao.class);
    private UnLookDao unLookDao = DaoFacory.getWebChatDao(UnLookDao.class);
    @Override
    public void addMoment(String s, String[] friends, User user) {
        Moment moment = momentDao.add(new Moment(s,user.getId()));
        if (friends != null){
            for (String friend : friends) {
                unLookDao.add(new UnLook(moment.getId(),Integer.parseInt(friend)));
            }
        }
    }

    @Override
    public void remove(Integer id) {
        momentDao.remove(id);
    }

    @Override
    public Moment get(Integer id) {
        return momentDao.selectById(id);
    }



}
