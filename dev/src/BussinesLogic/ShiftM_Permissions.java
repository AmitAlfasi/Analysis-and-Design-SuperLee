package BussinesLogic;

/**
 * This class creates the shift managers permissions.
 * Every shift manager include 2 objects from this type (one for cancellations, another for manage the team).
 */
public class ShiftM_Permissions {

    //variables
    private String name;
    private String description;

    //constructor
    public ShiftM_Permissions(String name, String description) {
        this.name = name;
        this.description = description;
    }

    //getters
    public String getName() {return name;}
    public String getDescription() {return description;}

    //there are no setter, you can delete the permission and create a new one that is more specific
}
