package ExceptionsPackage;

public class QualificationsException extends ModuleException{
    private static final String PREFIX = "Qualifications Error: ";
    public QualificationsException(String message) {
        super(message);
    }
//    @Override
//    public void printStackTrace() {
//        System.err.println(PREFIX + getMessage());
//        super.printStackTrace();
//    }
}
