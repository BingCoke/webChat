package com.dzt.service.impl;

import com.dzt.bean.selectBean.LongCount;
import com.dzt.core.DaoFacory;
import com.dzt.dao.PersonRelationshipDao;
import com.dzt.entity.PersonRelationship;
import com.dzt.service.PersonRelationshipService;

/**
 * @author Z
 */
public class PersonRelationShipServiceImpl implements PersonRelationshipService {
    private PersonRelationshipDao prDao = DaoFacory.getWebChatDao(PersonRelationshipDao.class);
    @Override
    public void add(PersonRelationship personRelationship) {
        prDao.add(personRelationship);
    }

    @Override
    public boolean isHaveRelationShip(Integer fromId, Integer toId) {
        LongCount longCount = prDao.relationShipCount(fromId, toId).get(0);
        if (longCount.getCount() != 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean isBlack(int id, Integer userId) {
        PersonRelationship personRelationship = prDao.selectByFromTo(userId, id).get(0);
        if (personRelationship.getBlack() == 0){
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void updateBlack(Integer id, int i) {

        prDao.updateBlack(i,id);
    }

    @Override
    public void updateRemark(String remark, Integer id) {
        prDao.updateRemark(remark,id);
    }

    @Override
    public void deleteFriend(int id, Integer userId) {
        Integer id1 = prDao.selectByFromTo(id, userId).get(0).getId();
        Integer id2 = prDao.selectByFromTo(userId, id).get(0).getId();
        prDao.remove(id1);
        prDao.remove(id2);
    }
}
