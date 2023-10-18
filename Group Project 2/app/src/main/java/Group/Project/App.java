package Group.Project;

import java.util.Scanner;

public class App {

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


        } else if (choice.equals("2")) {
            User user = userManager.login();

            if (userManager.getAdminCheckLogin()) {
                System.out.println("1. View all the users\n2. Add a user\n3. Delete a user\n");
                System.out.println("Enter your choice (1/2/3):");
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
                    default:
                        System.out.println("Invalid choice!");
                        return;
                }


            } else if (userManager.getCheckLogin()) {
                System.out.println("1. Update user profiles");
                System.out.println("Enter your choice (1):");
                String choice2 = scanner.nextLine();

                if (choice2.equals("1")) {
                    userManager.updateUserObject(user);
                }


            } else {
                guestUser.view_scrolls();
            }

        } else {
            System.out.println("Invalid input.");
        }

    }
}