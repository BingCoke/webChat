package com.dzt.service;

import com.dzt.bean.selectBean.GroupUser;
import com.dzt.entity.UserGroup;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author Z
 */
public interface UserGroupService {
    /**
     * 创建一个群，并返回群号
     * @param groupName
     * @return
     */
    int addGroup(UserGroup groupName);

    /**
     * 得到用户的所有群
     * @param id
     * @return
     */
    String getMyGroups(int id);

    /**
     * 保存群信息
     * @param groupToLook
     */
    void save(UserGroup groupToLook);

    /**
     * 根据id得到群
     * @param id
     * @return
     */
    UserGroup selectById(Integer id);
}
