package com.dzt.service.impl;

import com.dzt.core.DaoFacory;
import com.dzt.dao.ExpressionDao;
import com.dzt.entity.Expression;
import com.dzt.service.ExpressionService;

import java.util.List;

/**
 * @author Z
 */
public class ExpressionServiceImpl implements ExpressionService {
    private ExpressionDao expressionDao = DaoFacory.getWebChatDao(ExpressionDao.class);
    @Override
    public List<Expression> getUserExpression(Integer id) {
        return expressionDao.selectExact("userId",id);
    }

    @Override
    public void add(Expression expression) {
        expressionDao.add(expression);
    }

    @Override
    public Expression getById(Integer id) {
        return expressionDao.selectById(id);
    }

    @Override
    public void remove(Integer id) {
        expressionDao.remove(id);
    }
}
