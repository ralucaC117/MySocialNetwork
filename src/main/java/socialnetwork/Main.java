package socialnetwork;

import socialnetwork.config.ApplicationContext;
import socialnetwork.domain.*;
import socialnetwork.domain.validators.*;
import socialnetwork.repository.Repository;
import socialnetwork.repository.Repository0;
import socialnetwork.repository.database.FriendRequestsDbRepository;
import socialnetwork.repository.database.FriendshipsDbRepository;
import socialnetwork.repository.database.MessagesDbRepository;
import socialnetwork.repository.database.UtilizatorDbRepository;
import socialnetwork.repository.file.UtilizatorFile;
import socialnetwork.repository.file.UtilizatorFile0;
import socialnetwork.service.FriendRequestsService;
import socialnetwork.service.FriendshipsService;
import socialnetwork.service.MessagesService;
import socialnetwork.service.UtilizatorService;
import socialnetwork.ui.AdminUI;
import socialnetwork.ui.MainUI;
import socialnetwork.ui.UI;
import socialnetwork.ui.UserUI;
import sun.nio.ch.Util;

import java.time.LocalDateTime;
import java.util.Optional;

public class Main {
    public static void main(String[] args) throws Exception {

//        final String url = ApplicationContext.getPROPERTIES().getProperty("database.socialnetwork.url");
//        final String username= ApplicationContext.getPROPERTIES().getProperty("databse.socialnetwork.username");
//        final String pasword= ApplicationContext.getPROPERTIES().getProperty("database.socialnetwork.pasword");
//        Validator<Utilizator> utilizatorValidator = new UtilizatorValidator();
//        Validator<Prietenie> prietenieValidator = new FriendshipValidator();
//        Validator<Message> messageValidator = new MassageValidator();
//        Validator<FriendRequest> friendRequestValidator = new FriendRequestValidator();
//
//        Repository<Long,Utilizator> userFileRepository3 =
//                new UtilizatorDbRepository(url,username, pasword,  utilizatorValidator);
//
//        Repository<Tuple<Long,Long>, Prietenie> friendshipFileRepository3 =
//                new FriendshipsDbRepository(url,username, pasword,  prietenieValidator);
//
//        Repository<Long, Message> messageRepository =
//                new MessagesDbRepository(url, username, pasword, messageValidator );
//
//        Repository<Tuple<Long, Long>, FriendRequest> friendRequestRepository =
//                new FriendRequestsDbRepository(url, username, pasword, friendRequestValidator);
//
//        UtilizatorService utilizatorService = new UtilizatorService(userFileRepository3);
//        FriendshipsService friendshipsService = new FriendshipsService(friendshipFileRepository3, userFileRepository3);
//        MessagesService messagesService = new MessagesService(messageRepository);
//        FriendRequestsService friendRequestsService = new FriendRequestsService(friendRequestRepository);
//
//
//        AdminUI adminUI = new AdminUI(utilizatorService, friendshipsService);
//        UserUI userUI = new UserUI(utilizatorService, friendshipsService, messagesService, friendRequestsService);
//
//        MainUI ui = new MainUI(adminUI, userUI);
//       ui.run();

       MainFX.main(args);


    }
}


