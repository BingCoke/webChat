package com.dzt.dao;

import com.dzt.bean.selectBean.LongCount;
import com.dzt.core.BaseDao;
import com.dzt.entity.PersonRelationship;

import java.util.List;

/**
 * @author Z
 */
public interface PersonRelationshipDao extends BaseDao<PersonRelationship> {
    /**
     * 检查两人是否为好友
     * @param fromId
     * @param toId
     */
    List<LongCount> relationShipCount(Integer fromId, Integer toId);

    /**
     * 根据from 和 to 的id 查找队医的关系
     * @param userId
     * @param id
     * @return
     */
    List<PersonRelationship> selectByFromTo(Integer userId, int id);

    /**
     * 设置好友间的拉黑
     * @param i 设置black的值
     * @param id 对应的id
     */
    void updateBlack(Integer i, Integer id);

    /**
     * 更新备注
     * @param remark
     * @param id
     */
    void updateRemark(String remark, Integer id);
}
