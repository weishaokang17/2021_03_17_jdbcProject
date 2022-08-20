package per.wsk.$02;


import org.junit.Test;
import per.wsk.util.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * @Author wb_weishaokang
 * @date 2021/3/24 4:45 下午
 * @description
 *
 * 查询、修改、删除天然有批量操作的功能。
 * 所以平时说的批量操作都是批量插入。
 *
 *
 * 先创建商品表：
 * CREATE TABLE goods(
 * 	id INT PRIMARY KEY AUTO_INCREMENT,
 * 	NAME VARCHAR(25)
 * );
 *
 *
 *
 *  方式一：使用Statement
 * Connection conn = JDBCUtils.getConnection();
 * Statement st = conn.createStatement();
 * for(int i = 1;i <= 200000;i++){
 * 		String sql = "insert into goods(name)values('name_" + i + "')";
 * 		st.execute(sql);
 * }
 *
 *
 *
 *
 *
 */
public class BatchInsertTest {

    //批量插入的方式二：使用PreparedStatement
    @Test
    public void testInsert1() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {

            long start = System.currentTimeMillis();

            conn = JDBCUtil.getConnection();
            String sql = "insert into goods(name)values(?)";
            //预编译该sql
            ps = conn.prepareStatement(sql);

            for(int i = 1;i <= 200000;i++){
                ps.setObject(1, "name_" + i);

                ps.executeUpdate();
            }

            long end = System.currentTimeMillis();

            System.out.println("花费的时间为：" + (end - start) +" 毫秒");//61597 毫秒
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            JDBCUtil.closeResource(conn, ps);
        }
        /*
        该方案优于Statement，因为200000条数据但只有一个sql字符串，statement是有200000条sql字符串，而且该方案
        会预编译，只编译一次，检查一次sql语法，statement是每条sql都要重新编译。
         */
    }



    /*
     * 批量插入的方式三：
     * 1.addBatch()、executeBatch()、clearBatch()
     * 2.mysql服务器默认有可能是关闭批处理的，我们需要通过一个参数，让mysql开启批处理的支持。
     * 		 ?rewriteBatchedStatements=true 写在配置文件的url后面
     *
     */
    @Test
    public void testInsert2() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {

            long start = System.currentTimeMillis();

            conn = JDBCUtil.getConnection();
            String sql = "insert into goods(name)values(?)";
            //预编译sql
            ps = conn.prepareStatement(sql);

            for(int i = 1;i <= 200000;i++){
                ps.setObject(1, "name_" + i);

                ps.addBatch();
                if (i % 1000 == 0) {
                    //1.执行
                    ps.executeBatch();
                    //2.清空批量
                    ps.clearBatch();
                }
            }

            long end = System.currentTimeMillis();

            System.out.println("花费的时间为：" + (end - start) +" 毫秒");//59763 毫秒
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            JDBCUtil.closeResource(conn, ps);
        }
    }


    @Test
    public void testInsert3() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {

            long start = System.currentTimeMillis();

            conn = JDBCUtil.getConnection();
            //设置不允许自动提交数据
            conn.setAutoCommit(false);

            String sql = "insert into goods(name)values(?)";
            ps = conn.prepareStatement(sql);


            for(int i = 1;i <= 200000;i++){
                ps.setObject(1, "name_" + i);

                ps.addBatch();
                if (i % 1000 == 0) {
                    //1.执行
                    ps.executeBatch();
                    //2.清空批量
                    ps.clearBatch();
                }
            }
            //提交数据
            conn.commit();

            long end = System.currentTimeMillis();

            System.out.println("花费的时间为：" + (end - start) +" 毫秒");// 16395 毫秒
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            JDBCUtil.closeResource(conn, ps);
        }
    }







}
