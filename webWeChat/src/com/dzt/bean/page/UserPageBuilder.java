package com.dzt.bean.page;

import com.dzt.core.DaoFacory;
import com.dzt.dao.UserDao;
import com.dzt.entity.User;

import java.util.List;
import java.util.Map;

/**
 * @author Z
 */
public class UserPageBuilder extends PageBulider<UserPageBuilder> {
    private UserDao userDao = DaoFacory.getWebChatDao(UserDao.class);


    @Override
    public UserPageBuilder buildTotalDataSize(Map map) {
        int count = 0;
        if (map.containsKey("name")){
            String name = (String)map.get("name");
            count = userDao.selectVague("name",name).size();
        } else if (map.containsKey("username")){
            String username = (String)map.get("username");
            count = userDao.selectExact("username",username).size();
        } else {
            return this;
        }
        super.getPage().setTotalDateSize(count);
        return this;
    }

    @Override
    public UserPageBuilder buildData(Map map){
        List list = null;
        Page page = super.getPage();
        if (map.containsKey("name")){
            String name = (String)map.get("name");
            list = userDao.selectVague("name", name, (page.getCurrentPage() - 1) * page.getDataSize(), page.getDataSize());
        } else if (map.containsKey("username")){
            String username = (String)map.get("username");
            list = userDao.selectExact("username",username,(page.getCurrentPage() - 1) * page.getDataSize(), page.getDataSize());
        }
        for (User user : (List<User>)list) {
            user.setPassword("");
            user.setPhone("");
            user.setMail("");
        }
        super.getPage().setData(list);
        return this;
    }


}
