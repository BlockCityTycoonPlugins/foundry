package me.darkmun.blockcitytycoonfoundry;


public class WrongBlockException extends Exception {
    public WrongBlockException() {
        super();
    }
    public WrongBlockException(String message) {
        super(message);
    }
    public WrongBlockException(String message, Throwable cause) {
        super(message, cause);
    }
    public WrongBlockException(Throwable cause) {
        super(cause);
    }
}
