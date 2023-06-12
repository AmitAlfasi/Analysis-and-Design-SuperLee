package ExceptionsPackage;

public class UiException extends ModuleException {
    private static final String PREFIX = "Ui Error: ";
    public UiException(String message) {
        super(message);
    }

//    @Override
//    public void printStackTrace() {
//        System.err.println(PREFIX + getMessage());
//        super.printStackTrace();
//    }
}
