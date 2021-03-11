package socialnetwork.domain;

import java.time.LocalDate;

public class FriendRequestDTO {
    String usernameFrom;
    String usernameTo;
    String status;
    LocalDate date;

    public FriendRequestDTO(String usernameFrom, String usernameTo, LocalDate date,String status){
        this.usernameFrom = usernameFrom;
        this.usernameTo = usernameTo;
        this.status = status;
        this.date = date;
    }

    public String getUsernameFrom() {
        return usernameFrom;
    }

    public void setUsernameFrom(String usernameFrom) {
        this.usernameFrom = usernameFrom;
    }

    public String getUsernameTo() {
        return usernameTo;
    }

    public void setUsernameTo(String usernameTo) {
        this.usernameTo = usernameTo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "FriendRequestDTO{" +
                "usernameFrom='" + usernameFrom + '\'' +
                ", usernameTo='" + usernameTo + '\'' +
                ", status='" + status + '\'' +
                ", date=" + date +
                '}';
    }
}
