/*
import DataSourceConnectionPool.MyConnectionUtil;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.sql.*;
import java.util.*;

public class ConnectionPool {
    private int minConnections = 10; // 连接池的初始大小
    private int incrementalConnections = 5;// 连接池自动增加的大小
    private int maxConnections = 50; // 连接池最大的大小
    private int dbSourceSize = 10;//数据源数量
    private int dbSourceMinConnections = 5;
    private Multimap<String,MyConnection> connectionPool = null;
    // 它中存放的对象为 PooledConnection 型

    */
/**
     * 返回连接池的初始大小
     *
     * @return 初始连接池中可获得的连接数量
     *//*

    public int getMinConnections() {
        return this.minConnections;
    }

    */
/**
     *
     * 创建一个数据库连接池，连接池中的可用连接的数量采用类成员 initialConnections 中设置的值
     *//*


    public synchronized void initPool() throws Exception {

        if (connectionPool != null) {
            return; // 如果己经创建，则返回
        }
        connectionPool = HashMultimap.create(dbSourceSize,minConnections);

        ResourceBundle bundle = ResourceBundle.getBundle("DbConfig.properties");
        String driverClassName = bundle.getString("hive.driverClassName");
        String url = bundle.getString("hive.url");
        String username = bundle.getString("hive.username");
        String password = bundle.getString("hive.password");

        try {
            Class.forName(driverClassName);
            Connection conn = DriverManager.getConnection(url,username,password);
            PreparedStatement pstmt = conn.prepareStatement("select dbSourceName,url,username,password,driverClassName from datasource ");
            ResultSet set = pstmt.executeQuery();
            if(set.next()){
                String dbSourceName = set.getString("dbSourceName");
                driverClassName = set.getString("driverClassName");
                url = set.getString("url");
                username = set.getString("username");
                password = set.getString("password");
                Connection dbConnection = MyConnectionUtil.createConnection(driverClassName,url,username,password);

                for(int i=0;i<dbSourceMinConnections;i++){
                    connectionPool.put(dbSourceName,new MyConnection(dbConnection,false));
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    */
/**
     * 通过调用 getFreeConnection() 函数返回一个可用的数据库连接 , 如果当前没有可用的数据库连接，并且更多的数据库连接不能创
     * 建（如连接池大小的限制），此函数等待一会再尝试获取。
     *
     * @return 返回一个可用的数据库连接对象
     *//*


    public synchronized Connection getConnection(String dbSourceName) throws SQLException {
        // 确保连接池己被创建
        if (connectionPool.size() < 1) {
            return null; // 连接池还没创建，则返回 null
        }
        Connection conn = getFreeConnection(dbSourceName); // 获得一个可用的数据库连接
        // 如果目前没有可以使用的连接，即所有的连接都在使用中
        while (conn == null) {
            // 等一会再试
            // System.out.println("Wait");
            wait(250);
            conn = getFreeConnection(); // 重新再试，直到获得可用的连接，如果
            // getFreeConnection() 返回的为 null
            // 则表明创建一批连接后也不可获得可用连接
        }
        return conn;// 返回获得的可用的连接
    }

    */
/**
     * 本函数从连接池向量 connections 中返回一个可用的的数据库连接，如果 当前没有可用的数据库连接，本函数则根据
     * incrementalConnections 设置 的值创建几个数据库连接，并放入连接池中。 如果创建后，所有的连接仍都在使用中，则返回 null
     *
     * @return 返回一个可用的数据库连接
     *//*

    private Connection getFreeConnection(String dbSourceName) throws SQLException {
        // 从连接池中获得一个可用的数据库连接
        Connection conn = findFreeConnection();
        if (conn == null) {
            // 如果目前连接池中没有可用的连接
            // 创建一些连接
            createConnections(incrementalConnections);
            // 重新从池中查找是否有可用连接
            conn = findFreeConnection();
            if (conn == null) {
                // 如果创建连接后仍获得不到可用的连接，则返回 null
                return null;
            }
        }
        return conn;
    }

    */
/**
     * 查找连接池中所有的连接，查找一个可用的数据库连接， 如果没有可用的连接，返回 null
     *
     * @return 返回一个可用的数据库连接
     *//*


    private Connection findFreeConnection(String dbSourceName) throws SQLException {
        Connection conn = null;
        // 获得连接池向量中所有的对象
        try {
        Collection<MyConnection> connections = connectionPool.get("dbSourceName");
        // 查看该数据源是否有空闲的连接
        for(MyConnection myConn :connections)
            if (!myConn.isUsed) {
                // 如果该连接对象未被使用
                conn = myConn.getConnection();
                if(!conn.isClosed()) {
                    myConn.setIsUsed(true);
                    break;
                }else{

                }
            }
        } catch (SQLException e) {
            System.out.println(" 创建数据库连接失败！ " + e.getMessage());
            return null;
        }
        return conn;// 返回找到到的可用连接
    }

    */
/**
     * 此函数返回一个数据库连接到连接池中，并把此连接置为空闲。 所有使用连接池获得的数据库连接均应在不使用此连接时返回它。
     *
     * @param 需返回到连接池中的连接对象
     *//*


    public void returnConnection(Connection conn) {
        // 确保连接池存在，如果连接没有创建（不存在），直接返回
        if (connections == null) {
            System.out.println(" 连接池不存在，无法返回此连接到连接池中 !");
            return;
        }
        PooledConnection pConn = null;
        Enumeration enumerate = connections.elements();
        // 遍历连接池中的所有连接，找到这个要返回的连接对象
        while (enumerate.hasMoreElements()) {
            pConn = (PooledConnection) enumerate.nextElement();
            // 先找到连接池中的要返回的连接对象
            if (conn == pConn.getConnection()) {
                // 找到了 , 设置此连接为空闲状态
                pConn.setBusy(false);
                break;
            }
        }
    }

    */
/**
     * 关闭连接池中所有的连接，并清空连接池。
     *//*


    public synchronized void closeConnectionPool() throws SQLException {
        // 确保连接池存在，如果不存在，返回
        if (connections == null) {
            System.out.println(" 连接池不存在，无法关闭 !");
            return;
        }
        PooledConnection pConn = null;
        Enumeration enumerate = connections.elements();
        while (enumerate.hasMoreElements()) {
            pConn = (PooledConnection) enumerate.nextElement();
            // 如果忙，等 5 秒
            if (pConn.isBusy()) {
                wait(5000); // 等 5 秒
            }
            // 5 秒后直接关闭它
            closeConnection(pConn.getConnection());
            // 从连接池向量中删除它
            connections.removeElement(pConn);
        }
        // 置连接池为空
        connections = null;
    }

    */
/**
     * 关闭一个数据库连接
     *
     * @param 需要关闭的数据库连接
     *//*


    private void closeConnection(Connection conn) {
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println(" 关闭数据库连接出错： " + e.getMessage());
        }
    }
    */
/**
     * 使程序等待给定的毫秒数
     *
     * @param 给定的毫秒数
     *//*


    private void wait(int mSeconds) {
        try {
            Thread.sleep(mSeconds);
        } catch (InterruptedException e) {
        }
    }
    */
/**
     *
     * 内部使用的用于保存连接池中连接对象的类 此类中有两个成员，一个是数据库的连接，另一个是指示此连接是否 正在使用的标志。
     *//*


    class MyConnection implements Cloneable{
        Connection connection = null;// 数据库连接
        boolean isUsed = false; // 此连接是否正在使用的标志，默认没有正在使用

        public MyConnection clone(MyConnection conn){
            MyConnection o = null;
            try{
                o = (MyConnection) super.clone();
            }catch(CloneNotSupportedException e){
                e.printStackTrace();
            }
            return o;
        }

        // 构造函数，根据一个 Connection 构告一个 PooledConnection 对象
        public MyConnection(Connection connection,boolean isUsed) {
            this.connection = connection;
        }

        // 返回此对象中的连接
        public Connection getConnection() {
            return connection;
        }

        // 设置此对象的，连接
        public void setConnection(Connection connection) {
            this.connection = connection;
        }

        // 获得对象连接是否忙
        public boolean isUsed() {
            return isUsed;
        }

        // 设置对象的连接正在忙
        public void setIsUsed(boolean busy) {
            this.isUsed = busy;
        }
    }
}
*/
