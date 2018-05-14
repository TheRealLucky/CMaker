package i15091.project.cmaker;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

public class TestProject {

    @Before
    public void set_up() throws Exception {
        project = new Project("TestCMaker", "../tests");
        project.junit_enabled = true;
        project.create_entry_point = true;
        project.createProject();
    }

    @Test 
    public void testFolderStructure() {
        String path = "../tests/TestCMaker";
        File test = null;
        test = new File(path);
        assertTrue(test.exists() && test.isDirectory());
        test = new File(new String(path + "/build"));
        assertTrue(test.exists() && test.isDirectory());
        test = new File(new String(path + "/src"));
        assertTrue(test.exists() && test.isDirectory());
        test = new File(new String(path + "/tests"));
        assertTrue(test.exists() && test.isDirectory());
        test = new File(new String(path + "/CMakeLists.txt"));
        assertTrue(test.exists() && test.isFile());
        test = new File(new String(path + "/src/Main.java"));
        assertTrue(test.exists() && test.isFile());
        test = new File(new String(path + "/CMaker.properties"));
        assertTrue(test.exists() && test.isFile());
    }

    @Test
    public void testAddJar() { 
       project.addJar("../tests/Test.jar");
       File test = new File("../tests/TestCMaker/external/Test.jar");
       assertTrue(test.exists() && test.isFile());
    }

    public static void main(String[] args){
    }

    Project project;
}
