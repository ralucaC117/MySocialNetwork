package controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import socialnetwork.domain.*;
import socialnetwork.domain.Event;
import socialnetwork.repository.paging.Page;
import socialnetwork.service.*;

import javax.swing.text.html.Option;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;



public class UserMenuController {

    private Stage primaryStage;
    private UtilizatorService utilizatorService;
    private FriendshipsService friendshipsService;
    private MessagesService messagesService;
    private FriendRequestsService friendRequestsService;
    private EventsService eventsService;

    private Long id;

    ObservableList<UserDTO> userDTOObservableList = FXCollections.observableArrayList();
    ObservableList<FriendRequestDTO> friendRequestDTOObservableList = FXCollections.observableArrayList();
    ObservableList<Utilizator> messagesObservableList = FXCollections.observableArrayList();
    ObservableList<Utilizator> friendsObservableList = FXCollections.observableArrayList();
    ObservableList<EventDTO> eventObservableList = FXCollections.observableArrayList();

    ObservableList<String> notificationsObservabeList = FXCollections.observableArrayList();

    @FXML
    TableView<UserDTO> tableViewAll;
    @FXML
    javafx.scene.control.TableColumn<UserDTO, Long> columnID;
    @FXML
    javafx.scene.control.TableColumn<UserDTO, String> columnUsername;
    @FXML
    javafx.scene.control.TableColumn<UserDTO, String> columnFirstName;
    @FXML
    TableColumn<UserDTO, String> columnLastName;


    @FXML
    TextField searchTextField;


    @FXML
    TableView<FriendRequestDTO> friendRequestDTOTableView;
    @FXML
    TableColumn<FriendRequestDTO, String> columnFrom;
    @FXML
    TableColumn<FriendRequestDTO, String> columnTo;
    @FXML
    TableColumn<FriendRequestDTO, String> columnStatus;
    @FXML
    TableColumn<FriendRequestDTO, LocalDate> columnDate;

    @FXML
    CheckBox checkBoxMyFriends;

    @FXML
    TableView<Utilizator> tableViewInbox;
    @FXML
    TableColumn<Utilizator, String> columnMessages;


    @FXML
    AnchorPane anchorPaneConversation;


    @FXML
    TextField textFieldMessage;

    @FXML
    Label labelName;

    @FXML
    CategoryAxis dateAxis = new CategoryAxis();
    @FXML
    NumberAxis numberAxis = new NumberAxis();

    @FXML
    CategoryAxis dateAxis2 = new CategoryAxis();
    @FXML
    NumberAxis numberAxis2 = new NumberAxis();

    @FXML
    BarChart<String, Number> chartMessages = new BarChart<String, Number>(dateAxis, numberAxis);

    @FXML
    BarChart<String, Number> chartMessagesFrom = new BarChart<String, Number>(dateAxis2, numberAxis2);



    @FXML
    DatePicker beginDate;
    @FXML
    DatePicker endDate;


    @FXML
    TableView<Utilizator> tableViewUsernamesReports;
    @FXML
    TableColumn<Utilizator, String> columnUsernameReports;

    @FXML
    TableView<EventDTO> tableEvents;
    @FXML
    TableColumn<EventDTO, String> columnEventName;
    @FXML
    TableColumn<EventDTO, String> columnEventDate;
    @FXML
    TableColumn<EventDTO, String> columnHost;
    @FXML
    TableColumn<EventDTO, String> columnEventDescription;


    @FXML
    javafx.scene.control.Button  buttonGoing;
    @FXML
    javafx.scene.control.Button buttonNotGoing;
    @FXML
    javafx.scene.control.Button buttonNotifications;


    @FXML
    ListView<String> listViewNotifications;

    @FXML
    javafx.scene.control.Button buttonMenuNotifications;

    @FXML
    ImageView imageView;


    @FXML
    TextField textFieldEventName;
    @FXML
    TextArea textAreaEventDescription;
    @FXML
    DatePicker datePickerEvent;
    @FXML
    Spinner<Integer> spinnerHour;
    @FXML
    Spinner<Integer> spinnerMinutes;


    @FXML
    Pagination friendsPagination;

    @FXML
    ScrollPane scrollPaneConv;

    @FXML
    Text textName;


    @FXML
    Text nrOfNotifications;

    @FXML
    Circle circleNotifications;

    @FXML
    Button buttonAccept;
    @FXML
    Button buttonDecline;
    @FXML
    Button buttonCancel;

    @FXML
    Button buttonAddFriend;

    @FXML
    Button buttonRemoveFriend;


    public void setPrimaryStage(Stage primaryStage){
        this.primaryStage = primaryStage;
    }

    public void setId(Long id){
        this.id = id;
    }

    @FXML
    public void initialize(){
        columnID.setCellValueFactory(new PropertyValueFactory<UserDTO, Long>("ID"));
        columnUsername.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("username"));
        columnFirstName.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("firstName"));
        columnLastName.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("lastName"));

        columnFrom.setCellValueFactory(new PropertyValueFactory<FriendRequestDTO, String>("usernameFrom"));
        columnTo.setCellValueFactory(new PropertyValueFactory<FriendRequestDTO, String>("usernameTo"));
        columnStatus.setCellValueFactory(new PropertyValueFactory<FriendRequestDTO, String>("status"));
        columnDate.setCellValueFactory(new PropertyValueFactory<FriendRequestDTO, LocalDate>("date"));

        columnMessages.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("username"));

        columnUsernameReports.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("username"));


        columnEventName.setCellValueFactory(new PropertyValueFactory<EventDTO, String>("name"));
        columnHost.setCellValueFactory(new PropertyValueFactory<EventDTO, String>("host"));
        columnEventDate.setCellValueFactory(new PropertyValueFactory<EventDTO, String>("date"));
        columnEventDescription.setCellValueFactory(new PropertyValueFactory<EventDTO, String >("description"));


        this.tableViewAll.setItems(userDTOObservableList);
        this.friendRequestDTOTableView.setItems(friendRequestDTOObservableList);
        this.tableViewInbox.setItems(messagesObservableList);
        this.tableViewUsernamesReports.setItems(friendsObservableList);
        this.tableEvents.setItems(eventObservableList);
        this.listViewNotifications.setItems(notificationsObservabeList);


    }

    public void setServices(UtilizatorService utilizatorService, FriendshipsService friendshipsService,
                            MessagesService messagesService, FriendRequestsService friendRequestsService,
                            EventsService eventsService) throws FileNotFoundException {
        this.utilizatorService= utilizatorService;
        this.friendshipsService = friendshipsService;
        this.messagesService = messagesService;
        this.friendRequestsService = friendRequestsService;
        this.eventsService = eventsService;

        friendsPagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) ->
                pageHandler());
        Page<Utilizator> utilizatorPage =  utilizatorService.getPagedUsers(friendsPagination.getCurrentPageIndex());
        userDTOObservableList.setAll(utilizatorPage.getContent()
                .filter(utilizator -> utilizator.getId()!=this.id)
                .map(utilizator -> new UserDTO(utilizator.getId(), utilizator.getUsername(), utilizator.getFirstName(), utilizator.getLastName()))
                .collect(Collectors.toList()));

        //userDTOObservableList.setAll(getUserDTOList());
        friendRequestDTOObservableList.setAll(getFriendRequestDTOList());
        //messagesObservableList.setAll(getUsersFromConversations());
        messagesObservableList.setAll(getFriends());
        friendsObservableList.setAll(getFriends());
        eventObservableList.setAll(getEvents());
        notificationsObservabeList.setAll(getNotifications());

        textName.setText("Hello, "+utilizatorService.getOne(this.id).get().getUsername()+"!");
        FileInputStream fileInputStream = new FileInputStream(utilizatorService.getOne(this.id).get().getProfilePicPath());
        Image image = new Image(fileInputStream);
        imageView.setImage(image);

        Image img = new Image("Images/icons8-bell-24.png");
        ImageView view = new ImageView(img);
        view.setFitHeight(20);
        view.setPreserveRatio(true);
        buttonMenuNotifications.setPrefSize(20, 20);
        buttonMenuNotifications.setGraphic(view);

        nrOfNotifications.setVisible(false);
        circleNotifications.setVisible(false);
        if(getNotifications().size() != 0) {
            circleNotifications.setVisible(true);
            nrOfNotifications.setVisible(true);
            nrOfNotifications.setText(String.valueOf(getNotifications().size()));


        }

    }


    public void handleButtonViewConversation(){

        try{
        scrollPaneConv.setVvalue(1.0);
        anchorPaneConversation.getChildren().clear();
        Long id = Long.parseLong(tableViewInbox.getSelectionModel().getSelectedItem().getUsername().split("#")[1]);
        
        List<Message> conversation = messagesService.findConversation(this.id, id).get();

        int posY=5;
        for (Message message : conversation) {
            Rectangle r = new Rectangle(120, 30);
            r.setArcWidth(20);
            r.setArcHeight(20);

            Label label = new Label();
            label.setText(message.getMessage());
            label.setLayoutY(posY);
            r.setLayoutY(posY);
            posY+=50;

            if(message.getFrom() == this.id){
                BackgroundFill background_fill = new BackgroundFill(Color.web("#868B8E"), CornerRadii.EMPTY, Insets.EMPTY);
                label.setBackground(new Background(background_fill));
                label.setTextFill(Color.web("#EEEDE7"));
                label.setLayoutX(280);

                r.setFill(Color.web("#868B8E"));
                r.setLayoutX(250);


                anchorPaneConversation.getChildren().add(r);
                anchorPaneConversation.getChildren().add(label);
            }
            else {
                BackgroundFill background_fill = new BackgroundFill(Color.web("#B9B7BD"), CornerRadii.EMPTY, Insets.EMPTY);
                label.setBackground(new Background(background_fill));
                label.setTextFill(Color.web("#EEEDE7"));
                label.setLayoutX(20);

                r.setFill(Color.web("#B9B7BD"));
                r.setLayoutX(10);

                anchorPaneConversation.getChildren().add(r);
                anchorPaneConversation.getChildren().add(label);
                }

            }
        scrollPaneConv.setVvalue(1.0);}
        catch(Exception exception){

        }

    }
    private List<UserDTO> getUserDTOList() throws FileNotFoundException {
        labelName.setText(utilizatorService.getOne(this.id).get().getUsername());
        FileInputStream fileInputStream = new FileInputStream(utilizatorService.getOne(this.id).get().getProfilePicPath());
        Image image = new Image(fileInputStream);
        imageView.setImage(image);

        Iterable<Utilizator> utilizatorIterable = utilizatorService.getAll();
        List<Utilizator> usersList = StreamSupport
                .stream(utilizatorIterable.spliterator(), false)
                .collect(Collectors.toList());
        Utilizator thisUser = utilizatorService.getOne(this.id).get();
        usersList.remove(thisUser);
        return usersList.stream()
                .map(user-> new UserDTO(user.getId(),  user.getUsername(), user.getFirstName(), user.getLastName())
                )
                .collect(Collectors.toList());
    }


    private List<FriendRequestDTO> getFriendRequestDTOList(){
        Iterable<FriendRequest> friendRequestIterable = friendRequestsService.getAll(this.id);
        List<FriendRequest> friendRequestList = StreamSupport
                .stream(friendRequestIterable.spliterator(), false)
                .filter(friendRequest -> friendRequest.getStatus().equals(Status.PENDING))
                .collect(Collectors.toList());
        return friendRequestList
                .stream()
                .map(friendRequest -> new FriendRequestDTO(utilizatorService.getOne(friendRequest.getId().getLeft()).get().getUsername(),utilizatorService.getOne(friendRequest.getId().getRight()).get().getUsername(),  friendRequest.getDate(),friendRequest.getStatus().name()))
                .collect(Collectors.toList());
    }

    private List<EventDTO> getEvents(){
        DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        Iterable<Event> eventIterable = eventsService.getAll();
        List<Event> eventList = StreamSupport
                .stream(eventIterable.spliterator(), false)
                .collect(Collectors.toList());
        return eventList
                .stream()
                .map(event -> new EventDTO(event.getName(), utilizatorService.getOne(event.getHostId()).get().getUsername(), event.getDescription(), event.getDate().format(myFormat)))
                .collect(Collectors.toList());
    }
    private List<Utilizator> getFriends(){

        List<Prietenie> all = friendshipsService.getAll();
        List<Utilizator> friends = all
                .stream()
                .filter(prietenie -> prietenie.getId().getLeft() == this.id)
                .map(prietenie -> utilizatorService.getOne(prietenie.getId().getRight()).get())
                .collect(Collectors.toList());

        friends.addAll(
                all.stream()
                .filter(prietenie -> prietenie.getId().getRight() == this.id)
                        .map(prietenie -> {return utilizatorService.getOne(prietenie.getId().getLeft()).get();})
                .collect(Collectors.toList())

        );

        return friends.stream().distinct().collect(Collectors.toList());
    }

    private List<Utilizator> getUsersFromConversations(){
        Iterable<Message> messageIterable = messagesService.getAll();

        List<Utilizator> usersFrom =
                StreamSupport
                .stream(messageIterable.spliterator(), false)
                .filter(message -> message.getTo().contains(this.id))
                .collect(
                        Collectors.groupingBy(Message::getFrom, Collectors.counting())

                )
                .keySet()
                .stream()
                .map(id->utilizatorService.getOne(id).get())
                .collect(Collectors.toList());

        List<Long> idsTo =  new ArrayList<>();
        StreamSupport
                .stream(messageIterable.spliterator(), false)
                .filter(message -> message.getFrom() == this.id)
                .forEach(message -> message.getTo().forEach(idsTo::add));

       List<Utilizator> usersTo =  idsTo.stream()
               .map(id->utilizatorService.getOne(id).get())
                .collect(Collectors.toList());

        usersFrom.addAll(usersTo);

        return usersFrom.stream().distinct().collect(Collectors.toList());

    }


    public void handleButtonBackToStart(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader;
        loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/startMenuView.fxml"));
        AnchorPane root = loader.load();
        Scene startScene = new Scene(root,340,400);

        StartMenuController startMenuController = loader.getController();
        startMenuController.setPrimaryStage(primaryStage);
        startMenuController.setServices(utilizatorService, friendshipsService, messagesService,friendRequestsService, eventsService);

        primaryStage.setTitle("My Social network");
        primaryStage.setScene(startScene);
        primaryStage.show();
    }


    public void checkIfUserFriend(){
        UserDTO selectedUser = tableViewAll.getSelectionModel().getSelectedItem();
        Long idSelectedUser = Long.parseLong(selectedUser.getUsername().split("#")[1]);

        Optional<Prietenie> friendship = friendshipsService.getOne(new Tuple<>(this.id, idSelectedUser));
        Optional<FriendRequest> friendRequestFromThis = friendRequestsService.getOne(new Tuple<>(this.id, idSelectedUser));
        Optional<FriendRequest> friendRequestToThis = friendRequestsService.getOne(new Tuple<>(idSelectedUser, this.id));
        try{
        if(friendship.isPresent()){
            buttonAddFriend.setDisable(true);
            buttonRemoveFriend.setDisable(false);
        }
        else{
            buttonAddFriend.setDisable(false);
            buttonRemoveFriend.setDisable(true);
        }

        if(friendRequestFromThis.isPresent() && friendRequestFromThis.get().getStatus().equals(Status.PENDING)){
            buttonAddFriend.setDisable(true);
            buttonRemoveFriend.setDisable(true);
        }

        if(friendRequestToThis.isPresent() && friendRequestToThis.get().getStatus().equals(Status.PENDING)){
            buttonAddFriend.setDisable(true);
            buttonRemoveFriend.setDisable(true);
        }}
        catch (NullPointerException exception){

        }

    }

    public void handleButtonRemoveFriend(ActionEvent actionEvent) throws Exception {
        try{
            UserDTO friendToRemove = tableViewAll.getSelectionModel().getSelectedItem();
            friendshipsService.deletePrietenie(this.id, friendToRemove.getID());
            handleCheckBoxMyFriends();
            checkIfUserFriend();}
        catch (NullPointerException exception){

        }
    }

    public void handleButtonAddFriend(ActionEvent actionEvent) throws Exception {
        try{
            UserDTO friendToAdd = tableViewAll.getSelectionModel().getSelectedItem();
            friendRequestsService.sendFriendRequest(this.id, friendToAdd.getID());
            friendRequestDTOObservableList.setAll(getFriendRequestDTOList());
            checkIfUserFriend();}
        catch(NullPointerException exception){

        }
    }

    public void handleSearch() throws FileNotFoundException {
//        String filter = searchTextField.getText();
//        List<UserDTO> list = getUserDTOList();
//
//        list = list.stream()
//                .filter(x->x.getUsername().contains(filter))
//                 .collect(Collectors.toList());
//        userDTOObservableList.setAll(list);
        //pageHandler();
        String filter = searchTextField.getText();
         friendsPagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) ->
                pageHandler());

        Page<Utilizator> utilizatorPage =  utilizatorService.getPagedAndFilteredUsers(friendsPagination.getCurrentPageIndex(), filter);
        userDTOObservableList.setAll(utilizatorPage.getContent()
                .filter(utilizator -> utilizator.getId()!=this.id)
                .map(utilizator -> new UserDTO(utilizator.getId(), utilizator.getUsername(), utilizator.getFirstName(), utilizator.getLastName()))
                .collect(Collectors.toList()));
        tableViewAll.setItems(userDTOObservableList);

    }
    public void checkFriendRequest(){
        try {
            FriendRequestDTO selectedUser = friendRequestDTOTableView.getSelectionModel().getSelectedItem();
            Long idFrom = Long.parseLong(selectedUser.getUsernameFrom().split("#")[1]);
            Long idTo = Long.parseLong(selectedUser.getUsernameTo().split("#")[1]);
            Optional<FriendRequest> friendRequest = friendRequestsService.getOne(new Tuple<>(idFrom, idTo));

            if (idTo == this.id && friendRequest.get().getStatus().equals(Status.PENDING)) {
                buttonAccept.setDisable(false);
                buttonDecline.setDisable(false);
                buttonCancel.setDisable(true);
            } else if (idFrom == this.id && friendRequest.get().getStatus().equals(Status.PENDING)) {
                buttonAccept.setDisable(true);
                buttonDecline.setDisable(true);
                buttonCancel.setDisable(false);
            }
        }
        catch (NullPointerException exception){

        }
    }


    public void handleButtonAccept() throws Exception {

        try{
        FriendRequestDTO friendToAdd = friendRequestDTOTableView.getSelectionModel().getSelectedItem();
        Long idFriend = Long.parseLong(friendToAdd.getUsernameFrom().split("#")[1]);

        friendRequestsService.answerFriendRequest(idFriend, this.id, "approve");
        friendshipsService.addFriendship(new Prietenie(new Tuple<Long, Long>(this.id, idFriend)));
        friendRequestDTOObservableList.setAll(getFriendRequestDTOList());
        checkFriendRequest();
        messagesObservableList.setAll(getFriends());}
        catch (Exception exception){

        }

    }

    public void handleButtonDecline() throws Exception {

        try{

        FriendRequestDTO friendToRemove = friendRequestDTOTableView.getSelectionModel().getSelectedItem();
        Long idFriend = Long.parseLong(friendToRemove.getUsernameFrom().split("#")[1]);

        friendRequestsService.answerFriendRequest(idFriend, this.id, "reject");
        friendRequestDTOObservableList.setAll(getFriendRequestDTOList());
        checkFriendRequest();
        messagesObservableList.setAll(getFriends());}
        catch (Exception exception){

        }

    }

    public void handleButtonCancel() throws Exception {
        try {
            FriendRequestDTO friendToRemove = friendRequestDTOTableView.getSelectionModel().getSelectedItem();
            Long idFriend = Long.parseLong(friendToRemove.getUsernameTo().split("#")[1]);
            friendRequestsService.removeFriendship(this.id, idFriend);
            friendRequestDTOObservableList.setAll(getFriendRequestDTOList());
            checkFriendRequest();
        }
        catch (Exception exception){

        }

    }

    public void handleCheckBoxMyFriends() throws FileNotFoundException {
//        if(checkBoxMyFriends.isSelected()){
//            Utilizator thisUser = utilizatorService.getOne(this.id).get();
//            Set<Utilizator> friends =  friendshipsService.friendsOfUser(thisUser).keySet();
//            List<UserDTO> dtoFriends = friends.stream()
//                .map(user-> new UserDTO(user.getId(),  user.getUsername(), user.getFirstName(), user.getLastName())
//                )
//                .collect(Collectors.toList());
//            userDTOObservableList.setAll(dtoFriends);}
//        else
//            userDTOObservableList.setAll(getUserDTOList());
        if(checkBoxMyFriends.isSelected()){
            friendsPagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) ->
                    pageHandler());

            Page<Utilizator> utilizatorPage =  utilizatorService.getPagedFriendsOfUser(friendsPagination.getCurrentPageIndex(), this.id);
            userDTOObservableList.setAll(utilizatorPage.getContent()
                    .filter(utilizator -> utilizator.getId()!=this.id)
                    .map(utilizator -> new UserDTO(utilizator.getId(), utilizator.getUsername(), utilizator.getFirstName(), utilizator.getLastName()))
                    .collect(Collectors.toList()));
            tableViewAll.setItems(userDTOObservableList);
        }
        else
            pageHandler();
    }


    public void handleButtonSend() throws Exception {

        String text = textFieldMessage.getText();
        Long idTo = Long.parseLong(tableViewInbox.getSelectionModel().getSelectedItem().getUsername().split("#")[1]);

        Message message = new Message(this.id, text);
        Message reply = new Message();
        reply.setId(-1L);
        message.setTo(Collections.singletonList(idTo));
        message.setReply(reply);

        messagesService.addMessage(message);

        textFieldMessage.setText("");

        handleButtonViewConversation();
    }

    public void generateReports1(){


        chartMessages.getData().clear();

        LocalDate begin = beginDate.getValue();
        LocalDate end = endDate.getValue();

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Messages");

        XYChart.Series series2 = new XYChart.Series();
        series2.setName("New Friends");

        List<Message> messageList = StreamSupport.stream(messagesService.getAll().spliterator(), false)
                .collect(Collectors.toList());

        //List<FriendRequest> friendRequestsList = friendRequestsService.getAll(this.id);

        List<Prietenie> friendsList = friendshipsService.getAll();


        for (LocalDate date = begin; date.isBefore(end); date = date.plusDays(1))
        {

            LocalDate finalDate = date;
            long nrMessages = messageList.stream().filter(message -> (message.getTo().contains(this.id) || message.getFrom()==this.id))
                    .filter(message -> message.getDate().getDayOfMonth() == finalDate.getDayOfMonth() && message.getDate().getMonthValue() == finalDate.getMonthValue() && message.getDate().getYear() == finalDate.getYear() )
                    .collect(Collectors.toList())
                    .stream().count();

            series1.getData().add(new XYChart.Data(finalDate.toString(), nrMessages));

            int nrFriends = friendsList.stream()
                    .filter(prietenie -> prietenie.getId().getLeft()==this.id || prietenie.getId().getRight() == this.id)
                    .filter(friendRequest -> friendRequest.getDate().getDayOfMonth() == finalDate.getDayOfMonth() && friendRequest.getDate().getMonthValue() == finalDate.getMonthValue() && friendRequest.getDate().getYear() == finalDate.getYear())
                    .collect(Collectors.toList())
                    .size();

            series2.getData().add(new XYChart.Data(finalDate.toString(), nrFriends));

        }

        chartMessages.setBarGap(3);
        chartMessages.setCategoryGap(20);
        chartMessages.getData().addAll(series1, series2);

    }

    public void generateReports2(){
        chartMessagesFrom.getData().clear();

        LocalDate begin = beginDate.getValue();
        LocalDate end = endDate.getValue();

        Long idTo = Long.parseLong(tableViewUsernamesReports.getSelectionModel().getSelectedItem().getUsername().split("#")[1]);

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Messages");

        for (LocalDate date = begin; date.isBefore(end); date = date.plusDays(1)) {

            LocalDate finalDate = date;
            long nrMessages = messagesService.findConversation(this.id, idTo).get()
                    .stream()
                    .filter(message -> message.getDate().getDayOfMonth() == finalDate.getDayOfMonth() && message.getDate().getMonthValue() == finalDate.getMonthValue() && message.getDate().getYear() == finalDate.getYear())
                    .count();

            series1.getData().add(new XYChart.Data(finalDate.toString(), nrMessages));

        }

        chartMessagesFrom.setBarGap(3);
        chartMessagesFrom.setCategoryGap(20);
        chartMessagesFrom.getData().addAll(series1);
    }

    public void handleButtonGenerateReports(){
        generateReports1();
        generateReports2();
    }

    public void handleButtonDownload() throws FileNotFoundException, DocumentException {

        Document doc = new Document();
        PdfWriter.getInstance(doc, new FileOutputStream("Report.pdf"));
        doc.open();

        String username = utilizatorService.getOne(this.id).get().getUsername();
        com.itextpdf.text.Font bold0 = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
        Paragraph paragraph0 = new Paragraph("          Activity of " + username + " during " + beginDate.getValue().toString() +"-" + endDate.getValue().toString(), bold0);

        com.itextpdf.text.Font bold = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 14, com.itextpdf.text.Font.NORMAL);
        Paragraph paragraph = new Paragraph("Messages");


        PdfPTable table = new PdfPTable(5);

        Paragraph paragraph2 = new Paragraph("New friends");

        PdfPTable table2 = new PdfPTable(2);


        Stream.of("ID", "From", "To", "Text", "Date").forEach(table::addCell);

        Stream.of("Username", "Date since friends").forEach(table2::addCell);

        List<Message> messageList = StreamSupport.stream(messagesService.getAll().spliterator(), false)
                .collect(Collectors.toList());

        List<Prietenie> friendships = friendshipsService.getAll();

        LocalDate begin = beginDate.getValue();
        LocalDate end = endDate.getValue();
        List<Message> messages = new ArrayList<>();

        List<Utilizator> friends = new ArrayList<>();

        for (LocalDate date = begin; date.isBefore(end.plusDays(1)); date = date.plusDays(1)) {
            LocalDate finalDate = date;
            messages.addAll(messageList.stream().filter(message -> (message.getTo().contains(this.id) || message.getFrom() == this.id))
                    .filter(message -> message.getDate().getDayOfMonth() == finalDate.getDayOfMonth() && message.getDate().getMonthValue()==finalDate.getMonthValue() && message.getDate().getYear()==finalDate.getYear())
                    .collect(Collectors.toList())) ;

            friends.addAll(
                    friendships.stream()
                            .filter(prietenie -> prietenie.getId().getLeft() == this.id)
                            .filter(friendRequest -> friendRequest.getDate().getDayOfMonth() == finalDate.getDayOfMonth() && friendRequest.getDate().getMonthValue()==finalDate.getMonthValue() && friendRequest.getDate().getYear()==finalDate.getYear())
                            .map(prietenie -> utilizatorService.getOne(prietenie.getId().getRight()).get())
                            .collect(Collectors.toList()));

            friends.addAll(
                    friendships.stream()
                            .filter(prietenie -> prietenie.getId().getRight() == this.id)
                            .filter(friendRequest -> friendRequest.getDate().getDayOfMonth() == finalDate.getDayOfMonth() && friendRequest.getDate().getMonthValue()==finalDate.getMonthValue() && friendRequest.getDate().getYear()==finalDate.getYear())
                            .map(prietenie -> utilizatorService.getOne(prietenie.getId().getLeft()).get())
                            .collect(Collectors.toList()));

        }
        DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        messages
                .forEach(val -> {
                    table.addCell(val.getId().toString());
                    table.addCell(utilizatorService.getOne(val.getFrom()).get().getUsername());
                    //table.addCell(val.getTo().toString());
                    table.addCell(
                             val.getTo()
                            .stream().map(to-> utilizatorService.getOne(to).get().getUsername())
                            .collect(Collectors.toList())
                            .toString());
                    table.addCell(val.getMessage());
                    table.addCell(val.getDate().format(myFormat));
                });

        friends
                .forEach(val->{
                    table2.addCell(val.getUsername());
                    table2.addCell(friendshipsService.getOne(new Tuple<Long, Long>(this.id, val.getId())).get().getDate().toLocalDate().toString());
                });

        doc.add(paragraph0);

        paragraph.add(table);
        doc.add(paragraph);

        paragraph2.add(table2);
        doc.add(paragraph2);

        doc.close();

    }

    public void handleButtonDownload2() throws FileNotFoundException, DocumentException {
        Long idTo = Long.parseLong(tableViewUsernamesReports.getSelectionModel().getSelectedItem().getUsername().split("#")[1]);

        Document doc = new Document();
        PdfWriter.getInstance(doc, new FileOutputStream("Report2.pdf"));
        doc.open();

        String username = utilizatorService.getOne(this.id).get().getUsername();
        com.itextpdf.text.Font bold0 = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 14, com.itextpdf.text.Font.NORMAL);
       // Paragraph paragraph0 = new Paragraph("          Activity of " + username + " during " + beginDate.getValue().toString() +"-" + endDate.getValue().toString());


        com.itextpdf.text.Font bold = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
        Paragraph paragraph = new Paragraph("Conversation between  " +username+" and " + utilizatorService.getOne(idTo).get().getUsername(), bold0);

        PdfPTable table = new PdfPTable(5);

        Stream.of("ID", "From", "To", "Text", "Date").forEach(table::addCell);

        LocalDate begin = beginDate.getValue();
        LocalDate end = endDate.getValue();
        List<Message> messages = new ArrayList<>();


        for (LocalDate date = begin; date.isBefore(end); date = date.plusDays(1)) {
            LocalDate finalDate = date;
            messages.addAll(messagesService
                    .findConversation(this.id, idTo).get()
                    .stream()
                    .filter(message -> message.getDate().getDayOfMonth() == finalDate.getDayOfMonth() && message.getDate().getMonthValue()==finalDate.getMonthValue() && message.getDate().getYear()==finalDate.getYear())
                    .collect(Collectors.toList())
            );
        }

        DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        messages.stream()
                .sorted(Comparator.comparing(Message::getDate))
                .forEach(val -> {
                    table.addCell(val.getId().toString());
                    table.addCell(utilizatorService.getOne(val.getFrom()).get().getUsername());
                    //table.addCell(val.getTo().toString());
                    table.addCell(
                            val.getTo()
                                    .stream().map(to-> utilizatorService.getOne(to).get().getUsername())
                                    .collect(Collectors.toList())
                                    .toString());
                    table.addCell(val.getMessage());
                    table.addCell(val.getDate().format(myFormat));
                });
        paragraph.add(table);
        doc.add(paragraph);

        doc.close();

    }

    public void checkUserGoingToEvent(){
        Long e_id = Long.parseLong(tableEvents.getSelectionModel().getSelectedItem().getName().split("#")[1]);
        Optional<Event> event = eventsService.getOne(e_id);
        if(event.isPresent()){

            if(event.get().getParticipants().contains(new Tuple(this.id, true))){
                buttonGoing.setDisable(true);
                buttonNotGoing.setDisable(false);
                buttonNotifications.setDisable(false);
               // buttonNotifications.setVisible(false);
                buttonNotifications.setText("Unsubscribe");
            }
            else if(event.get().getParticipants().contains(new Tuple(this.id, false))){
                buttonGoing.setDisable(true);
                buttonNotGoing.setDisable(false);
                buttonNotifications.setDisable(false);
                buttonNotifications.setText("Subscribe");
            }
            else {
                buttonGoing.setDisable(false);
                buttonNotGoing.setDisable(true);
                buttonNotifications.setDisable(true);


            }

        }
    }
    public void handleButtonGoing(){
        Long e_id = Long.parseLong(tableEvents.getSelectionModel().getSelectedItem().getName().split("#")[1]);
        Long u_id = this.id;
        this.eventsService.addUserToEvent(e_id, u_id);

//        buttonGoing.setDisable(true);
//        buttonNotGoing.setDisable(false);
//
//        buttonNotifications.setDisable(false);
//        if(eventsService.getOne(e_id).get().getParticipants().contains(new Tuple(this.id, true)))
//            buttonNotifications.setText("Unsubscribe");
//        else
//        if(eventsService.getOne(e_id).get().getParticipants().contains(new Tuple(this.id, false)))
//            buttonNotifications.setText("Subscribe");
        checkUserGoingToEvent();

    }


    public void handleButtonNotGoing(){
        Long e_id = Long.parseLong(tableEvents.getSelectionModel().getSelectedItem().getName().split("#")[1]);
        Long u_id = this.id;
        this.eventsService.removeUserFromEvent(e_id, u_id);

//        buttonGoing.setDisable(false);
//        buttonNotGoing.setDisable(true);

        checkUserGoingToEvent();

    }

    public void handleButtonNotifications(){
        Long e_id = Long.parseLong(tableEvents.getSelectionModel().getSelectedItem().getName().split("#")[1]);
        Long u_id = this.id;
        if(buttonNotifications.getText().equals("Subscribe"))
            eventsService.updateNotificationResponse(e_id, u_id, true);
        else
            if(buttonNotifications.getText().equals("Unsubscribe"))
                eventsService.updateNotificationResponse(e_id, u_id, false);

        checkUserGoingToEvent();

        notificationsObservabeList.setAll(getNotifications());
        if(getNotifications().size() != 0){
            circleNotifications.setVisible(true);
            nrOfNotifications.setVisible(true);
            nrOfNotifications.setText(String.valueOf(getNotifications().size()));
        }

    }

    public List<String> getNotifications(){


        DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        List<Event> myEvents = StreamSupport.stream(eventsService.getAll().spliterator(), false)
                .filter(event -> event.getParticipants().contains(new Tuple(this.id, true)))
                .filter(event -> event.getDate().toLocalDate().getMonthValue() >= LocalDateTime.now().toLocalDate().getMonthValue() &&  event.getDate().toLocalDate().getYear() >= LocalDateTime.now().toLocalDate().getYear())
                .collect(Collectors.toList());

        List<String> notifications =  myEvents
                .stream()
                .map(event -> "Evenimentul " + event.getName() + "va avea loc curand!(" + event.getDate().format(myFormat)+")")
                .collect(Collectors.toList());
        //String.valueOf(notifications.size())
//        if(notifications.size() != 0) {
//            buttonMenuNotifications.setText("New notifications!");
//            buttonMenuNotifications.setStyle("-fx-background-color: #ff0000");}
//        if(notifications.size()!=0){
//            circleNotifications.setVisible(true);
//            nrOfNotifications.setVisible(true);
//            nrOfNotifications.setText(String.valueOf(getNotifications().size()));
//        }

        return notifications;
    }

    public void handleButtonMenuNotifications(){
        notificationsObservabeList.setAll(getNotifications());
        listViewNotifications.setVisible(true);
//        buttonMenuNotifications.setStyle("-fx-background-color: #EEEDE7");
//        buttonMenuNotifications.setText("Notifications");
        circleNotifications.setVisible(false);
        nrOfNotifications.setVisible(false);
    }

    public void handleExitListViewNotifications(){
        listViewNotifications.setVisible(false);
    }

    public void handleButtonAddEvent() throws Exception {
        String name = textFieldEventName.getText();
        String description = textAreaEventDescription.getText();
        Long hostId = this.id;
        LocalDate date = datePickerEvent.getValue();
        Integer hour = spinnerHour.getValue();
        Integer minutes = spinnerMinutes.getValue();

        LocalDateTime completeDate = date.atTime(hour, minutes);

        Event event = new Event(hostId, name, description, completeDate);
        eventsService.addEvent(event);

        eventObservableList.setAll(getEvents());
    }

    public void pageHandler(){

        if(checkBoxMyFriends.isSelected()){
            friendsPagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) ->
                    pageHandler());

            Page<Utilizator> utilizatorPage =  utilizatorService.getPagedFriendsOfUser(friendsPagination.getCurrentPageIndex(), this.id);
            userDTOObservableList.setAll(utilizatorPage.getContent()
                    .filter(utilizator -> utilizator.getId()!=this.id)
                    .map(utilizator -> new UserDTO(utilizator.getId(), utilizator.getUsername(), utilizator.getFirstName(), utilizator.getLastName()))
                    .collect(Collectors.toList()));
            tableViewAll.setItems(userDTOObservableList);
        }
        else{
            friendsPagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) ->
                    pageHandler());

            Page<Utilizator> utilizatorPage =  utilizatorService.getPagedUsers(friendsPagination.getCurrentPageIndex());
            userDTOObservableList.setAll(utilizatorPage.getContent()
                    .filter(utilizator -> utilizator.getId()!=this.id)
                    .map(utilizator -> new UserDTO(utilizator.getId(), utilizator.getUsername(), utilizator.getFirstName(), utilizator.getLastName()))
                    .collect(Collectors.toList()));
        }

    }



}
