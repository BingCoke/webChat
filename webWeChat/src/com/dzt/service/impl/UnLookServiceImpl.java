package com.dzt.service.impl;

import com.dzt.core.DaoFacory;
import com.dzt.dao.UnLookDao;
import com.dzt.service.UnLookService;

/**
 * @author Z
 */
public class UnLookServiceImpl implements UnLookService {
    private UnLookDao unLookDao = DaoFacory.getWebChatDao(UnLookDao.class);
    @Override
    public void removeByMomentId(Integer id) {
        unLookDao.removeByMomentId(id);
    }
}
