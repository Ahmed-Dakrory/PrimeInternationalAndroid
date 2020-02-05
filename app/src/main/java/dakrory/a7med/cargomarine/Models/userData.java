package dakrory.a7med.cargomarine.Models;

import com.google.gson.annotations.SerializedName;

public class userData {

    @SerializedName("error")
    private String error;

    @SerializedName("data")
    private dataClassOfUser userDetails;


    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public dataClassOfUser getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(dataClassOfUser userDetails) {
        this.userDetails = userDetails;
    }

    public static class dataClassOfUser {

    public static int ROLE_MAIN=0;
    public static int ROLE_SHIPPER=1;
    public static int ROLE_VENDOR=2;
    public static int ROLE_CUSTOMER=3;
    public static int ROLE_CONGSIGNEE=4;
    public static int ROLE_MAIN2=5;

    @SerializedName("id")
    private int id;

    @SerializedName("userName")
    private String userName;


    @SerializedName("firstName")
    private String firstName;


    @SerializedName("lastName")
    private String lastName;


    @SerializedName("email")
    private String email;


    @SerializedName("mainUserId")
    private int mainUserId;

    @SerializedName("role")
    private int role;

    @SerializedName("shipperId")
    private int shipperId;


    @SerializedName("vendorId")
    private int vendorId;


    @SerializedName("customerId")
    private int customerId;


    @SerializedName("consigneeId")
    private int consigneeId;


    @SerializedName("mainTwoId")
    private int mainTwoId;


    @SerializedName("allowAccess")
    private int allowAccess;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getMainUserId() {
        return mainUserId;
    }

    public void setMainUserId(int mainUserId) {
        this.mainUserId = mainUserId;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getShipperId() {
        return shipperId;
    }

    public void setShipperId(int shipperId) {
        this.shipperId = shipperId;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getConsigneeId() {
        return consigneeId;
    }

    public void setConsigneeId(int consigneeId) {
        this.consigneeId = consigneeId;
    }

    public int getMainTwoId() {
        return mainTwoId;
    }

    public void setMainTwoId(int mainTwoId) {
        this.mainTwoId = mainTwoId;
    }

    public int getAllowAccess() {
        return allowAccess;
    }

    public void setAllowAccess(int allowAccess) {
        this.allowAccess = allowAccess;
    }
}

}
