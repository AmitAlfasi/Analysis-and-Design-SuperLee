package BussinesLogic;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * this class hold all information regarding the shift manager
 */
public class ShiftManager {

    //variables
    private String fullName;
    private String id;
    private List<ShiftM_Permissions> permissions;
    private LocalDate shiftDate; //date of shift
    private int shiftSlot; //morning - 0  or evening shift - 1
    private List<Cancellation> cancelations;

    //constructor
    public ShiftManager(String name, String id, LocalDate shiftDate, int shiftSlot) {
        this.fullName = name;
        this.id = id;
        this.shiftDate = shiftDate;
        this.shiftSlot = shiftSlot;

        this.permissions = new ArrayList<ShiftM_Permissions>();

        //add permissions to shift manager - these are the basic permission that evey shift manager has
        ShiftM_Permissions cancellation = new ShiftM_Permissions("cancellations", "shift manager can cancel items");
        this.permissions.add(cancellation);
        ShiftM_Permissions manageStaff = new ShiftM_Permissions("manage staff", "shift manager can manage shift staff");
        this.permissions.add(manageStaff);

        this.cancelations=new ArrayList<Cancellation>();
    }

    //getters
    public String getFullName() {return fullName;}
    public String getId() {return id;}
    public List<ShiftM_Permissions> getPermissions() {return permissions;}
    public LocalDate getShiftDate() {return shiftDate;}
    public int getShiftSlot() {return shiftSlot;}
    public List<Cancellation> getCancelations() {return cancelations;}
    public void addToCancelations(Cancellation cancelation)
    {
        this.cancelations.add(cancelation);
    }

    /**
     * @param permission: add this permission the list of permission of specific shift manager
     */
    public void addPermission(ShiftM_Permissions permission)
    {
        this.permissions.add(permission);
    }

    /**
     * @param permission: remove this permission from specific shift manager
     */
    public void removePermission(ShiftM_Permissions permission)
    {
        this.permissions.remove(permission);
    }

    /**
     * find permission in shift managers list
     * @param name : permissions name
     * @return if found return the permission, if not then null is returned
     */
    public ShiftM_Permissions findPermission(String name){
        for (ShiftM_Permissions permission : permissions)
            if (Objects.equals(permission.getName(), name))
                return permission;
        return null;
    }

    /**
     * serches for cancellation of this shift manager
     * @param ID : cancellation ID
     * @return : cancellation in list
     */
    public Cancellation findCancellationInList(int ID)
    {
        for(Cancellation cancellation : cancelations)
            if(cancellation.getCancelID() == ID)
                return cancellation;
        return  null;
    }

}
