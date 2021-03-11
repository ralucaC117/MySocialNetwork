package socialnetwork.ui;

import socialnetwork.domain.Prietenie;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.Utilizator;
import socialnetwork.service.FriendRequestsService;
import socialnetwork.service.FriendshipsService;
import socialnetwork.service.MessagesService;
import socialnetwork.service.UtilizatorService;

import java.util.Optional;
import java.util.Scanner;

public class AdminUI {

    private UtilizatorService utilizatorService;
    private FriendshipsService friendshipsService;


    public AdminUI(UtilizatorService service, FriendshipsService serviceF) {
        this.utilizatorService = service;
        this.friendshipsService = serviceF;
    }
    public void run() throws Exception {
        Long id, salt;
        String username;
        String lastName, firstName;
        String password;

        System.out.println("1. Add user");
        System.out.println("2. Delete user");
        System.out.println("3. Add friendship");
        System.out.println("4. Delete friendship");
        System.out.println("5. See all users");
        System.out.println("6. See all friendships of one user");
        System.out.println("7. See all friendships of one user from specified month and year");
        System.out.println("8. Exit");

        while(true){

            System.out.println("Enter number command");
            Scanner scan = new Scanner(System.in);
            int command = scan.nextInt();

            switch(command){
                case 1:
                    System.out.println("Select an ID:");
                    id = scan.nextLong();
                    scan.nextLine();
                    System.out.println("Provide first name");
                    firstName = scan.nextLine();
                    System.out.println("Provide last name");
                    lastName = scan.nextLine();
                    System.out.println("Select username:");
                    username = scan.nextLine();
                    System.out.println("Select a password! Don't forget it");
                    password = scan.nextLine();

                    Utilizator utilizator = new Utilizator(lastName, firstName);
                    utilizator.setId(id);
                    utilizator.setUsername(username);

                    salt = utilizatorService.createSalt();
                    utilizator.setSalt(salt.toString());
                    String hashedPassword = utilizatorService.generateHash(password, "MD5", utilizatorService.longToBytes(salt));
                    utilizator.setPassword(hashedPassword);

                    try {
                        Optional<Utilizator> o = utilizatorService.addUtilizator(utilizator);
                        if(!o.isPresent())
                            System.out.println("Utilizatorul a fost adaugat cu succes!");
                        else
                            System.out.println("Utilizatorul nu a fost adaugat!");
                    } catch (Exception exception) {
                        System.out.println(exception.getMessage());
                    }
                    break;
                case 2:
                    try {
                        scan.nextLine();
                        System.out.println("Select an ID");
                        id = scan.nextLong();
                        utilizatorService.deleteUtilizator(id);
                        System.out.println("Utilizatorul a fost sters!");

                    } catch (Exception E) {
                        System.out.println(E.getMessage());
                    }
                    break;
                case 3:
                    try{
                        Long id1, id2;
                        System.out.println("Introduceti cele 2 ID-uri");
                        scan.nextLine();
                        id1 = scan.nextLong();
                        id2 = scan.nextLong();

                        try{

                            Optional<Prietenie> o = friendshipsService.addFriendship(new Prietenie(new Tuple<Long, Long>(id1, id2)));
                            if(!o.isPresent()){
                                System.out.println("Prietenie adaugata!");
                            }
                            else
                                System.out.println("Prietenia nu s-a adaugat!");
                        }
                        catch(Exception E){
                            System.out.println(E.getMessage());
                        }}
                    catch(Exception E){
                        System.out.println("ID-urile trebuie sa fie numere pozitive!");
                    }

                    break;
                case 4:
                    Long id3, id4;
                    System.out.println("Introduceti cele 2 ID-uri");
                    scan.nextLine();
                    id3 = scan.nextLong();
                    id4 = scan.nextLong();

                    try{
                        friendshipsService.deletePrietenie(id3, id4);
                        System.out.println("Prietenia a fost stearsa!");

                    } catch (Exception exception) {
                        System.out.println(exception.getMessage());
                    }
                    break;

                case 5:
                    utilizatorService.getAll().forEach(System.out::println);
                    break;

                case 6:
                    scan.nextLine();
                    System.out.println("Introduceti ID");
                    id = scan.nextLong();

                    friendshipsService.friendsOfUser(utilizatorService.getOne(id).get()).forEach((x,y)->{
                        System.out.println(x.getFirstName()+"|"+x.getLastName()+"|"+y);
                    });
                    break;

                case 7:
                    System.out.println("Introduceti ID");
                    id = scan.nextLong();

                    System.out.println("Introduceti luna si anul");
                    scan.nextLine();
                    String time = scan.nextLine();

                    String[] params = time.split(" ");
                    friendshipsService.friendsOfUserFromMonth(utilizatorService.getOne(id).get(), Integer.parseInt(params[0]), Integer.parseInt(params[1])).forEach((x,y)->{
                        System.out.println(x.getFirstName()+"|"+x.getLastName()+"|"+y);
                    });
                    break;

                case 8:
                    return;
            }
        }
    }
}
