package com.dzt.service;

import com.dzt.entity.Expression;

import java.util.List;

public interface ExpressionService {
    /**
     * 得到用户的表情
     * @param id
     * @return
     */
    List<Expression> getUserExpression(Integer id);

    /**
     * 给用户增加一个表情
     * @param expression
     */
    void add(Expression expression);

    /**
     * 根据id得到表情
     * @param id
     * @return
     */
    Expression getById(Integer id);

    /**
     * 根据id将表情删除
     * @param id
     */
    void remove(Integer id);
}
