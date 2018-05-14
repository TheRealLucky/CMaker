package i15091.project.cmaker;

import java.io.*;
import java.util.*;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.FileHandler;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;

public class Maker {

    public final static Logger LOGGER = Logger.getLogger(Maker.class.getName());

    public Maker() {
        LOGGER.addHandler(CMLogger.fileHandler);
        LOGGER.setLevel(Level.ALL);
    }


    //Checks if given directory contains src, build and CMakeLists.txt
    public static boolean checkProjectFolder(File dir){
        if(dir.exists() && dir.isDirectory()){
            CMLogger.logInfo(LOGGER, "Found project folder!");
            File src = new File(new String(dir.getAbsolutePath() + "/src"));
            if(src.exists() && src.isDirectory()){
                CMLogger.logInfo(LOGGER, "Found source folder!");
                File build = new File(new String(dir.getAbsolutePath() + "/build"));
                if(build.exists() && build.isDirectory()){
                    CMLogger.logInfo(LOGGER, "Found build folder!");
                    File lists = new File(new String(dir.getAbsolutePath() + "/CMakeLists.txt"));
                    if(lists.exists()){
                        CMLogger.logInfo(LOGGER, "Found CMakeLists.txt!");
                        return true;
                    }
                }
            }
        }
        return false;
    }


    //Create new CMaker project
    public void createProject(String name, String path, boolean junit_enabled
                                         , boolean create_entry_point) throws ProjectExistsException,
                                                                              CouldNotCreateProjectException {
        if (project == null) {
            project = new Project(name, path);
            project.junit_enabled = junit_enabled;
            project.create_entry_point = create_entry_point;
            try{
                project.createProject();
            } catch(CouldNotCreateProjectException e){
                CMLogger.logSevere(LOGGER, "Failed to create Project!");
                throw new CouldNotCreateProjectException("Failed to create Project");
            }
        }
        else{
            CMLogger.logSevere(LOGGER, "Project already exists!");
            throw new ProjectExistsException("Project already exists!");
        }
    }


    //Open a existing CMaker project
    public void openProject(String path) throws PathNotFoundException {
        CMLogger.logInfo(LOGGER, "Opening project...");
        if( Maker.checkProjectFolder(new File(path)) ) {
            try {
                Properties prop = new Properties();
                String cmaker_path = new String(path + "/CMaker.properties");
                InputStream input = new FileInputStream(cmaker_path);

                prop.load(input);

                boolean junit_enabled;
                if(prop.getProperty("junit_enabled").equals("true")) {
                    junit_enabled = true;
                }
                else{
                    junit_enabled = false;
                }
                if(!prop.getProperty("path").equals(path)){
                    input.close();
                    OutputStream output = new FileOutputStream(cmaker_path);
                    prop.setProperty("path", path);
                    prop.store(output, null);
                    output.close();
                    input = new FileInputStream(cmaker_path);
                    prop.load(input);
                    clearBuildFolder(path);
                }
                String new_path = prop.getProperty("path");
                project = new Project(prop.getProperty("name"), new_path.substring(0, new_path.length() - prop.getProperty("name").length() - 1));
                project.junit_enabled = junit_enabled;

            } catch (IOException io) {
                io.printStackTrace();
                CMLogger.logInfo(LOGGER, "Properties file not found!");
            }
        }
        else {
            CMLogger.logInfo(LOGGER, "Project path not found!");
            throw new PathNotFoundException("Project path not found !");
        }
    }


    //Used to clear the build directory after the cmake path has changed
    public void clearBuildFolder(String path) {
        CMLogger.logInfo(LOGGER, "Clearing build folder...");
        if(new File(path + "/build").exists()){
            CMConsole.execute(new String(path + "/build"), "rm CMakeCache.txt");
        }
    }


    //Get the java files of the project directory
    public LinkedList<String> getFiles(String dir) {
        LinkedList<String> filenames = new LinkedList<String>();
        
        if(project != null) {
            File[] files = new File(project.getPath() + "/" + project.getName() + "/" + dir).listFiles();
            if(files != null){
                for(File file : files){
                    String name = file.getName();
                    if(file.isFile() && name.length() >= 6){ 
                        if(name.substring(name.length()-5, name.length()).equals(".java") ) {
                            filenames.add(name);
                        }
                    }
                }
                return filenames;
            }
        }
        else {
            return null;
        }
        return null;
    }

    public static void deleteProject(String path_to_project, String project_name) {
        CMLogger.logInfo(LOGGER, "Deleting project...");
        CMConsole.execute(path_to_project, new String("rm -r " + project_name));
    }

    public void addSourceFile(String name) {
        CMLogger.logInfo(LOGGER, "Adding source file to project...");
        if(name == null) {
            throw new IllegalArgumentException();
        }
        try{
            String path = null;
            File file = null;
            if(name.length() >= 6){
                if(name.substring(name.length()-5, name.length()).equals(".java")){
                    name = name.substring(0, name.length()-5);
                }
            }

            path = new String(getPath() + "/src/" + name + ".java");
            file = new File(path);

            file.createNewFile();

            FileWriter writer = new FileWriter(path, true);

            writer.append("public class " + name + " {");
            writer.append("}");
            //TAB = \t

            writer.flush();
            writer.close();

        } catch(IOException e) {
            e.printStackTrace();
        }
    }


    public Project getProject(){
        return project;
    }


    public void clearProject() {
        project = null;
    }

    public String getPath() {
        return new String(project.getPath() + "/" + project.getName());
    }


    private Project project;
    private Handler fileHandler;

    //File writing purposes
    private static final String NEWLINE_SEPERATOR = "\n";
}