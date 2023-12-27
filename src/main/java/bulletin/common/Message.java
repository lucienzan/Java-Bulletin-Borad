package bulletin.common;

public class Message {
	
	// Message Types
    public static int SUCCESS = 1;
    public static int FAIL = 2;
    public static int EXIST = 3;
    
    // Success Messages
    public static String CreateSuccess = "Successfully created.";
    public static String LoginSuccess = "Successfully logined";
    public static String UpdateSuccess = "Successfully updated.";
    public static String DeleteSuccess = "Successfully deleted.";
    public static String SendSuccess = "Reset password link is successfully sent into your mail box.";
    
    // Error Messages
    public static String AccountExist = "Email has already used.";
    public static String AccountFail = "Failed to create.";
    public static String IsAccountMatch = "Email or password is incorrect.";
    public static String AccountNotFound = "Account does not exist.";
    public static String NotActive = "Account has not activated yet.";
    public static String LoginFail = "Login Failed.";

    // Validation Messages
    // Account
    public static String REmail = "Email field is required.";
    public static String RFirstname = "FirstName field is required.";
    public static String LengthCheck = "Words length must be less than 20.";
    public static String RPassword = "Password filed is required.";
    public static String RCPassword = "Confirm Password filed is required.";
    public static String ROPassword = "Old Password filed is required.";
    public static String OPasswordMatch = "Old Password does not correct.";
    public static String PasswordMatch = "Password and Confirm Password should be the same.";
    public static String LAddress = "Address words length less than 100.";
    public static String RPhone = "Phone field is required.";
    public static String FPhone = "Phone's format is incorrect.";
    public static String FDOB = "Invalid date format.";
    public static String FPassword = "Password's format is incorrect.";
    public static String FEmail = "Email's format is incorrect.";
    public static String AccountDelete = "User account is deleted.";
    public static String FileTypeError = "File's extension does not support.";
    public static String FileSize = "File size exceeds the maximum allowed size (10MB).";
    
    // Role
    public static String RRole = "Role field is required.";
    
    // Post
    public static String RTitle = "Title field is required.";
    public static String RDescription = "Description field is required.";
    public static String LTitle = "Title words length must be less than 225.";
    public static String LDescription = "Description words length must be less than 1000.";
    public static String RFile = "File input field is required.";
    public static String TitleExist = "Title is already used.";

    // SQL Error
    public static String SError = "Either server or sql is failed.";
}
