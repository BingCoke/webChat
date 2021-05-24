package com.dzt.dao;

import com.dzt.bean.selectBean.GroupUser;
import com.dzt.core.BaseDao;
import com.dzt.entity.GroupMember;

import java.util.List;

/**
 * @author Z
 */
public interface GroupMemberDao extends BaseDao<GroupMember> {
    /**
     * 某个群找到群主的对应字段
     * @param toId
     * @return
     */
    List<GroupMember> selectGroupLord(Integer toId);

    /**
     * 找到群管理和群主
     * @param toId
     * @return
     */
    List<GroupMember> getManagement(Integer toId);

    /**
     * 找到该群指定成员
     * @param groupId
     * @param userId
     * @return
     */
    List<GroupMember> getMember(Integer groupId, Integer userId);

    /**
     * 得到一个群的群成员
     * @param id
     * @return
     */

    List<GroupUser> getGroupMember(Integer id);

    /**
     * 修改某个群成员的群昵称
     * @param vest
     * @param id
     * @param userId
     */
    void saveVest(String vest, Integer id, Integer userId);

    /**
     * 删除某个群成员
     * @param id
     * @param userId
     */
    void deleteMember(Integer id, Integer userId);

    /**
     * 设置某个群成员的权限
     * @param power
     * @param id
     * @param userId
     */
    void updatePower(int power, Integer id, Integer userId);


}
