package bulletin.common;

public class Message {
	
	// Message Types
    public static int SUCCESS = 1;
    public static int FAIL = 2;
    public static int EXIST = 3;
    
    // Success Messages
    public static String AccountSuccess = "Successfully created.";
    public static String LoginSuccess = "Successfully logined";
    
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
    public static String PasswordMatch = "Password and Confirm Password should be the same.";
    public static String RRole = "Role field is required.";
    public static String LAddress = "Address words length less than 100.";
    public static String RPhone = "Phone field is required.";
    public static String FPhone = "Phone's format is incorrect.";
    public static String RDOB = "Birthday Date filed is required.";
    public static String FPassword = "Password's format is incorrect.";
    public static String FEmail = "Email's format is incorrect.";
    public static String AccountDelete = "User account is deleted.";
    public static String FileTypeError = "File's extension does not support.";
    
    // SQL Error
    public static String SError = "Either server or sql is failed.";
}
