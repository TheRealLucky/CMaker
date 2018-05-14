package i15091.project.cmaker;

public class ProjectExistsException extends Exception {
    ProjectExistsException(String msg) {
        super(msg);
    }

    ProjectExistsException() {
        super();
    }
}