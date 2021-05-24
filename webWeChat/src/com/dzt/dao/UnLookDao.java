package com.dzt.dao;

import com.dzt.core.BaseDao;
import com.dzt.entity.UnLook;

/**
 * @author Z
 */
public interface UnLookDao extends BaseDao<UnLook> {
    /**
     * 删除朋友圈的相关信息
     * @param id
     */
    void removeByMomentId(Integer id);
}
