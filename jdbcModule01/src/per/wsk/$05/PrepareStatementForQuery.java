package per.wsk.$05;

import org.junit.Test;
import per.wsk.$02.User;
import per.wsk.$05.bean.Order;
import per.wsk.$05.bean.Customer;
import per.wsk.$05.util.JDBCUtil;


import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * @Author wb_weishaokang
 * @date 2021/3/22 6:13 下午
 * @description
 */
public class PrepareStatementForQuery {

    @Test
    public void test01(){
        String sql = "select id,email,birth from customers where name = ?";
        List<Customer> resultList = prepareStatementForQuery(Customer.class,sql, "张学友");
        resultList.stream().forEach(System.out::println);

        System.out.println("---------------------------------------");

        String sql2 = "select order_id orderId,order_name orderName,order_date orderDate from `order` where order_name = ? or order_id = ?";
        List<Order> resultList2 = prepareStatementForQuery(Order.class,sql2, "DD", 4);
        resultList2.stream().forEach(System.out::println);
    }




    public <T> List<T> prepareStatementForQuery(Class<T> clazz,String sql, Object...args){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        List<T> list = new LinkedList<>();
        try {
            //1.获取连接
            connection = JDBCUtil.getConnection();
            //2.预编译sql语句，获取PrepatedStatement对象
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1,args[i]);
            }
            //3.执行,返回结果集
            resultSet = ps.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();//获取结果集的元数据
            int columnCount = metaData.getColumnCount();//查询结果的列数
            //4.处理结果集
            while(resultSet.next()){
                T instance = clazz.newInstance();
                for (int i = 1; i <= columnCount; i++) {
                    //列值
                    Object columnValue = resultSet.getObject(i);
                    //列名
//                    String columnName = metaData.getColumnName(i);
                    String columnLabel = metaData.getColumnLabel(i);
                    /*
                    getColumnName(int column)：获取指定列的名称
                    getColumnLabel(int column)：获取指定列的sql中定的别名，如果没有别名就还是数据库的列名
                     */

                    //通过反射给对象的各字段赋值
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(instance,columnValue);
                }
                list.add(instance);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        //5.关闭资源
        JDBCUtil.closeResource(resultSet,connection,ps);
        return list;
    }


    /**
     * PrepareStatement可以解决sql注入问题
     */
    @Test
    public void testLogin() {
        Scanner scan = new Scanner(System.in);

        System.out.print("用户名：");
        String userName = scan.nextLine();
        System.out.print("密   码：");
        String password = scan.nextLine();

		/*
		例如：输入的 user是  1' or ，password是  ='1' or '1' = '1
		很明显，数据库中不存在用户名是 1' or ,密码是 ='1' or '1' = '1的用户。但拼接后，形成的sql就成了下面的样子。
		下面的sql的查询条件是 user = '1' 或者 ' and password = ' = '1' 或者  '1' = '1'
		三个条件满足一个就能查到结果，很明显，第三个条件 '1' = '1' 恒成立。所以这个sql始终能查到结果

		 SELECT user,password FROM user_table WHERE USER = '1' or ' AND PASSWORD = '='1' or '1' = '1';

		 SQL 注入是利用某些系统没有对用户输入的数据进行充分的检查，而在用户输入数据中注入非法的 SQL 语句段或命令。如
		 SELECT user,password FROM user_table WHERE USER = '1' or ' AND PASSWORD = '='1' or '1' = '1'这个sql。
		 从而利用系统的 SQL 引擎完成恶意行为的做法。

		 但PrepareStatement不存在这种sql注入的问题，因为PrepareStatement有预编译的处理，先编译好了这条sql，后面将变量
		 直接填到占位符？里面， 这样最终形成的查询sql中，查询条件user就是 1' or ，password就是  ='1' or '1' = '1，很明显数据库不存在这个用户。
		 */
        String sql = "SELECT user,password FROM user_table WHERE user = ? and password = ?";
        System.out.println(sql);
        List<User> list = prepareStatementForQuery(User.class,sql,userName,password);
        if (list != null && list.size() > 0) {
            System.out.println("登录成功!");
        } else {
            System.out.println("用户名或密码错误！");
        }
    }
    /*
        PrepareStatement相对于Statement解决的问题
        ①省略了繁琐的sql字符串拼接，解决了sql注入问题
        ②PrepareStatement可以操作Blob数据，Statement办不到
        ③PrepareStatement可以实现更高效的批量操作，例如：批量向一张表中添加一万条数据Statement是直接将每个参数写到sql中了，
        由于每条sql插入的数据不同，导致这10000条sql都各不相同，执行时需要对这10000条sql每个都进行语法校验，比较耗时。
        但PrepareStatement用的占位符，预编译时这10000条sql都一模一样，所以只进行了一次语法校验。

     */



}
