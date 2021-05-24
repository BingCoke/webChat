package com.dzt.bean.page;

import com.dzt.bean.selectBean.LongCount;
import com.dzt.bean.selectBean.Publisher;
import com.dzt.core.DaoFacory;
import com.dzt.dao.MomentDao;
import com.dzt.entity.Moment;
import com.dzt.entity.User;

import java.util.List;
import java.util.Map;

/**
 * @author Z
 */
public class MomentPageBuilder extends PageBulider<MomentPageBuilder> {
    private MomentDao momentDao = DaoFacory.getWebChatDao(MomentDao.class);

    @Override
    MomentPageBuilder buildTotalDataSize(Map map) {
        if (map.containsKey("user")){
            User user = (User) map.get("user");
            LongCount longCount = momentDao.getFriendsMomentCount(user.getId(), user.getId(), user.getId()).get(0);
            getPage().setTotalDateSize(longCount.getCount().intValue());
        } else if (map.containsKey("adminToLook")){
            User user = (User) map.get("adminToLook");
            LongCount longCount = momentDao.getUserMomentCount(user.getId()).get(0);
            getPage().setTotalDateSize(longCount.getCount().intValue());
        }
        return this;
    }

    @Override
    MomentPageBuilder buildData(Map map) {
        Page page = getPage();
        if (map.containsKey("user")){
            User user = (User) map.get("user");
            List<Publisher> friendsMoment = momentDao.getFriendsMoment(user.getId(), user.getId(), user.getId(), (page.getCurrentPage() - 1) * page.getDataSize(), page.getDataSize());
            page.setData(friendsMoment);
        } else if (map.containsKey("adminToLook")){
            User user = (User) map.get("adminToLook");
            List<Publisher> userMoment = momentDao.getUserMoment(user.getId(), (page.getCurrentPage() - 1) * page.getDataSize(), page.getDataSize());
            page.setData(userMoment);
        }
        return this;
    }
}
