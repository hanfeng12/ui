
package Group.Project;


public class User {
    private String phoneNum;
    private String emailAddr;
    private String fullName;
    private String idKey;
    private String username;
    private String passwd;

    private String encryptedPasswd;

    private int lineNum;


    public User(String phoneNum, String emailAddr, String fullName, String idKey, String username, String passwd) {
        this.phoneNum = phoneNum;
        this.emailAddr = emailAddr;
        this.fullName = fullName;
        this.idKey = idKey;
        this.username = username;
        this.passwd = passwd;
        this.encryptedPasswd = UserManager.hashPassword(passwd);
    }


    public String getPhoneNum() {
        return phoneNum;
    }


    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }


    public String getEmail() {
        return emailAddr;
    }


    public void setEmail(String emailAddr) {
        this.emailAddr = emailAddr;
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    public String getIdKey() {
        return idKey;
    }


    public void setIdKey(String idKey) {
        this.idKey = idKey;
    }


    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }


    public String getEncryptedPasswd() { return encryptedPasswd; }

    public void setEncryptedPasswd(String passwd) { encryptedPasswd = UserManager.hashPassword(passwd); }

    public int getLineNumber() {
        return lineNum;
    }

    public void setLineNumber(int lineNum) {
        this.lineNum = lineNum;
    }




    public String toString() {
        return "Phone number: " + phoneNum
                + ", Email address: " + emailAddr
                + ", Full name: " + fullName
                + ", ID keys: " + idKey
                + ", Username: " + username
                + ", Password: " + encryptedPasswd;
    }
}
