package per.wsk.$01.util;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Author wb_weishaokang
 * @date 2021/3/31 5:06 下午
 * @description
 */
public class JdbcUtil {

    //这行代码不能写到getConnection()方法里面，不然相当于每调用一次getConnection()方法，就创建了一个新的数据库连接池。
    private static ComboPooledDataSource dataSource = new ComboPooledDataSource("helloc3p0");

    public static Connection getConnectionByC3p0() throws SQLException {
        return dataSource.getConnection();
    }

}
