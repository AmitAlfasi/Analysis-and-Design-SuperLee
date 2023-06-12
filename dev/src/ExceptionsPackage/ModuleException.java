package ExceptionsPackage;

public abstract class ModuleException extends Exception {
    private static final String PREFIX = "ModuleException Error: ";
    public ModuleException(String message){
        super(message);
    }
    @Override
    public void printStackTrace() {
        System.err.println(PREFIX + getMessage());
        super.printStackTrace();
    }

}
