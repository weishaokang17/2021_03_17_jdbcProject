package per.wsk.$01;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @Author wb_weishaokang
 * @date 2021/3/17 6:24 下午
 * @description
 * 连接数据库
 */
public class ConnectionTest {

    //连接数据库一
    @Test
    public void testConnection01() throws SQLException {
        //获取Driver的实现类对象
        Driver dirver = new com.mysql.jdbc.Driver();

        String url = "jdbc:mysql://localhost:3306/jdbc_base_study?useUnicode=true&characterEncoding=utf8";
        Properties info = new Properties();
        //将连接数据库的用户名和密码封装在Properties对象中
        info.setProperty("user","root");
        info.setProperty("password","wskzxw200817");

        Connection connect = dirver.connect(url, info);

        System.out.println(connect);
    }
    /*
    一、JDBC URL 用于标识一个被注册的驱动程序，驱动程序管理器通过这个 URL 选择正确的驱动程序，从而建立到数据库的连接。
    二、JDBC URL的标准由三部分组成，各部分间用冒号分隔。
       jdbc:子协议:子名称
       协议：JDBC URL中的协议总是jdbc
       子协议：子协议用于标识一个数据库驱动程序
       子名称：一种标识数据库的方法。子名称可以依不同的子协议而变化，用子名称的目的是为了定位数据库提供足够的信息。包含主机名(对应服务端的ip地址)，端口号，数据库名

    三、几种常用数据库的 JDBC URL
        ①MySQL的连接URL编写方式：
        jdbc:mysql://主机名称:mysql服务端口号/数据库名称?参数=值&参数=值
        jdbc:mysql://localhost:3306/atguigu
        jdbc:mysql://localhost:3306/atguigu?useUnicode=true&characterEncoding=utf8（如果JDBC
        程序与服务器端的字符集不一致，会导致乱码，那么可以通过参数指定服务器端的字符集）
        jdbc:mysql://localhost:3306/atguigu?user=root&password=123456
        ②Oracle 9i的连接URL编写方式：
        jdbc:oracle:thin:@主机名称:oracle服务端口号:数据库名称
        jdbc:oracle:thin:@localhost:1521:atguigu
        ③SQLServer的连接URL编写方式：
        jdbc:sqlserver://主机名称:sqlserver服务端口号:DatabaseName=数据库名称
        jdbc:sqlserver://localhost:1433:DatabaseName=atguigu
     */



    //连接数据库二，在上面一的基础上，代码中没有出现第三方的API（即上面一的new com.mysql.jdbc.Driver() ）,使代码具有更好的可移植性
    @Test
    public void testConnection02() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        //1.使用Drivier实现类对象，使用反射、
        Class<?> clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver)clazz.newInstance();
        //2.提供要连接的数据库
        String url = "jdbc:mysql://localhost:3306/jdbc_base_study?useUnicode=true&characterEncoding=utf8";
        //3.提供连接数据库的账号密码
        Properties info = new Properties();
        info.setProperty("user","root");
        info.setProperty("password","wskzxw200817");
        //4.获取连接
        Connection connect = driver.connect(url, info);

        System.out.println(connect);
    }


    //连接数据库三：使用DriverManager替换Driver
    @Test
    public void testConnection03() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        //1.获取Driver实现类对象，使用Drivier实现类对象，使用反射
        Class<?> clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver)clazz.newInstance();
        //2.获取另外三个连接数据库的基本信息
        String url = "jdbc:mysql://localhost:3306/jdbc_base_study?useUnicode=true&characterEncoding=utf8";
        String user = "root";
        String password = "wskzxw200817";

        //注册驱动
        DriverManager.registerDriver(driver);
        //获取连接
        Connection connection = DriverManager.getConnection(url, user, password);


        System.out.println(connection);

    }



    //连接数据库四：
    @Test
    public void testConnection04() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        //1.获取另外三个连接数据库的基本信息
        String url = "jdbc:mysql://localhost:3306/jdbc_base_study?useUnicode=true&characterEncoding=utf8";
        String user = "root";
        String password = "wskzxw200817";


        //2.获取Driver实现类对象，使用Drivier实现类对象，使用反射
        Class.forName("com.mysql.jdbc.Driver");
        /*
        Class<?> clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver)clazz.newInstance();
        //注册驱动
        DriverManager.registerDriver(driver);
        */

        //省略了注册驱动这一步，但仍然可以获取数据库连接对象，因为mysql的Driver实现类com.mysql.jdbc.Driver中，里面的静态代码块在类加载时注册了驱动。

        //获取连接
        Connection connection = DriverManager.getConnection(url, user, password);


        System.out.println(connection);

    }

    /*
    连接数据库五（final版）：将数据库连接信息添加到配置文件中。这样实现了数据和代码的分离，如果后面要改成别的数据库连接，
    直接修改jdbc.properties配置文件即可。  如果修改了配置信息，省去了重新打包的过程。不然如果在代码中修改数据库连接信息，需要整个项目重新打包。
     */
    @Test
    public void testConnection05() throws IOException, ClassNotFoundException, SQLException {
        //1.读取配置文件中的基本信息
        InputStream inputStream = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");

        Properties properties = new Properties();
        properties.load(inputStream);

        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        String url = properties.getProperty("url");
        String driverClass = properties.getProperty("driverClass");

        //2.加载驱动
        Class.forName(driverClass);

        //3.获取连接
        Connection connection = DriverManager.getConnection(url, user, password);

        System.out.println(connection);
    }


    /*
    CRUD是指在做计算处理时的增加(Create)、读取查询(Retrieve)、更新(Update)和删除(Delete)几个单词的首字母简写。
    主要被用在描述软件系统中DataBase或者持久层的基本操作功能。
     */
}
