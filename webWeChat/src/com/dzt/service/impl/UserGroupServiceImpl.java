package com.dzt.service.impl;

import com.dzt.bean.MyResult;
import com.dzt.bean.page.Page;
import com.dzt.bean.page.PageDirector;
import com.dzt.bean.selectBean.GroupUser;
import com.dzt.core.BeanFactory;
import com.dzt.core.DaoFacory;
import com.dzt.dao.UserGroupDao;
import com.dzt.entity.User;
import com.dzt.entity.UserGroup;
import com.dzt.service.GroupMemberService;
import com.dzt.service.UserGroupService;
import com.dzt.util.StringUtil;
import com.dzt.util.ToObjectUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * @author Z
 */
public class UserGroupServiceImpl implements UserGroupService {
    private UserGroupDao userGroupDao = DaoFacory.getWebChatDao(UserGroupDao.class);
    private GroupMemberService groupMemberService = BeanFactory.getBean(GroupMemberServiceImpl.class);

    @Override
    public int addGroup(UserGroup group) {
        int count = userGroupDao.getCount();
        group.setId(count + 5);
        userGroupDao.add(group);
        return count + 5;
    }

    @Override
    public String getMyGroups(int id) {
        List<UserGroup> userGroups = userGroupDao.getGroupByUser(id);

        return MyResult.build().setData(userGroups).setCode(200).toJson();
    }

    @Override
    public void save(UserGroup groupToLook) {
        userGroupDao.save(groupToLook);
    }

    @Override
    public UserGroup selectById(Integer id) {
        return userGroupDao.selectById(id);
    }


}
