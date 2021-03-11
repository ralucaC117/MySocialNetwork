package socialnetwork.ui;

import socialnetwork.service.FriendRequestsService;
import socialnetwork.service.FriendshipsService;
import socialnetwork.service.MessagesService;
import socialnetwork.service.UtilizatorService;

import java.util.Scanner;

public class MainUI {

    private AdminUI adminUI;
    private UserUI userUI;

    public MainUI(AdminUI adminUI, UserUI userUI) {
        this.adminUI = adminUI;
        this.userUI = userUI;
    }


    public void run() throws Exception {

        while(true){

            Long id = null;

            System.out.println("1. View as admin");
            System.out.println("2. Log in as user");
            System.out.println("3. Exit");

            System.out.println("Enter number command");
            Scanner scan = new Scanner(System.in);
            int command = scan.nextInt();

            switch(command){
                case 1:
                    adminUI.run();
                    break;
                case 2:
                    System.out.println("Authenticate yourself!");
                    System.out.println("Username: ");
                    scan.nextLine();
                    String username = scan.nextLine();
                    System.out.println("Password: ");
                    String password = scan.nextLine();
                    try{
                        id = userUI.check(username, password);
                        userUI.run(id);
                    }catch (Exception ex){
                        System.out.println(ex.getMessage());
                    }

                    break;
                case 3:
                    return;


            }
        }
    }
}
