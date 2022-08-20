package per.wsk.$04;

import org.junit.Test;
import per.wsk.$04.bean.Customer;
import per.wsk.$04.bean.Order;
import per.wsk.$04.util.JDBCUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author wb_weishaokang
 * @date 2021/3/22 2:54 下午
 * @description
 */
public class OrderForQuery {

    /**
     * 针对于表的字段名与类的属性名不相同的情况：
     * 	 1. 在sql语句中，使用类的属性名来命名字段的别名
     * 	 2. 使用ResultSetMetaData时，需要使用getColumnLabel()来替换getColumnName(),
     * 	    获取列的别名。
     * 	 说明：如果sql中没有给字段起别名，getColumnLabel()获取的就是列名
     */
    @Test
    public void test01(){
        String sql = "select order_id orderId,order_name orderName,order_date orderDate from `order` where order_name = ? or order_id = ?";
        List<Order> resultList = queryOrderList(sql, "DD", 4);
        resultList.stream().forEach(System.out::println);
    }



    /**
     * 针对Customer对象的任意几个属性的查询
     * @param sql
     * @param args
     * @return
     */
    public List<Order> queryOrderList(String sql, Object...args){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        List<Order> list = new LinkedList<>();
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
                Order order = new Order();
                for (int i = 1; i <= columnCount; i++) {
                    //列值
                    Object columnValue = resultSet.getObject(i);
                    //列名
//                    String columnName = metaData.getColumnName(i);
                    String columnLabel = metaData.getColumnLabel(i);
                    //通过反射给对象的各字段赋值
                    Field field = Order.class.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(order,columnValue);
                }
                list.add(order);
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

}
