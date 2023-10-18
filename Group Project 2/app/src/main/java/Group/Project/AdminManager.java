package Group.Project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class AdminManager {

    private static final String FILE_PATH = "src/main/resources/accounts.txt";

    public void view_all_users (){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add_a_user(){

    }

    public void delete_a_user(){

    }


}
