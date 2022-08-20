package per.wsk.$05;

import org.junit.Test;
import per.wsk.$05.util.JDBCUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * @Author wb_weishaokang
 * @date 2021/3/23 5:49 下午
 * @description
 *
 * 练习：
 */
public class Practice {

    @Test
    public void test01(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("输入姓名：");
        String name = scanner.next();
        System.out.println("输入邮箱");
        String email = scanner.next();
        System.out.println("输入生日：");
        String birth = scanner.next();//此处不需要担心字符串类型，插入到数据库的birth这一列是否会报错的问题，因数据库中birth是日期类型，
        //其实不需要担心，只要字符串是YYYY-MM-dd格式，就能添加成功，这里包含了隐式转换。

        String sql = "insert into customers (name,email,birth) values (?,?,?)";

        excute(sql,name,email,birth);

    }





    /**
     * 通用的增、删、改操作
     * @param sql
     * @param args
     */
    public static int excute(String sql,Object... args) {
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
            /*
            ps.execute()
            如果执行的是查询操作，且能查到结果集，返回true。
            如果执行的是增删改操作，或查询操作但没有查到结果集，返回false
             */
//            ps.execute();

            return ps.executeUpdate();//这个是增删改调用的方法，返回结果表示影响的行数
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //5.资源的关闭
        JDBCUtil.closeResource(connection,ps);
        return 0;
    }



}
