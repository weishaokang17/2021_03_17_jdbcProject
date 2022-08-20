package per.wsk.$01.test;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Author wb_weishaokang
 * @date 2021/3/29 6:10 下午
 * @description
 *
 * JDBC 的数据库连接池使用 javax.sql.DataSource 来表示，DataSource 只是一个接口，该接口通常由服务器
 * (Weblogic, WebSphere, Tomcat)提供实现，也有一些开源组织提供实现：
 * ①DBCP 是Apache提供的数据库连接池。tomcat 服务器自带dbcp数据库连接池。速度相对c3p0较快，但因
 * 自身存在BUG，Hibernate3已不再提供支持。
 * ②C3P0 是一个开源组织提供的一个数据库连接池，速度相对较慢，稳定性还可以。hibernate官方推荐使用
 * Proxool 是sourceforge下的一个开源项目数据库连接池，有监控连接池状态的功能，稳定性较c3p0差一
 * 点
 * ③BoneCP 是一个开源组织提供的数据库连接池，速度快
 * ④Druid 是阿里提供的数据库连接池，据说是集DBCP 、C3P0 、Proxool 优点于一身的数据库连接池，但是
 * 速度不确定是否有BoneCP快
 *
 * DataSource 通常被称为数据源，它包含连接池和连接池管理两个部分，习惯上也经常把 DataSource 称为连接
 * 池。 DataSource用来取代DriverManager来获取Connection，获取速度快，同时可以大幅度提高数据库访问速
 * 度。
 *
 * 注意：
 * ①数据源和数据库连接不同，数据源无需创建多个，它是产生数据库连接的工厂，因此整个应用只需要一个
 * 数据源即可。
 * ②当数据库访问结束后，程序还是像以前一样关闭数据库连接：conn.close(); 但conn.close()并没有关闭数
 * 据库的物理连接，它仅仅把数据库连接释放，归还给了数据库连接池。
 *
 *
 */
public class C3P0Test {


    @Test
    public void test01() throws Exception {
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setDriverClass("com.mysql.jdbc.Driver");
        cpds.setJdbcUrl("jdbc:mysql://localhost:3306/jdbc_base_study?useUnicode=true&characterEncoding=utf8&rewriteBatchedStatements=true");
        cpds.setUser("root");
        cpds.setPassword("wskzxw200817");

        //初始的数据库连接池中的数据库连接数
        cpds.setInitialPoolSize(3);


        Connection connection = cpds.getConnection();
        System.out.println(connection);

        //销毁C3p0连接池，项目中一般不会将数据库连接池销毁的。
        DataSources.destroy(cpds);
    }


    @Test
    public void test02() throws SQLException {
        //获取数据库连接池
        ComboPooledDataSource cpds = new ComboPooledDataSource("helloc3p0");
        //获取连接
        Connection connection = cpds.getConnection();
        //
        System.out.println(connection);

    }



}
