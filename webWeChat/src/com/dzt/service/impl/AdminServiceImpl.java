package com.dzt.service.impl;

import com.dzt.core.DaoFacory;
import com.dzt.dao.AdminDao;
import com.dzt.entity.Admin;
import com.dzt.service.AdminService;
import org.apache.commons.codec.cli.Digest;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.List;

/**
 * @author Z
 */
public class AdminServiceImpl implements AdminService {
    private AdminDao adminDao = DaoFacory.getWebChatDao(AdminDao.class);
    @Override
    public Admin login(String username, String password) {

        List<Admin> admins = adminDao.selectExact("username", username);
        if (admins.size() > 0 ){
            Admin admin = admins.get(0);
            if (admin != null && admin.getPassword().equals(DigestUtils.md5Hex(password))){
                return admin;
            } else {
                return null;
            }
        } else {
            return null;
        }

    }

    @Override
    public boolean checkUser(String username) {
        List<Admin> admins = adminDao.selectExact("username", username);
        if (admins.size() > 0){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void add(Admin admin) {
        adminDao.add(admin);
    }
}
