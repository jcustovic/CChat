package hr.chus.client.smartgwt.client.i18n;

import com.google.gwt.i18n.client.Constants;

/**
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 * 
 */
public interface Dictionary extends Constants {

	@DefaultStringValue("Username")
	String username();

	@DefaultStringValue("Password")
	String password();

	@DefaultStringValue("Role")
	String role();

	@DefaultStringValue("Name")
	String name();

	@DefaultStringValue("Surname")
	String surname();

	@DefaultStringValue("Email")
	String email();

	@DefaultStringValue("Active")
	String isActive();

	@DefaultStringValue("Disabled")
	String isDisabled();

	@DefaultStringValue("Disable")
	String disable();

	@DefaultStringValue("Operators")
	String operators();

	@DefaultStringValue("Nicks")
	String nicks();

	@DefaultStringValue("Pictures")
	String pictures();

	@DefaultStringValue("Loading...")
	String loading();

	@DefaultStringValue("Menu")
	String menu();

	@DefaultStringValue("Settings")
	String settings();

	@DefaultStringValue("Main")
	String main();

	@DefaultStringValue("Operators list")
	String operatorsList();

	@DefaultStringValue("Update")
	String update();

	@DefaultStringValue("Saving data...")
	String savingData();

	@DefaultStringValue("Delete")
	String delete();

	@DefaultStringValue("Add new operator")
	String addNewOperator();

	@DefaultStringValue("Add new")
	String addNew();

	@DefaultStringValue("Edit operator")
	String editOperator();

	@DefaultStringValue("Description")
	String description();

	@DefaultStringValue("Nick list")
	String nickList();

	@DefaultStringValue("If you delete this nick, picture assigned to this nick will be unassigned! Are u sure you want this?")
	String nickDeleteWarning();

	@DefaultStringValue("Confirm")
	String confirm();

	@DefaultStringValue("Show pictures")
	String showPictures();

	@DefaultStringValue("Add new nick")
	String addNewNick();

	@DefaultStringValue("Save")
	String save();

	@DefaultStringValue("Edit nick")
	String editNick();

	@DefaultStringValue("Type")
	String type();

	@DefaultStringValue("Size (bytes)")
	String sizeBytes();

	@DefaultStringValue("Assigned nick")
	String assignedNick();

	@DefaultStringValue("Picture list")
	String pictureList();

	@DefaultStringValue("Picture name")
	String imageName();

	@DefaultStringValue("Edit picture")
	String editPicture();

	@DefaultStringValue("Nick")
	String nick();

	@DefaultStringValue("No images match the specified filter")
	String noImageFound();

	@DefaultStringValue("Find")
	String find();

	@DefaultStringValue("Picture uploader")
	String pictureUploader();

	@DefaultStringValue("Image")
	String picture();

	@DefaultStringValue("Upload")
	String upload();

	@DefaultStringValue("Uploading...")
	String uploading();

	@DefaultStringValue("Are you sure?")
	String reqularConfirm();

	@DefaultStringValue("Close")
	String close();

	@DefaultStringValue("Upload done!")
	String uploadDone();

	@DefaultStringValue("Upload failed!")
	String uploadFailed();

	@DefaultStringValue("Don't assign nick...")
	String noNick();

	@DefaultStringValue("Yes")
	String yes();

	@DefaultStringValue("No")
	String no();

	@DefaultStringValue("Users")
	String users();

	@DefaultStringValue("Empty set")
	String emptySet();

	@DefaultStringValue("Field is required")
	String fieldIsRequired();

	@DefaultStringValue("Invalid email address")
	String invalidEmail();

	@DefaultStringValue("Errors happend")
	String errors();

	@DefaultStringValue("Not set")
	String notSet();

	@DefaultStringValue("Search")
	String search();

	@DefaultStringValue("Operator")
	String operator();

	@DefaultStringValue("Service provider")
	String serviceProvider();

	@DefaultStringValue("Phone no.")
	String msisdn();

	@DefaultStringValue("Address")
	String address();

	@DefaultStringValue("Birth date")
	String birthDate();

	@DefaultStringValue("Join date")
	String joinedDate();

	@DefaultStringValue("Notes")
	String notes();

	@DefaultStringValue("Short code")
	String shortCode();

	@DefaultStringValue("Invalid date")
	String invalidDate();

	@DefaultStringValue("Fetch size")
	String fetchSize();

	@DefaultStringValue("Send message")
	String sendMessage();

	@DefaultStringValue("Next")
	String next();

	@DefaultStringValue("Previous")
	String previous();

	@DefaultStringValue("Contacting server...")
	String contactingServer();

	@DefaultStringValue("Finding Records that match your criteria...")
	String findingRecordThatMatchCriteria();

	@DefaultStringValue("Deleting Record(s)...")
	String deletingRecord();

	@DefaultStringValue("Text")
	String text();

	@DefaultStringValue("Characters allowed")
	String charactersAllowed();

	@DefaultStringValue("Direction")
	String direction();

	@DefaultStringValue("Received time")
	String receivedTime();

	@DefaultStringValue("Messages")
	String messages();

	@DefaultStringValue("Sent")
	String sent();

	@DefaultStringValue("Received")
	String received();

	@DefaultStringValue("Received time from")
	String receivedTimeStart();

	@DefaultStringValue("Received time to")
	String receivedTimeEnd();

}
