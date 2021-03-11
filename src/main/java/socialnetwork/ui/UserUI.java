package socialnetwork.ui;

import socialnetwork.domain.Message;
import socialnetwork.domain.Prietenie;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.Utilizator;
import socialnetwork.service.FriendRequestsService;
import socialnetwork.service.FriendshipsService;
import socialnetwork.service.MessagesService;
import socialnetwork.service.UtilizatorService;

import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

public class UserUI {
    private UtilizatorService utilizatorService;
    private FriendshipsService friendshipsService;
    private MessagesService messagesService;
    private FriendRequestsService friendRequestsService;

    public UserUI(UtilizatorService service, FriendshipsService serviceF, MessagesService serviceM, FriendRequestsService serviceFR) {
        this.utilizatorService = service;
        this.friendshipsService = serviceF;
        this.messagesService = serviceM;
        this.friendRequestsService = serviceFR;
    }

    public Long check(String username, String password) throws Exception {
        Long id = Long.parseLong(username.split("#")[1]);
        Utilizator utilizator = utilizatorService.getOne(id).get();
        Long salt = Long.parseLong(utilizator.getSalt());
        String hashedPassword = utilizatorService.generateHash(password, "MD5", utilizatorService.longToBytes(salt));
        if(utilizator.getUsername().equals(username) && utilizator.getPassword().equals(hashedPassword))
        {
            System.out.println("Welcome " + utilizator.getFirstName() + " " + utilizator.getLastName() + " !");
            return id;
        }
        else
            throw new Exception("Invalid password!");

    }
    public void run(Long id) throws Exception {
        System.out.println("1. Send a message");
        System.out.println("2. Show inbox");
        System.out.println("3. Show friendship requests");
        System.out.println("4. Send friendship request");
        System.out.println("5. Answer friendship request");
        System.out.println("6. Exit");

        while(true){

            System.out.println("Enter number command");
            Scanner scan = new Scanner(System.in);
            int command = scan.nextInt();

            switch (command){
                case 1:
                    System.out.println("Reply to: ");
                    String ifReply = scan.nextLine();

                    Message reply = new Message();
                    Message message = new Message();
                    message.setFrom(id);
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
                case 2:
                    System.out.println("The other user: ");
                    Long id3 = scan.nextLong();

                    Optional<List<Message>> inbox =  messagesService.findConversation(id,id3);
                    if(inbox.isEmpty())
                        System.out.println("Nothing to show!");
                    else
                        inbox.get().forEach(System.out::println);
                    break;

                case 3:
                    friendRequestsService.getAll(id).stream()
                            .map(x->{x.setFrom(utilizatorService.getOne(x.getId().getLeft()).get()); x.setTo(utilizatorService.getOne(x.getId().getRight()).get());
                                return x;
                            })
                            .forEach(System.out::println);
                    break;
                case 4:
                    System.out.println("User to send the friend request to:");
                    Long id2 = scan.nextLong();
                    if(friendshipsService.getOne(new Tuple<>(id,id2)).isPresent() || friendshipsService.getOne(new Tuple<>(id2,id)).isPresent())
                        System.out.println("Prietenie existenta!");
                    else
                        friendRequestsService.sendFriendRequest(id, id2);
                    break;
                case 5:
                    System.out.println("The user you want to respond to: ");
                    id2 = scan.nextLong();
                    scan.nextLine();
                    System.out.println("Your response: ");
                    String response = scan.nextLine();
                    friendRequestsService.answerFriendRequest(id2, id, response);
                    if(response.equals("approve"))
                        friendshipsService.addFriendship(new Prietenie(new Tuple<Long, Long>(id2, id)));

                case 6:
                    return;

            }

        }



    }
}
