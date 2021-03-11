package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
//import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import socialnetwork.domain.Utilizator;
import socialnetwork.service.*;

import java.awt.*;
import java.io.IOException;


public class StartMenuController {
    @FXML
    PasswordField textfieldPassword;
    @FXML
    TextField usernameTextField;


    private Stage primaryStage;


    private UtilizatorService utilizatorService;
    private FriendshipsService friendshipsService;
    private MessagesService messagesService;
    private FriendRequestsService friendRequestsService;
    private EventsService eventsService;

    public void setServices(UtilizatorService utilizatorService, FriendshipsService friendshipsService,
                            MessagesService messagesService, FriendRequestsService friendRequestsService,
                            EventsService eventsService){
        this.utilizatorService= utilizatorService;
        this.friendshipsService = friendshipsService;
        this.messagesService = messagesService;
        this.friendRequestsService = friendRequestsService;
        this.eventsService = eventsService;

    }




    public void setPrimaryStage(Stage primaryStage){
        this.primaryStage = primaryStage;
    }

    public Long check(String username, String password) throws Exception {
        Long id = Long.parseLong(username.split("#")[1]);
        Utilizator utilizator = utilizatorService.getOne(id).get();
        Long salt = Long.parseLong(utilizator.getSalt());
        String hashedPassword = utilizatorService.generateHash(password, "MD5", utilizatorService.longToBytes(salt));
        if(utilizator.getUsername().equals(username) && utilizator.getPassword().equals(hashedPassword))
        {
           // System.out.println("Welcome " + utilizator.getFirstName() + " " + utilizator.getLastName() + " !");
            return id;
        }
        else
            throw new Exception("Invalid password!");


    }
    public void handleButtonLogIn(ActionEvent actionEvent) throws Exception {

        try{
            Long id = this.check(usernameTextField.getText(), textfieldPassword.getText());
            FXMLLoader loader;
            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/userMenuView.fxml"));
            TabPane root = loader.load();
            Scene userScene = new Scene(root,614,392);

            UserMenuController userMenuController = loader.getController();
            userMenuController.setPrimaryStage(primaryStage);
            userMenuController.setId(id);
            userMenuController.setServices(utilizatorService, friendshipsService, messagesService, friendRequestsService, eventsService);


            primaryStage.setTitle("User Menu");
            primaryStage.setScene(userScene);
            primaryStage.show();}
        catch (Exception ex){
            AlertBox alertBox = new AlertBox("Login failed!", "Incorrect password or username!");
            alertBox.display();
        }



    }
    public void handleButtonSignUp(ActionEvent actionEvent) {

    }


}
