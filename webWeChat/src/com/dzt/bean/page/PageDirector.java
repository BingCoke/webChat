package com.dzt.bean.page;

import com.dzt.bean.selectBean.Publisher;
import com.dzt.entity.User;
import com.dzt.entity.UserGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Z
 * 分页的制造
 */
public class PageDirector {
    private Integer currentPage;
    private Integer dataSize;
    private Map select = new HashMap();

    public PageDirector() {
    }

    public Page<User> BuildUserPage(){
        UserPageBuilder userPageBuilder = new UserPageBuilder();
        userPageBuilder
                .buildTotalDataSize(select)
                .buildDataSize(dataSize)
                .buildCurrentPage(currentPage)
                .buildData(select);
        return userPageBuilder.getPage();
    }

    public Page<UserGroup> BuildGroupPage(){
        GroupPageBuilder groupPageBuilder = new GroupPageBuilder();
        groupPageBuilder
                .buildTotalDataSize(select)
                .buildDataSize(dataSize)
                .buildCurrentPage(currentPage)
                .buildData(select);
        return groupPageBuilder.getPage();
    }

    public Page<Publisher> BuildMomentPage(){
        MomentPageBuilder momentPageBuilder = new MomentPageBuilder();
        momentPageBuilder
                .buildTotalDataSize(select)
                .buildDataSize(dataSize)
                .buildCurrentPage(currentPage)
                .buildData(select);
        return momentPageBuilder.getPage();
    }

    public Page buildMessagePage(){
        MessagePageBuilder messagePageBuilder = new MessagePageBuilder();
        messagePageBuilder
                .buildTotalDataSize(select)
                .buildDataSize(dataSize)
                .buildCurrentPage(currentPage)
                .buildData(select);
        return messagePageBuilder.getPage();
    }


    public PageDirector(Integer currentPage, Integer dataSize, Map select) {
        this.currentPage = currentPage;
        this.dataSize = dataSize;
        this.select = select;
    }

    public Map getSelect() {
        return select;
    }

    @Override
    public String toString() {
        return "PageDirector{" +
                "currentPage=" + currentPage +
                ", dataSize=" + dataSize +
                ", select=" + select +
                '}';
    }
}
