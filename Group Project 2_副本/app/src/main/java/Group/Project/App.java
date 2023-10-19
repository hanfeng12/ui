package Group.Project;

import java.io.File;
import java.util.List;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.io.IOException;

public class App {

    public static void createUserDirectories(String username) {
        try {
            Files.createDirectories(Paths.get("src/main/resources/" + username + "/download"));
            Files.createDirectories(Paths.get("src/main/resources/" + username + "/upload"));
            Files.createDirectories(Paths.get("src/main/resources/" + username + "/library"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void uploadFilesForUser(String username) {
        Path sourceDirectory = Paths.get("src/main/resources/" + username + "/upload");
        Path targetSharedLibrary = Paths.get("src/main/resources/library");
        Path targetUserLibrary = Paths.get("src/main/resources/" + username + "/library");

        File uploadDir = new File(sourceDirectory.toString());
        File[] files = uploadDir.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("No files available for upload.");
            return;
        }

        System.out.println("Files available for upload:");
        for (int i = 0; i < files.length; i++) {
            System.out.println((i + 1) + ". " + files[i].getName());
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of the file you want to upload:");
        int choice = scanner.nextInt();

        if (choice > 0 && choice <= files.length) {
            File selectedFile = files[choice - 1];

            // 首先，将文件复制到共享library
            Path targetSharedFilePath = targetSharedLibrary.resolve(selectedFile.getName());
            try {
                Files.copy(selectedFile.toPath(), targetSharedFilePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                System.out.println("Error uploading the file to shared library: " + e.getMessage());
                return;
            }

            // 然后，从共享library复制文件到用户的个人library
            Path targetUserFilePath = targetUserLibrary.resolve(selectedFile.getName());
            try {
                Files.copy(targetSharedFilePath, targetUserFilePath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("File uploaded and backed up successfully!");
            } catch (IOException e) {
                System.out.println("Error copying the file to user's library: " + e.getMessage());
            }

        } else {
            System.out.println("Invalid choice.");
        }
    }






    public static void downloadFilesForUser(String username) {
        Path sourceDirectory = Paths.get("src/main/resources/library");
        Path targetDirectory = Paths.get("src/main/resources/" + username + "/download");

        File libraryDir = new File(sourceDirectory.toString());
        File[] files = libraryDir.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("No files available for download.");
            return;
        }

        System.out.println("Files available for download:");
        for (int i = 0; i < files.length; i++) {
            System.out.println((i + 1) + ". " + files[i].getName());
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of the file you want to download:");
        int choice = scanner.nextInt();

        if (choice > 0 && choice <= files.length) {
            File selectedFile = files[choice - 1];

            System.out.println("Enter the start line number:");
            int startLine = scanner.nextInt();

            System.out.println("Enter the end line number:");
            int endLine = scanner.nextInt();

            try {
                List<String> allLines = Files.readAllLines(selectedFile.toPath());
                List<String> selectedLines = allLines.subList(startLine - 1, endLine);

                String originalFilename = selectedFile.getName();
                Path targetFilePath = targetDirectory.resolve(originalFilename);

                // 检查文件是否已存在
                if (Files.exists(targetFilePath)) {
                    System.out.println("File already exists. Do you want to overwrite it? (yes/no)");
                    String overwriteChoice = scanner.next();
                    if (!overwriteChoice.equalsIgnoreCase("yes")) {
                        // 添加后缀以生成新的文件名
                        String newFilename = generateUniqueFilename(targetDirectory, originalFilename);
                        targetFilePath = targetDirectory.resolve(newFilename);
                    }
                }

                Files.write(targetFilePath, selectedLines);
                System.out.println("File downloaded successfully!");
            } catch (IOException e) {
                System.out.println("Error downloading the file: " + e.getMessage());
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Invalid line numbers provided.");
            }

        } else {
            System.out.println("Invalid choice.");
        }
    }



    private static String generateUniqueFilename(Path directory, String originalFilename) {
        int counter = 1;
        String filenameWithoutExtension = originalFilename.substring(0, originalFilename.lastIndexOf('.'));
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        String newFilename = filenameWithoutExtension + "_" + counter + extension;

        while (Files.exists(directory.resolve(newFilename))) {
            counter++;
            newFilename = filenameWithoutExtension + "_" + counter + extension;
        }

        return newFilename;
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
