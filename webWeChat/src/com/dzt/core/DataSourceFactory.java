package com.dzt.core;




import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Z
 */
public class DataSourceFactory {
    public static final String DRUID_DATASOURCE = "test";
    public static final String WEB_DATASOURCE = "webChat";

    public static DataSource creatDataSource(String type){
        Properties properties = new Properties();
        DataSource dataSource = null;

        if (WEB_DATASOURCE.equals(type)){
            try {
                properties.load(DataSourceFactory.class.getClassLoader().getResourceAsStream("webChat.properties"));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            dataSource = new MyDataSource(properties);
        }

        return dataSource;
    }



}
