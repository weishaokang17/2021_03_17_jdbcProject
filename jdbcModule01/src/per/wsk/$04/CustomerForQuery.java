package per.wsk.$04;


import org.junit.Test;
import per.wsk.$04.bean.Customer;
import per.wsk.$04.util.JDBCUtil;



import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;


/**
 * @Author wb_weishaokang
 * @date 2021/3/19 3:19 下午
 * @description
 * 针对customers表的查询操作
 */
public class CustomerForQuery {

    /**
     * 针对Customer对象的所有属性的查询
     */
    @Test
    public void test01() {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            //1.获取连接
            connection = JDBCUtil.getConnection();
            //2.预编译sql语句，获取PrepatedStatement对象
            String sql = "select id,name,email,birth from customers where id = ?";
            ps = connection.prepareStatement(sql);
            ps.setInt(1,6);
            //3.执行,返回结果集
            resultSet = ps.executeQuery();
            //4.处理结果集
            List<Customer> list = new LinkedList<>();
            while(resultSet.next()){//next()方法判断下一条有没有数据，如果有数据，就返回true,同时指针下移一个，如果没有数据，就返回false，指针不下移
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String email = resultSet.getString(3);
                //要知道这个查询的sql语句中这几个字段，在数据库表Customer分别是什么类型，对应什么java类型，顺序也不能错，否则会类型转换异常
                Date birth = resultSet.getDate(4);

                Customer customer = new Customer(id, name, email, birth);
                list.add(customer);
            }
            //遍历
            list.stream().forEach(System.out::println);
            //输出：Customer [id=6, name=迪丽热巴, email=reba@163.com, birth=1983-05-17]
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //5.关闭资源
        JDBCUtil.closeResource(resultSet,connection,ps);

    }


    /**
     * 针对Customer对象的任意几个属性的查询
     * @param sql
     * @param args
     * @return
     */
    public List<Customer> queryCustomer(String sql,Object...args){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        List<Customer> list = new LinkedList<>();
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
                Customer customer = new Customer();
                for (int i = 1; i <= columnCount; i++) {
                    //列值
                    Object columnValue = resultSet.getObject(i);
                    //列名
                    String columnName = metaData.getColumnName(i);
                    //通过反射给对象的各字段赋值
                    Field field = Customer.class.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(customer,columnValue);
                }
                list.add(customer);
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
        }
        //5.关闭资源
        JDBCUtil.closeResource(resultSet,connection,ps);
        return list;
    }


    @Test
    public void test02(){
        String sql = "select id,email,birth from customers where name = ?";
        List<Customer> resultList = queryCustomer(sql, "张学友");
        resultList.stream().forEach(System.out::println);
    }


    /*
    常见的java中的字段类型与数据库中类型的对应

    Java类型          SQL类型
    boolean             BIT
    byte              TINYINT
    short             SMALLINT
    int                INTEGER
    long                 BIGINT
    String           CHAR,VARCHAR,LONGVARCHAR
    byte                array BINARY , VAR BINARY
    java.sql.Date           DATE
    java.sql.Time           TIME
    java.sql.Timestamp      TIMESTAMP
     */
}
