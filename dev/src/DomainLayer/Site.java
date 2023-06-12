package DomainLayer;

public abstract class Site {
    protected String address;
    protected Area areaCode;
    protected String contactName;
    protected String ContactNumber;


    public Site(String address, Area areaCode, String contactName, String contactNumber) {
        this.address = address;
        this.areaCode = areaCode;
        this.contactName = contactName;
        ContactNumber = contactNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Area getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(Area areaCode) {
        this.areaCode = areaCode;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }
}
