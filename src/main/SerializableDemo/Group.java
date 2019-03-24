package SerializableDemo;

import java.io.Serializable;

public class Group implements Serializable {

    private static String groupName;
    private int count;

    public Group(int count,String groupName) {
        this.count = count;
        this.groupName = groupName;
    }

    @Override
    public String toString() {
        return "Group{" +
                "count=" + count + ",count:" +count+
                '}';
    }
}
