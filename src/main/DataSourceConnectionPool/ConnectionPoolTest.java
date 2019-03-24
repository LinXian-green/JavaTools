/*
package DataSourceConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConnectionPoolTest {
    public static void main(String[] args){
        String sql = " select id from user";
        try {
            ConnectionPool pool = ConnectionPool.getInstance();
            Connection conn = pool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
*/
