package hr.chus.cchat.client.smartgwt.client.i18n;

import com.google.gwt.i18n.client.Constants;

/**
 * @author Jan Čustović (jan.custovic@gmail.com)
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
    String regularConfirm();

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
    String birthdate();

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

    @DefaultStringValue("Send bot message")
    String sendBotMessage();

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

    @DefaultStringValue("Statistics")
    String statistics();

    @DefaultStringValue("About")
    String about();

    @DefaultStringValue("Chat application")
    String chatApp();

    @DefaultStringValue("Statistics per service provider")
    String statisticsPerSP();

    @DefaultStringValue("Statistics per operator")
    String statisticsPerOperator();

    @DefaultStringValue("Live statistics")
    String liveStatistics();

    @DefaultStringValue("From date")
    String fromDate();

    @DefaultStringValue("To date")
    String toDate();

    @DefaultStringValue("Statistics type")
    String statisticsType();

    @DefaultStringValue("Graphics loading...")
    String graphicsLoading();

    @DefaultStringValue("Graphics loaded")
    String graphicsLoaded();

    @DefaultStringValue("Sum")
    String sum();

    @DefaultStringValue("Home")
    String home();

    @DefaultStringValue("Header")
    String header();

    @DefaultStringValue("Footer")
    String footer();

    @DefaultStringValue("Today")
    String today();

    @DefaultStringValue("Please select a file")
    String pleaseSelectAFile();

    @DefaultStringValue("Deleted")
    String deleted();

    @DefaultStringValue("No items to show")
    String noItemsToShow();

    @DefaultStringValue("Active")
    String active();

    @DefaultStringValue("Not active")
    String notActive();

    @DefaultStringValue("You are not active. Would you like to activate yourself?")
    String wouldYouLikeToActivateYourself();

    @DefaultStringValue("Total sms messages found in database")
    String totalSmsMessagesInDB();

    @DefaultStringValue("Total user found in database")
    String totalUsersInDB();

    @DefaultStringValue("User id")
    String userId();

    @DefaultStringValue("My users")
    String myUsers();

    @DefaultStringValue("Last 48h users")
    String last48HourUsers();

    @DefaultStringValue("Random users")
    String randomUsers();

    @DefaultStringValue("Last message date")
    String lastMsgDate();

    @DefaultStringValue("Unread message count")
    String unreadMsgCount();

    @DefaultStringValue("time")
    String time();

    @DefaultStringValue("Edit user")
    String editUser();

    @DefaultStringValue("Message sent successfully")
    String messageSentSuccessfully();

    @DefaultStringValue("Sending message failed")
    String sendingMessageFailed();

    @DefaultStringValue("Keyword")
    String keyword();

    @DefaultStringValue("External")
    String external();

    @DefaultStringValue("Bot")
    String bot();

    @DefaultStringValue("Bot not yet selected")
    String noBotSelected();

}
