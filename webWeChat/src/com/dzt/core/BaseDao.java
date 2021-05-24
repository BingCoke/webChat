package com.dzt.core;


import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public interface BaseDao<T> {

    /**
     * 向数据库增加数据
     * @param t
     */
    public T add(T t);

    /**
     * 把修改后的对象保存到数据库中
     * @param t
     * @return
     */
    public boolean save(T t);

    /**
     * 删除对象对应在数据库的数据
     * @param t
     * @return
     */
    public boolean remove(T t);

    public boolean remove(int id);


    /**
     * 查询表的所有的数据
     * @param start
     * @param num
     * @return
     */
    public List<T> selectAll(int start,int num);

    /**
     * 模糊查询
     * @param label 传入标签的名字（驼峰命名）
     * @param value 需要查询的标签的值
     * @return
     */
    public List<T> selectVague(String label, Object value);

    /**
     * 上个方法重载
     * @param label
     * @param value
     * @param start
     * @param num
     * @return
     */
    public List<T> selectVague(String label,Object value,int start,int num);

    /**
     * 准确查询
     * @param label 驼峰命名的标签
     * @param value
     * @return
     */
    public List<T> selectExact(String label,Object value);

    /**
     * 上一个方法的重载，当结果可能会很多时将结果数目进行控制
     * @param label
     * @param value
     * @param start
     * @param num
     * @return
     */
    public List<T> selectExact(String label, Object value,int start,int num);

    /**
     * 通过id查找
     * @param id
     * @return
     */
    public T selectById(int id);

    public int getCount();
}
