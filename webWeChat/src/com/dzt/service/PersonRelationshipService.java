package com.dzt.service;

import com.dzt.entity.PersonRelationship;

/**
 * @author Z
 */
public interface PersonRelationshipService {
    /**
     * 增加关系
     *
     * @param personRelationship
     */
    void add(PersonRelationship personRelationship);

    /**
     * 是否有该好友
     * @param fromId
     * @param toId
     * @return
     */
    boolean isHaveRelationShip(Integer fromId, Integer toId);

    /**
     * 是否被拉黑
     * @param id
     * @param userId
     * @return
     */
    boolean isBlack(int id, Integer userId);

    /**
     * 拉黑或者取消拉黑
     * @param id
     * @param i
     */
    void updateBlack(Integer id, int i);

    /**
     * 更新备注
     * @param remark
     * @param id
     */
    void updateRemark(String remark, Integer id);

    /**
     * 好友删除
     * @param id
     * @param userId
     */
    void deleteFriend(int id, Integer userId);
}
