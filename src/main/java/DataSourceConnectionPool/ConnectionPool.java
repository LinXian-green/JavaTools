package DataSourceConnectionPool;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ConnectionPool {
    private List<Connection> pool;
    private String url;
    private String username;
    private String password;
    private String driverClassName;
    private int initPoolSize = 1;
    private static ConnectionPool instance = null;

    private ConnectionPool(){
        init();
    }

    private void init() {
        readConfig();
        pool = new ArrayList<Connection>(initPoolSize);
        addConnection();
    }

    public synchronized  void release(Connection conn){
        pool.add(conn);
    }

    //close DBSource Connection
    public synchronized  void closePool(){
        try {
            for(int i = 0; i<pool.size();i++) {
                pool.get(i).close();
                pool.remove(i);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ConnectionPool getInstance(){
        if(instance == null){
            instance = new ConnectionPool();
        }
        return instance;
    }

    public static ConnectionPool getInstance(int initPoolSize){
        if(instance == null){
            instance = new ConnectionPool();
        }
        return instance;
    }

    //返回连接池中的数据库连接
    public synchronized Connection getConnection(){
        if(pool.size() > 0){
            Connection conn  = pool.get(0);
            pool.remove(conn);
            return conn;
        }else{
            return null;
        }
    }

    //创建数据库连接
    private void addConnection(){
        Connection conn = null;
        for(int i = 0; i < initPoolSize; i++){
            try {
                Class.forName(driverClassName);
                conn = DriverManager.getConnection(url,username,password);
                pool.add(conn);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //读取数据库连接池的属性文件
    private void readConfig(){
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("DbConfig.properties");
            this.driverClassName = bundle.getString("driverClassName");
            this.url = bundle.getString("url");
            this.username = bundle.getString("username");
            this.password = bundle.getString("password");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
