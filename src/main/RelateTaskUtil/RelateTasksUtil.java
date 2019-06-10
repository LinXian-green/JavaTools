package RelateTaskUtil;

import DataSourceConnectionPool.MyConnectionUtil;
import org.apache.commons.lang.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
* 获得所有相互依赖的任务
*
* Task表格式如下:
*
* id    name     dependTaskId
* 1     任务1         3
* 2     任务2         1，3
* 3     任务3
* 4     任务3         2
*
* 下面工具类实现随意输入一个任务Id,获得所有它依赖和被它依赖的任务
*
* */
public class RelateTasksUtil {

    private String driver="com.jdbc.sql.driver";
    private String url = "url";
    private String username = "root";
    private String password = "";

    public Set<Integer> getRelateTasks(int taskId, HashSet<Integer> resultSet) throws SQLException {
        HashSet<String> tasksList = null;
        Connection conn = MyConnectionUtil.createConnection(driver,url,username,password);
        String sql = " select id,dependTaskId from analyse_task where id="+taskId+" union select id,dependTaskId from analyse_task where dependTaskId='"+taskId+"'";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            int id = rs.getInt("id");
            String dependTaskId = rs.getString("dependTaskId");
            tasksList.add(StringUtils.valueOf(id));
            tasksList.addAll(Arrays.asList(dependTaskId.split(",")));
        }

        if(tasksList.size()==0)
            return resultSet;

        for(String task:tasksList){
            if(!resultSet.contains(task)){
                getRelateTasks(Integer.parseInt(task),resultSet);
            }
        }

    }
}
