package com.dzt.dao;

import com.dzt.core.BaseDao;
import com.dzt.entity.Admin;

/**
 * @author Z
 */
public interface AdminDao extends BaseDao<Admin> {
    /**
     * 修改用户的密码
     * @param password
     * @param userId
     */
    void updatePassword(String password, int userId);
}
