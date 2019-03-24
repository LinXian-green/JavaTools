package SerializableDemo;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class TestSerializeReadDemo {
    public static void main(String[] args) {


        String localPath = "C://a.txt";
        ObjectInputStream objectInputStream = null;
        User user1, user2 = null;
        int count;
        {
            try {
                objectInputStream = new ObjectInputStream(new FileInputStream(localPath));
                user1 = (User) objectInputStream.readObject();
                user2 = (User) objectInputStream.readObject();
                count = objectInputStream.readInt();
                System.out.println(user1.toString());
                System.out.println(user1.getGroup());
                System.out.println(user2.toString());
                System.out.println(user2.getGroup());
                System.out.println(count);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }
}
