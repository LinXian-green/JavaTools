package DataSourceConnectionPool;

import java.sql.*;
import java.util.Map;
import java.util.ResourceBundle;

public class MyConnectionUtil {
    private String url;
    private String username;
    private String password;
    private String driverClassName;



    public static Map<String,Connection> getDefaultDatabase(){
        ResourceBundle bundle = ResourceBundle.getBundle("DbConfig.properties");
        String driverClassName = bundle.getString("hive.driverClassName");
        String url = bundle.getString("hive.url");
        String username = bundle.getString("hive.username");
        String password = bundle.getString("hive.password");

        try {
            Class.forName(driverClassName);
            Connection conn = DriverManager.getConnection(url,username,password);
            PreparedStatement pstmt = conn.prepareStatement("select url,username,password,driverClassName from datasource ");
            ResultSet set = pstmt.executeQuery();
            if(set.next()){
                driverClassName = set.getString("driverClassName");
                url = set.getString("url");
                username = set.getString("username");
                password = set.getString("password");
                Connection dbConnection = createConnection(driverClassName,url,username,password);


            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //创建数据库连接
    public  static Connection createConnection(String driverClassName,String url,String username,String password){
        try {
            Class.forName(driverClassName);
            return DriverManager.getConnection(url,username,password);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //创建数据库连接
    public Connection createConnection(String dbName){
        try {
            Class.forName(driverClassName);
            return DriverManager.getConnection(url,username,password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    private void createConnections(int numConnections) throws SQLException {
        // 循环创建指定数目的数据库连接
        for (int x = 0; x < numConnections; x++) {
            // 是否连接池中的数据库连接的数量己经达到最大？最大值由类成员 maxConnections
            // 指出，如果 maxConnections 为 0 或负数，表示连接数量没有限制。
            // 如果连接数己经达到最大，即退出。
            if (this.maxConnections > 0
                    && this.connections.size() >= this.maxConnections) {
                break;
            }
            // add a new PooledConnection object to connections vector
            // 增加一个连接到连接池中（向量 connections 中）
            try {
                connections.addElement(new PooledConnection(newConnection()));
            } catch (SQLException e) {
                System.out.println(" 创建数据库连接失败！ " + e.getMessage());
                throw new SQLException();
            }
            // System.out.println(" 数据库连接己创建 ......");
        }
    }

    /**
     * 创建一个新的数据库连接并返回它
     *
     * @return 返回一个新创建的数据库连接
     */
    private Connection newConnection() throws SQLException {
        // 创建一个数据库连接
        Connection conn = DriverManager.getConnection(dbUrl, dbUsername,
                dbPassword);
        // 如果这是第一次创建数据库连接，即检查数据库，获得此数据库允许支持的
        // 最大客户连接数目
        // connections.size()==0 表示目前没有连接己被创建
        if (connections.size() == 0) {
            DatabaseMetaData metaData = conn.getMetaData();
            int driverMaxConnections = metaData.getMaxConnections();
            // 数据库返回的 driverMaxConnections 若为 0 ，表示此数据库没有最大
            // 连接限制，或数据库的最大连接限制不知道
            // driverMaxConnections 为返回的一个整数，表示此数据库允许客户连接的数目
            // 如果连接池中设置的最大连接数量大于数据库允许的连接数目 , 则置连接池的最大
            // 连接数目为数据库允许的最大数目
            if (driverMaxConnections > 0
                    && this.maxConnections > driverMaxConnections) {
                this.maxConnections = driverMaxConnections;
            }
        }
        return conn; // 返回创建的新的数据库连接
    }
}
