import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class AppTest {
    public static void main(String[] args){
        Multimap map = HashMultimap.create();
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++)
                map.put(""+i,j);
        }
        for(int i=0;i<10;i++){
            System.out.println("key:"+i+",value:"+map.get(i));
        }

    }
}
