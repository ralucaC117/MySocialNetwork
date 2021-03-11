package socialnetwork.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Utilizator extends Entity<Long>{
    private String firstName;
    private String lastName;
    private List<Utilizator> friends;

    private String username;
    private String password;
    private String salt;

    private String profilePicPath;




    public String getUsername() {
        return username;
    }

    public void setProfilePicPath(String profilePicPath){
        this.profilePicPath = profilePicPath;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Utilizator(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.friends = new ArrayList<Utilizator>();
    }

    public String getProfilePicPath(){
        return profilePicPath;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Utilizator> getFriends() {
        return friends;
    }

    public void addFriend(Utilizator u){
        friends.add(u);
    }

    @Override
    public String toString() {
        String friends=" ";
        for(Utilizator u:this.friends){
            friends+=u.getId();
            friends+=" ";
        }
        return "Utilizator{" +
                "ID=" + getId().toString() + ' '+
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", friends=[" + friends +
                "]}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Utilizator)) return false;
        Utilizator that = (Utilizator) o;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName());
                //getFriends().equals(that.getFriends());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName());
    }
}