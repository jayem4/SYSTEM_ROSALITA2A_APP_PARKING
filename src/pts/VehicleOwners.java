package pts;

public class VehicleOwners {

    public String ownerName;
    public String ownerAddress;
    public String ownerPhone;
    public String ownerEmail;

    
    public VehicleOwners(String ownerName, String ownerAddress, String ownerPhone, String ownerEmail) {
        this.ownerName = ownerName;
        this.ownerAddress = ownerAddress;
        this.ownerPhone = ownerPhone;
        this.ownerEmail = ownerEmail;
    }


    public String getOwnerName() {
        return ownerName;
    }

    public String getOwnerAddress() {
        return ownerAddress;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }
}
