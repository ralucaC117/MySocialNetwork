package socialnetwork.ui;

import socialnetwork.domain.*;
import socialnetwork.repository.file.UtilizatorFile;
import socialnetwork.service.FriendRequestsService;
import socialnetwork.service.FriendshipsService;
import socialnetwork.service.MessagesService;
import socialnetwork.service.UtilizatorService;
import sun.nio.ch.Util;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UI {

    private UtilizatorService utilizatorService;
    private FriendshipsService friendshipsService;
    private MessagesService messagesService;
    private FriendRequestsService friendRequestsService;

    public UI(UtilizatorService service, FriendshipsService serviceF, MessagesService serviceM, FriendRequestsService serviceFR) {
        this.utilizatorService = service;
        this.friendshipsService = serviceF;
        this.messagesService = serviceM;
        this.friendRequestsService = serviceFR;
    }

    public void run() throws Exception {
        System.out.println("1. Add user");
        System.out.println("2. Remove user");
        System.out.println("3. Add friendship");
        System.out.println("4. Remove friendship");
        System.out.println("5. See all users");
        System.out.println("6. See all friendships of one user");
        System.out.println("7. See all friendships of one user from specified month and year");
        System.out.println("8. Send a message");
        System.out.println("9. Show inbox");
        System.out.println("10. Show friendship requests");
        System.out.println("11. Send friendship request");
        System.out.println("12. Answer friendship request");
        System.out.println("13. Exit");

        while (true) {
            System.out.println("Enter number command");
            Scanner scan = new Scanner(System.in);
            int command = scan.nextInt();
            switch (command) {
                case 1:

                    Long id;
                    System.out.println("Introduceti ID");
                    try {
                        id = scan.nextLong();
                        scan.nextLine();
                        System.out.println("Introduceti nume");
                        String nume = scan.nextLine();
                        System.out.println("Introduceti prenume");
                        String prenume = scan.nextLine();
                        Utilizator u = (new Utilizator(nume, prenume));
                        u.setId(id);
                        try {
                            Optional<Utilizator> o = utilizatorService.addUtilizator(u);
                            if(!o.isPresent())
                            System.out.println("Utilizatorul a fost adaugat cu succes!");
                            else
                                System.out.println("Utilizatorul nu a fost adaugat!");
                        } catch (Exception exception) {
                            System.out.println(exception.getMessage());
                            //System.out.println("Utilizatorul nu a fost adaugat!");
                        }


                    } catch (Exception E) {
                        System.out.println("ID-ul trebuie sa fie un numar !");
                    }

                    break;
                case 2:
                    try {
                        scan.nextLine();
                        System.out.println("Introduceti ID");
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
                    System.out.println("Authenticate yourself!");
                    System.out.print("Id = ");
                    Long from = scan.nextLong();
                    scan.nextLine();
                    System.out.println("     From " + utilizatorService.getOne(from).get().getFirstName() + " " + utilizatorService.getOne(from).get().getLastName());

                    System.out.println("Reply to: ");
                    String ifReply = scan.nextLine();

                    Message reply = new Message();
                    Message message = new Message();
                    message.setFrom(from);
                    List<Long> to;

                    if(ifReply.equals("none")){
                        String toNoReply;
                        System.out.print("To: ");
                        toNoReply = scan.nextLine();

                        String[] to1 = toNoReply.split(" ");
                        to = Arrays.stream(to1).sequential().map(Long::parseLong).collect(Collectors.toList());

                        reply.setId(-1L);
                    }

                    else{
                        Long toFromReply = messagesService.getOne(Long.parseLong(ifReply)).get().getFrom();
                        to=new ArrayList<>();
                        to.add(toFromReply);
                        reply.setId(Long.parseLong(ifReply));
                    }
                    message.setReply(reply);

                    message.setTo(to);

                    System.out.println("Write your message!");
                    String text = scan.nextLine();
                    message.setMessage(text);

                    Optional<Message> o = messagesService.addMessage(message);
                    if(!o.isPresent()){
                      System.out.println("Message sent!");}
                    else
                        System.out.println("Message failed!");

                    break;

                case 9:
                    System.out.println("Authenticate yourself!");
                    System.out.print("Id = ");
                    id = scan.nextLong();
                    scan.nextLine();
                    System.out.println("     Authenticated as " + utilizatorService.getOne(id).get().getFirstName() + " " + utilizatorService.getOne(id).get().getLastName());

                    System.out.println("The other user: ");
                    id3 = scan.nextLong();

                    Optional<List<Message>> inbox =  messagesService.findConversation(id,id3);
                    if(inbox.isEmpty())
                        System.out.println("Nothing to show!");
                    else
                        inbox.get().forEach(System.out::println);
                   // messagesService.findConversation(id,id3).get().forEach(System.out::println);
                    break;
                case 10:
                    System.out.println("Authenticate yourself!");
                    System.out.print("Id = ");
                    id = scan.nextLong();
                    scan.nextLine();
                    System.out.println("     Authenticated as " + utilizatorService.getOne(id).get().getFirstName() + " " + utilizatorService.getOne(id).get().getLastName());

                    friendRequestsService.getAll(id).stream()
                            .map(x->{x.setFrom(utilizatorService.getOne(x.getId().getLeft()).get()); x.setTo(utilizatorService.getOne(x.getId().getRight()).get());
                                return x;
                            })
                            .forEach(System.out::println);
                    break;

                case 11:
                    System.out.println("Authenticate yourself!");
                    System.out.print("Id = ");
                    id = scan.nextLong();
                    scan.nextLine();
                    System.out.println("     Authenticated as " + utilizatorService.getOne(id).get().getFirstName() + " " + utilizatorService.getOne(id).get().getLastName());

                    System.out.println("User to send the friend request to:");
                    Long id2 = scan.nextLong();
                    if(friendshipsService.getOne(new Tuple<>(id,id2)).isPresent() || friendshipsService.getOne(new Tuple<>(id2,id)).isPresent())
                        System.out.println("Prietenie existenta!");
                    else
                        friendRequestsService.sendFriendRequest(id, id2);
                    break;
                case 12:
                    System.out.println("Authenticate yourself!");
                    System.out.print("Id = ");
                    id = scan.nextLong();
                    scan.nextLine();
                    System.out.println("     Authenticated as " + utilizatorService.getOne(id).get().getFirstName() + " " + utilizatorService.getOne(id).get().getLastName());

                    System.out.println("The user you want to respond to: ");
                    id2 = scan.nextLong();
                    scan.nextLine();
                    System.out.println("Your response: ");
                    String response = scan.nextLine();
                    friendRequestsService.answerFriendRequest(id2, id, response);
                    if(response.equals("approve"))
                        friendshipsService.addFriendship(new Prietenie(new Tuple<Long, Long>(id2, id)));

                    break;
                case 13:
                    return;
            }
        }

    }
}
