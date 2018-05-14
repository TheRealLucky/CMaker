package i15091.project.cmaker;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.File;

import java.util.*;

public class CSVHandler {

    //Write in CSV file
    public static void write(String path_to_csv, String value_name, String value_path){
        FileWriter file_writer = null;
        try{
            file_writer = new FileWriter(path_to_csv, true);

            if(!isEmpty(path_to_csv)){
                file_writer.append(NEWLINE_SEPERATOR);
            }
            file_writer.append(value_name);
            file_writer.append(SPLITTER);
            file_writer.append(value_path);

        } catch(IOException e){
            e.printStackTrace();
        } finally {
            try{
                file_writer.flush();
                file_writer.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }


    //Read CSV file
    public static LinkedList<String[]> read(String path_to_csv) {
        LinkedList<String[]> res = new LinkedList<String[]>();
        BufferedReader br = null;
        try{
            br = new BufferedReader(new FileReader(path_to_csv));

            String line;
            while((line = br.readLine()) != null) {
                String[] data = line.split(SPLITTER, -1);
                res.add(data);
            }
            br.close();
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            return res;
        }
    }


    //This method will remove all those names and paths out of the file, that were deleted or moved
    public static void removeRecents(String path_to_csv) {
        FileWriter writer = null;
        try{
            LinkedList<String[]> res = read(path_to_csv);
            writer = new FileWriter(path_to_csv, false);
            for(String[] text : res) {
                File file = new File(text[1]);
                if(file.isDirectory()){
                    write(path_to_csv, text[0], text[1]);
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            try{
                writer.flush();
                writer.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }


    //Read a standard text file
    public static String readFile(String path_to_file) {
        StringBuffer content = new StringBuffer();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path_to_file));
            String line;
            while((line = reader.readLine()) != null) {
                content.append(line);
                content.append(NEWLINE_SEPERATOR);
            }
            reader.close();
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }
        return content.toString();
    }


    //Helper method to check if file is empty
    private static boolean isEmpty(String path_to_csv) {
        try{
            BufferedReader reader = new BufferedReader(new FileReader(path_to_csv));
            if(reader.readLine() == null){
                reader.close();
                return true;
            }
            else{
                reader.close();
                return false;
            }
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static final String NEWLINE_SEPERATOR = "\n";
    private static final String SPLITTER = ",";
}