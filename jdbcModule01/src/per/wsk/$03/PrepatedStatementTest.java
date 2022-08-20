package per.wsk.$03;

import org.junit.Test;
import per.wsk.$01.ConnectionTest;
import per.wsk.$03.util.JDBCUtil;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * @Author wb_weishaokang
 * @date 2021/3/19 10:55 上午
 * @description
 */
public class PrepatedStatementTest {

    /**
     * 向customers表中添加数据
     */
    @Test
    public void test01(){
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        try {
            //1.读取配置文件中的基本信息
//        InputStream inputStream = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
            InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");

            Properties properties = new Properties();
            properties.load(inputStream);

            String user = properties.getProperty("user");
            String password = properties.getProperty("password");
            String url = properties.getProperty("url");
            String driverClass = properties.getProperty("driverClass");

            //2.加载驱动
            Class.forName(driverClass);

            //3.获取连接
            connection = DriverManager.getConnection(url, user, password);

            //4. 预编译sql语句，返回PreparedStatement对象
            String sql = "insert into customers (name,email,birth) values (?,?,?)";
            preparedStatement = connection.prepareStatement(sql);
            //5.填充占位符
            preparedStatement.setString(1,"魏少康");
            preparedStatement.setString(2,"17@163.com");

            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sf.parse("1992-12-14");
            preparedStatement.setDate(3,new java.sql.Date(date.getTime()));

            //6.执行操作
            preparedStatement.execute();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            //7.资源的关闭
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
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
    }


    //修改数据库中的一条数据
    @Test
    public void test02() {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            //1.获取数据库连接
            connection = JDBCUtil.getConnection();
            //2.预编译sql语句，获取PrepatedStatement对象
            String sql = "update customers set email = ? where id = ?";
            ps = connection.prepareStatement(sql);
            //3. 填充占位符
            ps.setObject(1,"2458250767@qq.com");
            ps.setInt(2,19);
            //4.执行
            ps.execute();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //5.资源的关闭
        JDBCUtil.closeResource(connection,ps);
    }


    /**
     * 通用的增、删、改操作
     * @param sql
     * @param args
     */
    public static void excute(String sql,Object... args) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            //1.获取数据库连接
            connection = JDBCUtil.getConnection();
            //2.预编译sql语句，获取PrepatedStatement对象
            ps = connection.prepareStatement(sql);
            //3. 填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1,args[i]);
            }
            //4.执行
            ps.execute();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //5.资源的关闭
        JDBCUtil.closeResource(connection,ps);
    }

    @Test
    public void testCommonExcute(){
//        String sql = "delete from customers where id = ?";
//        excute(sql,3);

        /*
        下面注释掉的sql执行报错，因为order这个表名在sql中有特殊含义，所以数据库表在命名时不要用有特殊含义的词，
        此时需要在sql中把order这个表名用Tab键上面那个键的符号引起来。
         */
//        String sql = "update order set order_name = ? where order_id = ?";
        String sql = "update `order` set order_name = ? where order_id = ?";
        excute(sql,"DD",2);

    }

}
