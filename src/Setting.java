package i15091.project.cmaker;

import java.util.Properties;
import java.io.*;

public class Setting{

    public static void setHome(String path) {
        InputStream input = null;
        OutputStream output = null;
        try{
            File file = new File("config.properties");
            Properties prop = new Properties();
            input = new FileInputStream(file);

            prop.load(input);
            prop.setProperty("Home", path);

            output = new FileOutputStream("config.properties");
            prop.store(output, "Test");
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            try{
                output.close();
                input.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void setTheme(Theme theme) {
        try{
            File file = new File("config.properties");
            if(file.exists() == false){
                file.createNewFile();
            }
            Properties prop = new Properties();
            InputStream input = new FileInputStream(file);
            if(input != null) {
                prop.load(input);
            }

            if(theme == Theme.BLUE){
                prop.setProperty("Theme", "blue");
            }
            if(theme == Theme.GREEN){
                prop.setProperty("Theme", "green");
            }
            if(theme == Theme.ORANGE){
                prop.setProperty("Theme", "orange");
            }

            OutputStream output = new FileOutputStream(file);
            prop.store(output, "CMaker config file");
            input.close();
            output.close();

        } catch(IOException io){
            io.printStackTrace();
        }
        
    }


    public static Theme getTheme() {
        try {
            File file = new File("config.properties");
            Properties prop = new Properties();
            InputStream input = new FileInputStream(file);

            prop.load(input);

            if(prop.getProperty("Theme").equals("blue")) {
                return Theme.BLUE;
            }
            else if(prop.getProperty("Theme").equals("green")) {
                return Theme.GREEN;
            }
            else if (prop.getProperty("Theme").equals("orange")){
                return Theme.ORANGE;
            }
            else {
                return null;
            }
        } catch (IOException io) {
            return null;
        }
    }

    public static String getHomePath() {
        String path = null;
        InputStream input = null;
        try{
            File file = new File("config.properties");
            Properties prop = new Properties();
            input = new FileInputStream(file);

            prop.load(input);

            path = prop.getProperty("Home");
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            try{
                input.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        return path;
    }
}