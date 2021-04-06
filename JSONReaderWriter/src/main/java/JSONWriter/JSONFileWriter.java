package JSONWriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import token.Token;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JSONFileWriter{

    public static void tokenListToJSON(List<Token> tokenList, String testName) {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            Writer writer = new FileWriter(testName+".txt");
            gson.toJson(tokenList,writer);
            writer.close();
            System.out.println("Successfully Copied JSON Object to File...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Token> fileJSONToTokenList(String testName){
        Gson gson = new Gson();
        try {
            Reader reader = new FileReader(testName+".txt");
            List<Token> result = Arrays.asList(gson.fromJson(reader,Token[].class));
            return result;
        } catch (FileNotFoundException e) {
            return new ArrayList<Token>();
        }
    }

}