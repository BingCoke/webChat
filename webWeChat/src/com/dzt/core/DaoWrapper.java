package com.dzt.core;

import java.io.Serializable;

public class DaoWrapper implements Serializable {
    /**
     * 类型
     */
    private String type;

    /**
     * 返回值的类型
     */
    private String resultType;

    /**
     * 参数的类型
     */
    private String paramType;

    /**
     * sql语句
     */
    private String sql;

    public DaoWrapper(String type, String resultType, String paramType, String sql) {
        this.type = type;
        this.resultType = resultType;
        this.paramType = paramType;
        this.sql = sql;
    }

    public DaoWrapper(String type, String resultType, String sql) {
        this.type = type;
        this.resultType = resultType;
        this.sql = sql;
    }

    public DaoWrapper() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    @Override
    public String toString() {
        return "DaoWrapper{" +
                "type='" + type + '\'' +
                ", resultType='" + resultType + '\'' +
                ", paramType='" + paramType + '\'' +
                ", sql='" + sql + '\'' +
                '}';
    }
}
