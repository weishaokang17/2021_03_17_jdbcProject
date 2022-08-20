package per.wsk.$03.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;


import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.util.Properties;

/**
 * @Author wb_weishaokang
 * @date 2021/3/31 7:18 下午
 * @description
 */
public class JDBCUtil {


    private static DataSource dataSource = null;


    public static Connection getConnection() throws Exception{
        //获取文件输入流
        File file = new File("src/druid.properties");
        FileInputStream inputStream = new FileInputStream(file);
        //将输入流加载到properties对象
        Properties properties = new Properties();
        properties.load(inputStream);
        //获取数据源
        getDataSource(properties);
        return dataSource.getConnection();
    }


    public static synchronized DataSource getDataSource(Properties properties) throws Exception {
        if (dataSource == null) {
            dataSource = DruidDataSourceFactory.createDataSource(properties);
        }
        return dataSource;
    }

}
