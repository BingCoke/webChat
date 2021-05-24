package com.dzt.dao;

import com.dzt.bean.selectBean.GroupUser;
import com.dzt.core.BaseDao;
import com.dzt.entity.UserGroup;

import java.util.List;

/**
 * @author Z
 */
public interface UserGroupDao extends BaseDao<UserGroup> {

    /**
     * 得到用户加入的群
     * @param id
     * @return
     */
    List<UserGroup> getGroupByUser(int id);

}
