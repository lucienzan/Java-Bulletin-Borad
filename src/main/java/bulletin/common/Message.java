package bulletin.common;

public class Message {
	
	// Message Types
    public static int SUCCESS = 1;
    public static int FAIL = 2;
    public static int EXIST = 3;
    
    // Success Messages
    public static String AccountSuccess = "successfully created.";
    
    // Error Messages
    public static String AccountExist = "email has already used.";
    public static String AccountFail = "fail to create.";

}
