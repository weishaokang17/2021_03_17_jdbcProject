package per.wsk.$03.test;

import com.alibaba.druid.pool.DruidDataSource;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.junit.Test;
import per.wsk.$03.util.JDBCUtil;


import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

/**
 * @Author wb_weishaokang
 * @date 2021/3/31 6:58 下午
 * @description
 */
public class DruidTest {


    @Test
    public void test01(){
        DruidDataSource druidDataSource = new DruidDataSource();
        //druidDataSource  向数据库连接信息等  set值  。。。

    }

    @Test
    public void test02() throws Exception {
        //获取文件输入流
        File file = new File("src/druid.properties");
        FileInputStream inputStream = new FileInputStream(file);
        //将输入流加载到properties对象
        Properties properties = new Properties();
        properties.load(inputStream);
        //创建数据源
        DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);

        Connection connection = dataSource.getConnection();

        System.out.println(connection);
    }

    @Test
    public void test03() throws Exception {
        Connection connection = JDBCUtil.getConnection();
        System.out.println(connection);
    }


}
