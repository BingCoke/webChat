package com.dzt.core;



import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.*;

public class SessionFactory {

    private DataSource dataSource;

    /**
     * 第一个是命名空间就是对应的某个dao，map下放入命名空间的相关配置，string是方法的名字，daowrapper用来描述这个方法，放入sql语句的类型，返回值什么的
     */
    private Map<String,Map<String  ,DaoWrapper>> env = new HashMap<>();
    private Map<String,String> entityMap = new HashMap<>() ;
    public SessionFactory(String configPath) {
        parseConfigXml(configPath);
    }

    public Session openSession(){
        Session session = null;
        try {
            session = new Session(dataSource,env,entityMap);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return session;
    }


    /**
     * 解析配置文件
     * @param configPath
     */
    private void parseConfigXml(String configPath) {
        //解析数据源
        InputStream resourceAsStream = SessionFactory.class.getClassLoader().getResourceAsStream(configPath);
        SAXReader saxReader = new SAXReader();
        Document read = null;
        try {
            read = saxReader.read(resourceAsStream);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Element dataSourceRoot = read.getRootElement();
        Element dataSourceElement = dataSourceRoot.element("dataSource");
        dataSource = DataSourceFactory.creatDataSource(dataSourceElement.getTextTrim());

        //获取所有的mapper文件
        List<Element> mapperElements = dataSourceRoot.elements("mapper");
        List<String> mapperPaths = new ArrayList<>();
        //所有的map路径
        for (Element mapperElement : mapperElements) {
            mapperPaths.add(mapperElement.getTextTrim());
        }

        for (String mapperPath : mapperPaths) {
            Map<String,DaoWrapper> wrapper = new HashMap<>();
            try {
                read = saxReader.read(this.getClass().getClassLoader().getResourceAsStream(mapperPath));
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            Element root = read.getRootElement();
            String namespace = root.attribute("namespace").getValue();
            entityMap.put(namespace,root.attributeValue("entity"));
            Iterator<Element> iterator = root.elementIterator();
            while (iterator.hasNext()) {
                Element element = iterator.next();
                String id = element.attributeValue("id");
                String type = element.getName();

                String paramType = element.attributeValue("paramType");
                String resultType = element.attributeValue("resultType");
                String sql = element.getTextTrim();
                wrapper.put(id,new DaoWrapper(type,resultType,paramType,sql));
            }
            env.put(namespace,wrapper);
        }


    }

}
