package per.wsk.$01;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import per.wsk.bean.Customer;
import per.wsk.util.JDBCUtil;

import java.io.*;
import java.sql.*;

/**
 * @Author wb_weishaokang
 * @date 2021/3/24 10:07 上午
 * @description
 */
public class BlobTest {

    /*
    MySQL 的 BLOB类型：
    （1）MySQL中，BLOB是一个二进制大型对象，是一个可以存储大量数据的容器，它能容纳不同大小的数据。
    （2）插入BLOB类型的数据必须使用PreparedStatement，因为BLOB类型的数据无法使用字符串拼接写的。
    （3）MySQL的四种BLOB类型(这四种类型除了存储的最大信息量不同外，其他方面都一样)
        类型          最大存储量
        TinyBlob        255字节
        Blob            65k
        MediumBlob      16M
        LongBlob        4G

        ①实际使用中根据需要存入的数据大小定义不同的BLOB类型。
        ②需要注意的是：如果存储的文件过大，数据库的性能会下降。
        ③如果在指定了相关的Blob类型以后，还报错：xxx too large，那么在mysql的安装目录下，找my.ini文件加上如
        下的配置参数： max_allowed_packet=16M。同时注意：修改了my.ini文件之后，需要重新启动mysql服务。
     */


    /**
     * 添加图片
     */
    @Test
    public void test01() throws Exception{
        Connection connection = JDBCUtil.getConnection();
        String sql = "insert into customers (name,email,birth,photo) values (?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1,"美国队长");
        preparedStatement.setString(2,"123456@qq.com");
        preparedStatement.setString(3,"1925-01-04");

        InputStream inputStream = new FileInputStream(new File("./photo/美国队长.jpeg"));

        preparedStatement.setBlob(4,inputStream);

        preparedStatement.executeUpdate();

        inputStream.close();
        JDBCUtil.closeResource(connection,preparedStatement);

    }


    /**
     * 查询图片
     */
    @Test
    public void test02() throws Exception {
        Connection connection = JDBCUtil.getConnection();
        String sql = "select id,name,email,birth,photo from customers where name = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1,"美国队长");

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            /*
            方式一：
            int id = resultSet.getInt(1);
            String name = resultSet.getString(2);
            String email = resultSet.getString(3);
            Date birth = resultSet.getDate(4);

            */
            //方式二：
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String email = resultSet.getString("email");
            Date birth = resultSet.getDate("birth");

            Customer customer = new Customer(id, name, email, birth);
            System.out.println(customer);

            //将Blob类型的数据下载下来，以文件的方式保存在本地
            Blob photo = resultSet.getBlob("photo");
            InputStream photoInputStream = photo.getBinaryStream();


            File file = new File("./photo/captainAmerica.jpeg");
            FileUtils.copyInputStreamToFile(photoInputStream,file);

            photoInputStream.close();
        }

        JDBCUtil.closeResource(resultSet,connection,preparedStatement);

    }

}
