package per.wsk.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.commons.dbutils.DbUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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



    /**
     * 关闭数据库连接
     * @param connection
     * @param statement
     */
    public static void closeResource(Connection connection, Statement statement){
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 关闭数据库连接
     * @param connection
     * @param statement
     */
    public static void closeResource(ResultSet resultSet, Connection connection, Statement statement){
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 使用DbUtils关闭资源
     * @param resultSet
     * @param connection
     * @param statement
     */
    public static void closeResourceByDbUtils(ResultSet resultSet,Connection connection, Statement statement){
        DbUtils.closeQuietly(resultSet);
        DbUtils.closeQuietly(connection);
        DbUtils.closeQuietly(statement);
    }


}
