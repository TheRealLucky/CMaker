package i15091.project.cmaker;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import i15091.project.cmaker.Maker;

import java.io.*;

public class TestMaker {

    @Before
    public void set_up() throws Exception {
        maker = new Maker();
        maker.createProject("TestCMaker", "../tests", true, true);
    }

    @Test 
    public void testCheckProjectFolder() {
        assertTrue(Maker.checkProjectFolder(new File("../tests/TestCMaker")));
    }

    @Test
    public void testAddSourceFileWithExtension() {
        maker.addSourceFile("TestFile.java");
        File test = new File("../tests/TestCMaker/src/TestFile.java");
        assertTrue(test.exists() && test.isFile());
    }

    @Test 
    public void testAddSourceFileWithoutExtension() {
        maker.addSourceFile("TestFile");
        File test = new File("../tests/TestCMaker/src/TestFile.java");
        assertTrue(test.exists() && test.isFile());
    }

    public static void main(String[] args){
    }
    
    Maker maker;
}
