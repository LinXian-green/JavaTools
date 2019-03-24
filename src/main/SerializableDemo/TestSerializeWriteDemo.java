package SerializableDemo;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class TestSerializeWriteDemo {

    public static void main(String[] args) {
        String localPath = "C://a.txt";

        User user1 = new User("Lcc",21);
        Group group1 = new Group(5,"yh");
        user1.setGroup(group1);

        User user2 = new User("LX",19);
        Group group2 = new Group(10,"gy");
        user2.setGroup(group2);

        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(new FileOutputStream(localPath));
            objectOutputStream.writeObject(user1);
            objectOutputStream.writeObject(user2);
            objectOutputStream.writeInt(666);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                objectOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Write Object to path:"+localPath);
    }

}
