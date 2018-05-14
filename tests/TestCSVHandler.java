package i15091.project.cmaker;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import i15091.project.cmaker.CSVHandler;

import java.util.*;

public class TestCSVHandler {

    @Before
    public void set_up() throws Exception {
    }

    @Test 
    public void testReadAndWrite() {
        CSVHandler.write("../tests/TestCSV.csv", "test_key", "test_value");
        LinkedList<String[]> list = CSVHandler.read("../tests/TestCSV.csv");
        String[] line = list.get(0);
        assertTrue(line[0].equals("test_key"));
        assertTrue(line[1].equals("test_value"));
    }

    public static void main(String[] args){
    }

}
