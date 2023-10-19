package Group.Project;

import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.io.IOException;

public class App {

    public static void createUserDirectories(String username) {
        try {
            Files.createDirectories(Paths.get("src/main/resources/" + username + "/library"));
            Files.createDirectories(Paths.get("src/main/resources/" + username + "/download"));
            Files.createDirectories(Paths.get("src/main/resources/" + username + "/upload"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void uploadFilesForUser(String username) {
        Path sourceDirectory = Paths.get("src/main/resources/" + username + "/upload");
        Path targetDirectory = Paths.get("src/main/resources/" + username + "/library");
        // ... (rest of the upload code)
    }

    public static void downloadFilesForUser(String username) {
        Path sourceDirectory = Paths.get("src/main/resources/" + username + "/library");
        Path targetDirectory = Paths.get("src/main/resources/" + username + "/download");
        // ... (rest of the download code)
    }

    public static void main(String[] args) {
        UserManager userManager = new UserManager();
        AdminManager adminManager = new AdminManager();
        GuestUser guestUser = new GuestUser();

        Scanner scanner = new Scanner(System.in);

        System.out.println("1. Register\n2. Login\n3. Use as a guest user\n");
        System.out.println("Enter your choice (1/2/3):");
        String choice = scanner.nextLine();

        if (choice.equals("1")) {
            userManager.register();
            createUserDirectories(userManager.getLatestRegisteredUsername());
        } else if (choice.equals("2")) {
            User user = userManager.login();
            if (user != null) {
                System.out.println("1. View all the users\n2. Add a user\n3. Delete a user\n4. Upload Files\n5. Download Files\n");
                System.out.println("Enter your choice (1/2/3/4/5):");
                String choice2 = scanner.nextLine();

                switch (choice2) {
                    case "1":
                        adminManager.view_all_users();
                        break;
                    case "2":
                        adminManager.add_a_user();
                        break;
                    case "3":
                        adminManager.delete_a_user();
                        break;
                    case "4":
                        uploadFilesForUser(user.getUsername());
                        break;
                    case "5":
                        downloadFilesForUser(user.getUsername());
                        break;
                    default:
                        System.out.println("Invalid choice!");
                        return;
                }
            }
        } else {
            System.out.println("Invalid input.");
        }
    }
}
