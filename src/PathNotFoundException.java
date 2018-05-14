package i15091.project.cmaker;

public class PathNotFoundException extends Exception {
    PathNotFoundException(String msg) {
        super(msg);
    }

    PathNotFoundException() {
        super();
    }
}