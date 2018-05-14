package i15091.project.cmaker;

import java.util.Properties;
import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.FileHandler;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;


public class Project{

    public final static Logger LOGGER = Logger.getLogger(Project.class.getName());

    Project(String name, String path) {
        this.name = name;
        this.path = path;
        
        LOGGER.addHandler(CMLogger.fileHandler);
        LOGGER.setLevel(Level.ALL);

    }

    public void createProject() throws CouldNotCreateProjectException {
        try{
            CMLogger.logInfo(LOGGER, "Creating Folder Structure...");
            createFolderStructure();
            if(create_entry_point){
                CMLogger.logInfo(LOGGER, "Creating Entry Point...");
                createEntryPoint();
            }
            CMLogger.logInfo(LOGGER, "Creating Lists File...");
            createListsFile();
            CMLogger.logInfo(LOGGER, "Creating Properties File...");
            createProperties();
            if(junit_enabled){
                CMLogger.logInfo(LOGGER, "Copying Test Files...");
                copyTestFiles();
            }
            CMLogger.logInfo(LOGGER, "Writing Project in csv file...");
            CSVHandler.write("../resources/data/Recents.csv", name, new String(path + "/" + name));
        } catch(CouldNotCreateProjectException e){
            CMLogger.logSevere(LOGGER, "Failed to create project");
            throw new CouldNotCreateProjectException("Failed to create project");
        }
    }

    private void copyTestFiles() {
        String test_dir = new String(path + "/" + name + "/tests");
        CMLogger.logInfo(LOGGER, "Copying Hamcrest File...");
        int exec = CMConsole.execute(".", 
            new String("cp ../tests/hamcrest-core-1.3.jar" + " " + test_dir));
        CMLogger.logInfo(LOGGER, "Copying JUnit File...");
        exec = CMConsole.execute(".", 
            new String("cp ../tests/junit-4.12.jar" + " " + test_dir));
    }

    private void createProperties() {
        try{
            Properties prop = new Properties();
            OutputStream output = new FileOutputStream(new String(path + "/" + name + "/CMaker.properties"));

            prop.setProperty("name", name);
            prop.setProperty("path", path);
            if(junit_enabled){
                prop.setProperty("junit_enabled", "true");
            }
            else {
                prop.setProperty("junit_enabled", "false");
            }

            prop.store(output, "CMaker config file");
            output.close();

        } catch(IOException io){
            io.printStackTrace();
            CMLogger.logSevere(LOGGER, "Failed to create properties!");
        }
    }


    private void createEntryPoint() {
        try{
            File main = new File(path + "/" + name + "/src/Main.java");
            FileWriter fw = new FileWriter(main.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("public class Main {");
            bw.newLine();
            bw.write("    public static void main(String[] args){");
            bw.newLine();
            bw.write("    }");
            bw.newLine();
            bw.write("}");
            bw.newLine();
            bw.close();

        } catch(IOException e){ 
            e.printStackTrace();
            CMLogger.logSevere(LOGGER, "Failed to create entry point!");
        }
    }

    //Create build, src and tests folder, as well as the projects main folder
    private void createFolderStructure() throws CouldNotCreateProjectException {
        try{
            String folder_path = new String(path + "/" + name);
            new File(folder_path).mkdir();                              //Create project folder
            new File(new String(folder_path +  "/build")).mkdir();      // create build folder
            new File(new String(folder_path + "/src")).mkdir();         // create src folder
            if(junit_enabled){
                new File(new String(folder_path + "/tests")).mkdir();   //create tests folder
            }
        } catch(Exception e){
            CMLogger.logSevere(LOGGER, "Failed to create Project");
            throw new CouldNotCreateProjectException("Failed to create project!");
        }
    }

    //Creates the CMakeLists.txt file
    private void createListsFile() {
        try{
            File cmake = new File(path + "/" + name + "/CMakeLists.txt");

            FileWriter fw = new FileWriter(cmake.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            // Write in file
            bw.write("#CMakeLists.txt created by CMaker!");
            bw.newLine();
            bw.write("cmake_minimum_required(VERSION 3.5)");
            bw.newLine();
            bw.write("project(" + name + " LANGUAGES Java)");
            bw.newLine();
            if(junit_enabled){
                bw.write("enable_testing()");
                bw.newLine();
            }
            bw.write("find_package(Java 1.7 REQUIRED COMPONENTS Development)");
            bw.newLine();
            bw.write("include(UseJava)");
            bw.newLine();
            bw.write("file(GLOB SOURCES \"src/*.java\")");
            bw.newLine();
            bw.write("add_jar(" + name + "Jar ${SOURCES}");
            if(create_entry_point){
                bw.write(" ENTRY_POINT Main.java");
            }
            bw.write(")");
            bw.close();

        } catch(Exception e) {
            e.printStackTrace();
            CMLogger.logSevere(LOGGER, "Failed to create CMakeLists.txt!");
        }
    }

    //Deprecated
    public void generateTests(String name) {
        LinkedList<String> temp = new LinkedList<String>();
        temp.add(name);
        generateTests(temp);
    }


    //Deprecated
    //Generate test files to according java files
    public void generateTests(LinkedList<String> files) {
        try{
            for(String file : files) {
                String filepath = new String(path + "/" + name);
                File testfile = new File(filepath + "/tests/Test" + file);
                File lists = new File(filepath + "/CMakeLists.txt");

                FileWriter fw;
                BufferedWriter bw;

                if(first_test) {
                    fw = new FileWriter(lists.getAbsoluteFile(), true);
                    bw = new BufferedWriter(fw);
                    bw.newLine();
                    bw.write("get_target_property(jarFile " + name + "Jar JAR_FILE)");
                    bw.newLine();
                    bw.write("file(GLOB TESTS \"tests/*.java\") ");
                    bw.newLine();
                    bw.write("set(ALL ${SOURCES} ${TESTS})");
                    bw.newLine();
                    bw.write("file(GLOB JUNIT_JAR_FILE \"tests/junit*.jar\")");
                    bw.newLine();
                    bw.write("add_jar(test" + name + " ${ALL} INCLUDE_JARS ${JUNIT_JAR_FILE}");
                    bw.newLine();
                    bw.write("        ENTRY_POINT TestMain)");
                    bw.newLine();
                    bw.write("get_target_property(junitJarFile test" + name + " JAR_FILE)");
                    bw.newLine();
                    bw.close();
                    fw.close();
                    first_test = false;
                }

                fw = new FileWriter(testfile.getAbsoluteFile());
                bw = new BufferedWriter(fw);

                bw.write("//Created by CMaker");
                bw.newLine();
                bw.write("import static org.junit.Assert.*;");
                bw.newLine();
                bw.write("import org.junit.Before;");
                bw.newLine();
                bw.write("import org.junit.Test;");
                bw.newLine();
                bw.write("public class Test" + file.substring(0, file.length()-5) + " {");
                bw.newLine();
                bw.write("  @Before");
                bw.newLine();
                bw.write("  public void set_up() throws Exception {");
                bw.newLine();
                bw.write("  }");
                bw.newLine();
                bw.write("}");
                bw.newLine();
                bw.close();
                fw.close();


                fw = new FileWriter(lists.getAbsoluteFile(), true);
                bw = new BufferedWriter(fw);
                bw.newLine();
                bw.write("add_test(NAME test" + name);
                bw.newLine();
                bw.write("    COMMAND ${Java_JAVA_EXECUTABLE}");
                bw.newLine();
                bw.write("    -cp .:${junitJarFile}:${JUNIT_JAR_FILE}:${HAMCREST_JAR_FILE}");
                bw.newLine();
                bw.write("    org.junit.runner.JUnitCore i15091.project.cmaker.Test" + name + ")");
                bw.newLine();
                bw.close();
                fw.close();
            }
        } catch(IOException e){
            e.printStackTrace();
            CMLogger.logSevere(LOGGER, "Failed to create test file(s)!");
        }

    }

    public void addJar(String path_to_jar) {
        CMLogger.logInfo(LOGGER, "Adding jar file to project...");
        File external_folder = new File(new String(path + "/" + name) + "/external");
        if(external_folder.exists() && external_folder.isDirectory()) {
            CMConsole.execute(path, new String("cp " + path_to_jar + " " + path + "/" + name + "/external/"));
        }
        else{
            external_folder.mkdir();
            CMConsole.execute(path, new String("cp " + path_to_jar + " " + path + "/" + name + "/external/"));
        }
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }


    private String name;
    private String path;
    private boolean first_test = true;
    public boolean junit_enabled = false;
    public boolean create_entry_point = false;
    Handler fileHandler;
}