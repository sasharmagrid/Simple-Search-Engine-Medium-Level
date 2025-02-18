package org.example;
import org.example.SearchEngine.Search;

import java.io.File;

public class App 
{
    public static void main( String[] args )
    {
        Search search = new Search();
        if(args[0].equals("--data")) {
            File file = new File(args[1]);
//        String filePath = "src/main/java/org/example/names.txt";
//        File file = new File(filePath);
            search.enterDetails(file);
            search.chooseOption();
        }
    }
}
