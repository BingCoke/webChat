package com.dzt.bean.page;

import com.dzt.core.DaoFacory;
import com.dzt.dao.UserGroupDao;
import com.dzt.entity.User;
import com.dzt.entity.UserGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GroupPageBuilder extends PageBulider<GroupPageBuilder> {
    private UserGroupDao userGroupDao = DaoFacory.getWebChatDao(UserGroupDao.class);
    @Override
    GroupPageBuilder buildTotalDataSize(Map map) {
        int count = 0;
        if (map.containsKey("name")){
            String name = (String)map.get("name");
            count = userGroupDao.selectVague("name",name).size();
        } else if (map.containsKey("id")){
            int id = (int)map.get("id");
            UserGroup userGroup = userGroupDao.selectById(id);
            if (userGroup == null){
                count = 0;
            } else {
                count = 1;
            }
        } else {
            return this;
        }
        super.getPage().setTotalDateSize(count);
        return this;
    }

    @Override
    GroupPageBuilder buildData(Map map) {
        List list = null;
        Page page = super.getPage();
        if (map.containsKey("name")){
            String name = (String)map.get("name");
            list = userGroupDao.selectVague("name", name, (page.getCurrentPage() - 1) * page.getDataSize(), page.getDataSize());
        } else if (map.containsKey("id")){
            int id = (int) map.get("id");
            UserGroup userGroup = userGroupDao.selectById(id);
            if (userGroup != null){
                list = new ArrayList();
                list.add(userGroup);
            }
        }
        super.getPage().setData(list);
        return this;
    }
}
