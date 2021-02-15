package com.example.campusdepartment.Utils;

/**
 * Created by 林嘉煌 on 2020/7/21.
 */


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Administrator
 *
 */
public class MysqlUtil {
    // 定义数据库连接参数
    public static final String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
    //要连接的数据库url,注意：此处连接的应该是服务器上的MySQl的地址
    public static final String URL = "jdbc:mysql://121.4.49.251:3306/ljh?useUnicode=true&characterEncoding=UTF-8";
    //连接数据库使用的用户名
    public static final String USERNAME = "root";
    //连接的数据库时使用的密码
    public static final String PASSWORD = "!@#123Qaz";
    //Demo123456!
    // 注册数据库驱动
    public static void drive(){
        try {
            Class.forName(DRIVER_CLASS_NAME);
        } catch (ClassNotFoundException e) {
            System.out.println("注册失败！");
            e.printStackTrace();
        }
    }
//    static {
//
//    }

    // 获取连接
    public static Connection getConn() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    // 关闭连接
    public static void closeConn(Connection conn) {
        if (null != conn) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("关闭连接失败！");
                e.printStackTrace();
            }
        }
    }
    //测试
    public static void main(String[] args) throws SQLException {
        System.out.println(MysqlUtil.getConn());
    }

}