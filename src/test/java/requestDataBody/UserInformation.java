package requestDataBody;



public class UserInformation {
	
	private String userFirstName, userLastName, userContactNumber,userEmailId;
	AddInformation userAddress;
	
	public UserInformation(String Fname, String Lname, String Phno, String mail, AddInformation Address ) {
		this.userFirstName = Fname;
		this.userLastName = Lname;
		this.userContactNumber = Phno;
		this.userEmailId = mail;
		this.userAddress = Address;	
	}
	
	public String getUser_first_name() {
		return userFirstName;
	}
	public void setUser_first_name(String userFirstName) {
		this.userFirstName = userFirstName;
	}
	public String getUser_last_name() {
		return userLastName;
	}
	public void setUser_last_name(String userLastName) {
		this.userLastName = userLastName;
	}
	public String getUser_contact_number() {
		return userContactNumber;
	}
	public void setUser_contact_number(String userContactNumber) {
		this.userContactNumber = userContactNumber;
	}
	public String getUser_email_id() {
		return userEmailId;
	}
	public void setUser_email_id(String userEmailId) {
		this.userEmailId = userEmailId;
	}
	public AddInformation getUserAddress() {
		return userAddress;
	}
	public void setUserAddress(AddInformation userAddress) {
		this.userAddress = userAddress;
	}
	
	
}