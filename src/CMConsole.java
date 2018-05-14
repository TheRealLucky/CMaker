package i15091.project.cmaker;

import java.util.*;
import java.io.*;

public class CMConsole {

    public static int execute(String path, String command) {
        try{
            Process process = Runtime.getRuntime().exec(command, null, new File(path));
            BufferedReader output;
            
            StringBuilder res = new StringBuilder();
            String line;

            if(process.waitFor() != 0){
                output = new BufferedReader(new InputStreamReader(process.getErrorStream()));

                while ((line = output.readLine()) != null) {
                    res.append(line);
                    res.append(System.getProperty("line.separator"));
                }

                output.close();
                error_msg = res.toString();
                return 1;
            }
            else{
                return 0;
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
        return 1;
    }

    public static String getOutput(String command) {
        StringBuilder output = null;
        try{
            Runtime rt = Runtime.getRuntime();
            Process pc = rt.exec(command);
            BufferedReader stream = new BufferedReader(new InputStreamReader(pc.getInputStream()));

            output = new StringBuilder();
            String line = null;
            while((line = stream.readLine()) != null) {
                output.append(line);
                output.append("\n");
            }
            
        } catch(IOException e) {
            e.printStackTrace();
        }

        return output.toString();
    }

    public static String error_msg = null;
}