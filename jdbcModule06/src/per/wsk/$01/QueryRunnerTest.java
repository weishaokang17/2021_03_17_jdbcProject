package per.wsk.$01;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.*;
import org.junit.Test;
import per.wsk.bean.Customer;
import per.wsk.util.JDBCUtil;

import java.sql.Connection;
import java.sql.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @Author wb_weishaokang
 * @date 2021/4/1 9:48 上午
 * @description
 *
 *
 * commons-dbutils 这个jar包 是 Apache 组织提供的一个开源 JDBC工具类库，它是对JDBC的简单封装，学习成本极低，
 * 并且使用dbutils能极大简化jdbc编码的工作量，同时也不会影响程序的性能。
 */
public class QueryRunnerTest {

    /**
     * 添加数据
     */
    @Test
    public void test01(){
        QueryRunner runner = new QueryRunner();

        Connection connection = null;
        try {
            connection = JDBCUtil.getConnection();
            String sql = "insert into customers (name,email,birth) values (?,?,?)";
            int insertCount = runner.update(connection, sql, "绿巨人", "1234@qq.com", "1997-02-04");

            System.out.println("共 " + insertCount + " 记录添加成功");
        } catch (Exception e) {
            e.printStackTrace();
        }

        JDBCUtil.closeResource(connection,null);
    }


    /**
     * 查询一条数据
     * BeanHandler
     */
    @Test
    public void testQuery01(){
        QueryRunner runner = new QueryRunner();
        Connection connection = null;
        try {
            connection = JDBCUtil.getConnection();
            BeanHandler<Customer> beanHandler = new BeanHandler<>(Customer.class);
            String sql = "select id,name,email,birth from customers where id = ?";

            Customer resultBean = runner.query(connection, sql, beanHandler, 19);

            System.out.println(resultBean);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JDBCUtil.closeResource(connection,null);
    }


    /**
     * 查询多条数据
     * BeanListHandler
     */
    @Test
    public void testQuery02(){
        QueryRunner runner = new QueryRunner();
        Connection connection = null;
        try {
            connection = JDBCUtil.getConnection();
            BeanListHandler<Customer> beanListHandler = new BeanListHandler<>(Customer.class);
            String sql = "select * from customers where id < ?";

            List<Customer> list = runner.query(connection, sql, beanListHandler, 19);

            list.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JDBCUtil.closeResource(connection,null);
    }


    /**
     * 查询一条数据
     * MapHandler
     */
    @Test
    public void testQuery03(){
        QueryRunner runner = new QueryRunner();
        Connection connection = null;
        try {
            connection = JDBCUtil.getConnection();

            MapHandler mapHandler = new MapHandler();
            String sql = "select id,name,email,birth from customers where id = ?";

            Map<String, Object> map = runner.query(connection, sql, mapHandler, 19);

            System.out.println(map);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JDBCUtil.closeResource(connection,null);
    }



    /**
     * 查询多条数据
     * MapListHandler
     */
    @Test
    public void testQuery04(){
        QueryRunner runner = new QueryRunner();
        Connection connection = null;
        try {
            connection = JDBCUtil.getConnection();
            MapListHandler mapListHandler = new MapListHandler();
            String sql = "select * from customers where id < ?";

            List<Map<String, Object>> mapList = runner.query(connection, sql, mapListHandler, 19);

            mapList.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JDBCUtil.closeResource(connection,null);
    }


    /**
     * 查询特殊的某些数据
     * scalarHandler
     */
    @Test
    public void testQuery05(){
        QueryRunner runner = new QueryRunner();
        Connection connection = null;
        try {
            connection = JDBCUtil.getConnection();
            ScalarHandler<Long> scalarHandler = new ScalarHandler<>();
            String sql = "select count(*) from customers";

            Long count = runner.query(connection, sql, scalarHandler);

            System.out.println("表中共 " + count + " 条记录");
        } catch (Exception e) {
            e.printStackTrace();
        }

        JDBCUtil.closeResource(connection,null);
    }


    /**
     * 查询特殊的某些数据
     * scalarHandler
     */
    @Test
    public void testQuery06(){
        QueryRunner runner = new QueryRunner();
        Connection connection = null;
        try {
            connection = JDBCUtil.getConnection();
            ScalarHandler<Date> scalarHandler = new ScalarHandler<>();
            String sql = "select max(birth) from customers";

            Date maxDate = runner.query(connection, sql, scalarHandler);

            System.out.println("最大生日是 : " + maxDate);// 2014-01-17
        } catch (Exception e) {
            e.printStackTrace();
        }

        JDBCUtil.closeResource(connection,null);
    }


    /**
     * 自定义ResultSetHandler
     */
    @Test
    public void testQuery07(){
        QueryRunner runner = new QueryRunner();
        Connection connection = null;
        try {
            connection = JDBCUtil.getConnection();

            String sql = "select id,name,email,birth from customers where id < ?";

            /*ResultSetHandler<List<Customer>> handler = new ResultSetHandler<List<Customer>>(){
                @Override
                public List<Customer> handle(ResultSet resultSet) throws SQLException {
                    return null;
                }
            };*/
            ResultSetHandler<List<Customer>> handler = (resultSet -> {
                List<Customer> list = new LinkedList<>();
                while (resultSet.next()){
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String email = resultSet.getString("email");
                    Date birth = resultSet.getDate("birth");

                    Customer customer = new Customer(id, name, email, birth);
                    list.add(customer);
                }
                return list;
            });
            //此处获取的resultList这个结果就是上面匿名实现类里面返回的list这个结果
            List<Customer> resultList = runner.query(connection, sql, handler,20);

            resultList.forEach(System.out::println);

        } catch (Exception e) {
            e.printStackTrace();
        }

//        JDBCUtil.closeResource(connection,null);
        //点开上面的runner.query方法看下就知道了，里面已经对resultSet和statement进行了关闭，此处只关闭连接即可。
        JDBCUtil.closeResource(null,connection,null);
    }

}
