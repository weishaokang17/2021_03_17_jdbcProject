package per.wsk.$02.util;

import com.mchange.v2.c3p0.DataSources;
import org.apache.commons.dbcp.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.util.Properties;

/**
 * @Author wb_weishaokang
 * @date 2021/3/31 6:10 下午
 * @description
 */
public class JdbcUtil {



    private static DataSource dataSource = null;


    public static Connection getConnection() throws Exception{
        //获取文件输入流
        File file = new File("src/dbcp.properties");
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
            dataSource = BasicDataSourceFactory.createDataSource(properties);
        }
        return dataSource;
    }


}
