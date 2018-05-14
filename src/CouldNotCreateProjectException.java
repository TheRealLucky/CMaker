package i15091.project.cmaker;

public class CouldNotCreateProjectException extends Exception {

    CouldNotCreateProjectException() {
        super();
    }

    CouldNotCreateProjectException(String msg) {
        super(msg);
    }
}