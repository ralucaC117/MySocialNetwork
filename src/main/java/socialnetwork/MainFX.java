package socialnetwork;

import controller.StartMenuController;
import controller.UserMenuController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import socialnetwork.config.ApplicationContext;
import socialnetwork.domain.*;
import socialnetwork.domain.validators.*;
import socialnetwork.repository.Repository;
import socialnetwork.repository.database.*;
import socialnetwork.service.*;
import sun.nio.ch.Util;

import java.io.IOException;

public class MainFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws IOException {
        final String url = ApplicationContext.getPROPERTIES().getProperty("database.socialnetwork.url");
        final String username= ApplicationContext.getPROPERTIES().getProperty("database.socialnetwork.username");
        final String pasword= ApplicationContext.getPROPERTIES().getProperty("database.socialnetwork.pasword");
        socialnetwork.domain.validators.Validator<Utilizator> utilizatorValidator = new UtilizatorValidator();
        socialnetwork.domain.validators.Validator<Prietenie> prietenieValidator = new FriendshipValidator();
        socialnetwork.domain.validators.Validator<Message> messageValidator = new MassageValidator();
        Validator<FriendRequest> friendRequestValidator = new FriendRequestValidator();
        Validator<Event> eventValidator = new EventValidator();

        Repository<Long,Utilizator> userFileRepository3 =
                new UtilizatorDbRepository(url,username, pasword,  utilizatorValidator);

        Repository<Tuple<Long,Long>, Prietenie> friendshipFileRepository3 =
                new FriendshipsDbRepository(url,username, pasword,  prietenieValidator);

        Repository<Long, Message> messageRepository =
                new MessagesDbRepository(url, username, pasword, messageValidator );

        Repository<Tuple<Long, Long>, FriendRequest> friendRequestRepository =
                new FriendRequestsDbRepository(url, username, pasword, friendRequestValidator);

        //Repository<Long, Event> eventRepository = new EventsDbRepository(url, username, pasword, eventValidator);

        EventsDbRepository eventRepository = new EventsDbRepository(url, username, pasword, eventValidator);

        UtilizatorDbPageRepository utilizatorDbPageRepository = new UtilizatorDbPageRepository(url, username, pasword, utilizatorValidator);

        //UtilizatorService utilizatorService = new UtilizatorService(userFileRepository3);
        UtilizatorService utilizatorService = new UtilizatorService(utilizatorDbPageRepository);
        FriendshipsService friendshipsService = new FriendshipsService(friendshipFileRepository3, userFileRepository3);
        MessagesService messagesService = new MessagesService(messageRepository);
        FriendRequestsService friendRequestsService = new FriendRequestsService(friendRequestRepository);
        EventsService eventsService = new EventsService(eventRepository);

        FXMLLoader loader;
        loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/startMenuView.fxml"));
        AnchorPane root=loader.load();

        StartMenuController startMenuController = loader.getController();

        startMenuController.setPrimaryStage(primaryStage);
        startMenuController.setServices(utilizatorService,friendshipsService,messagesService,friendRequestsService, eventsService);

        Scene main = new Scene(root, 340, 400);
        primaryStage.setScene(main);
        primaryStage.setTitle("MySocialNetwork");
        primaryStage.show();

    }
}

