package per.wsk.$02.test;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.junit.Test;
import per.wsk.$02.util.JdbcUtil;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @Author wb_weishaokang
 * @date 2021/3/31 5:20 下午
 * @description
 */
public class DBCPTest {


    @Test
    public void test01() throws SQLException {
        //①创建了DBCP数据库连接池
        BasicDataSource dataSource = new BasicDataSource();
        //②设置连接数据库的基本信息
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        //localhost:3306 可以省略，但如果ip不是localhost或端口号不是3306不能省略
        dataSource.setUrl("jdbc:mysql:///jdbc_base_study?useUnicode=true&characterEncoding=utf8&rewriteBatchedStatements=true");
        dataSource.setUsername("root");
        dataSource.setPassword("wskzxw200817");

        //③设置其他关于数据库连接池相关属性的设置
        dataSource.setInitialSize(5);
        dataSource.setMaxActive(10);

        Connection connection = dataSource.getConnection();

        System.out.println("----------");
        System.out.println(connection);
    }


    //使用配置文件
    @Test
    public void test02() throws Exception {
        //获取文件输入流
        File file = new File("src/dbcp.properties");
        FileInputStream inputStream = new FileInputStream(file);
        //将输入流加载到properties对象
        Properties properties = new Properties();
        properties.load(inputStream);
        //创建数据源
        DataSource dataSource = BasicDataSourceFactory.createDataSource(properties);

        Connection connection = dataSource.getConnection();
        System.out.println(connection);
    }


    @Test
    public void test03() throws Exception {
        Connection connection = JdbcUtil.getConnection();

        System.out.println(connection);
    }

}
