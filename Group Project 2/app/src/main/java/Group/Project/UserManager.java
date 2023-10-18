package Group.Project;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class UserManager {
    private static final String FILE_PATH = "src/main/resources/accounts.txt";

    private boolean checkLogin;

    private boolean AdmincheckLogin;


    public boolean getCheckLogin(){
        return checkLogin;
    }

    private boolean idKeyExists(String idKey) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                Map<String, String> map = parseLineToMap(line);
                if (map.get("ID keys").equals(idKey)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encryptedPasswd = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encryptedPasswd);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    static String bytesToHex(byte[] encryptedPasswd) {
        StringBuilder hexString = new StringBuilder(2 * encryptedPasswd.length);
        for (int i = 0; i < encryptedPasswd.length; i++) {
            String hex = Integer.toHexString(0xff & encryptedPasswd[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }


    public void register() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter phone number: ");
        String phoneNum = scanner.nextLine();

        System.out.println("Enter email address: ");
        String emailAddr = scanner.nextLine();

        System.out.println("Enter full name: ");
        String fullName = scanner.nextLine();

        System.out.println("Enter ID key: ");
        String idKey = scanner.nextLine();
        while (idKeyExists(idKey)) {
            System.out.println("ID key already exists, enter a different one: ");
            idKey = scanner.nextLine();
        }

        System.out.println("Enter username: ");
        String username = scanner.nextLine();

        System.out.println("Enter password: ");
        String passwd = scanner.nextLine();


        User user = new User(phoneNum, emailAddr, fullName, idKey, username, passwd);


        try {
            FileWriter writer = new FileWriter(FILE_PATH, true);
            writer.write(user.toString() + '\n');
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        scanner.close();
    }

    public static Map<String, String> parseLineToMap(String line) {
        Map<String, String> map = new HashMap<>();
        String[] pairs = line.split(", ");
        for (String pair : pairs) {
            String[] keyValue = pair.split(": ");
            map.put(keyValue[0], keyValue[1]);
        }
        return map;
    }

    private User parseLineToUser(String line, String passwd) {
        Map<String, String> map = parseLineToMap(line);
        return new User(
                map.get("Phone number"),
                map.get("Email address"),
                map.get("Full name"),
                map.get("ID keys"),
                map.get("Username"),
                passwd
        );
    }

    public User login(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username: ");
        String username = scanner.nextLine();
        System.out.println("Enter your passwd: ");
        String passwd = scanner.nextLine();



        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));
            String line;
            int lineNum = 0;
            while ((line = reader.readLine()) != null) {
                lineNum ++;
                Map<String, String> map = parseLineToMap(line);
                String ExpectedUsername = map.get("Username");
                String ExpectedPasswd = map.get("Password");

                if (username.equals("admin") && passwd.equals("admin123")) {
                    System.out.println("Admin Login success.");
                    AdmincheckLogin = true;
                    return null;

                } else if (username.equals(ExpectedUsername) && hashPassword(passwd).equals(ExpectedPasswd)) {
                    System.out.println("Login success.");
                    User matchedUser = parseLineToUser(line, passwd);
                    matchedUser.setLineNumber(lineNum);
                    checkLogin = true;
                    return matchedUser;
                }

            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Login failed.");
        checkLogin = false;
        return null;
    }

    public void updateUserObject(User user) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select the information you want to update:");
        System.out.println("1. Phone number");
        System.out.println("2. Email address");
        System.out.println("3. Full name");
        System.out.println("4. ID keys");
        System.out.println("5. Username");
        System.out.println("6. Password");
        System.out.println("Enter your choice (1/2/3/4/5/6):");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                System.out.println("Enter new phone number:");
                String newPhoneNum = scanner.nextLine();
                user.setPhoneNum(newPhoneNum);
                break;
            case "2":
                System.out.println("Enter new email address:");
                String newEmail = scanner.nextLine();
                user.setEmail(newEmail);
                break;
            case "3":
                System.out.println("Enter new full name:");
                String newFullname = scanner.nextLine();
                user.setFullName(newFullname);
                break;
            case "4":
                System.out.println("Enter new id keys:");
                String newIDkeys = scanner.nextLine();
                user.setIdKey(newIDkeys);
                break;
            case "5":
                System.out.println("Enter new username:");
                String newUsername = scanner.nextLine();
                user.setUsername(newUsername);
                break;
            case "6":
                System.out.println("Enter new password:");
                String newPassword = scanner.nextLine();
                user.setPasswd(newPassword);
                user.setEncryptedPasswd(newPassword);
                break;
            default:
                System.out.println("Invalid choice!");
                return;
        }

        updateUserInFile(user);
    }



    public void updateUserInFile(User user) {
        List<String> all_lines = new ArrayList<>();
        String line;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            while ((line = reader.readLine()) != null) {
                all_lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }

        String updatedLine = user.toString();

        all_lines.set(user.getLineNumber() - 1, updatedLine);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String eachLine : all_lines) {
                writer.write(eachLine);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to the file: " + e.getMessage());
        }
    }

    public boolean getAdminCheckLogin(){
        return AdmincheckLogin;
    }

}



