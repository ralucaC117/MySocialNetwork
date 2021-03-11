package socialnetwork.repository.database;

import socialnetwork.domain.Utilizator;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository;

//import javax.xml.validation.Validator;
import java.sql.*;
import java.util.*;

public class UtilizatorDbRepository implements Repository<Long, Utilizator> {
    private String url;
    private String username;
    private String password;
    private Validator<Utilizator> validator;

    public UtilizatorDbRepository(String url, String username, String password, Validator<Utilizator> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }
    @Override
    public Optional<Utilizator> findOne(Long aLong) {
        Optional<Utilizator> o = Optional.empty();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users WHERE users.id = ?");
             )
             {
                 statement.setLong(1, aLong);
                 ResultSet resultSet = statement.executeQuery();
                 while(resultSet.next())
                 {String firstName = resultSet.getString(2);
                 String lastName = resultSet.getString(3);
                 Utilizator utilizator = new Utilizator(firstName, lastName);
                 utilizator.setId(aLong);

                 String username = resultSet.getString(4);
                 String password = resultSet.getString(5);
                 String salt = resultSet.getString(6);

                 String profilePicPath = resultSet.getString(7);

                 utilizator.setUsername(username);
                 utilizator.setPassword(password);
                 utilizator.setSalt(salt);
                 utilizator.setProfilePicPath(profilePicPath);

                 o = Optional.of(utilizator);}
            /*while (resultSet.next()) {
                Long id = resultSet.getLong(1);
                String firstName = resultSet.getString(2);
                String lastName = resultSet.getString(3);

                Utilizator utilizator = new Utilizator(firstName, lastName);
                utilizator.setId(id);
                if(id == aLong)
                    o=Optional.of(utilizator);
            }*/

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return o;
    }

    @Override
    public Iterable<Utilizator> findAll() {
        Set<Utilizator> users = new HashSet<Utilizator>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT users.id, users.nume, users.prenume, users.username, friendships.id2, users.profile_pic_path from users left join friendships on users.id=friendships.id1 ");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong(1);
                String firstName = resultSet.getString(2);
                String lastName = resultSet.getString(3);
                String username = resultSet.getString(4);

                Long friendID = resultSet.getLong(5);

                String profilePicPath = resultSet.getString(6);

                Utilizator utilizator = new Utilizator(firstName, lastName);
                utilizator.setId(id);
                utilizator.setUsername(username);
                utilizator.setProfilePicPath(profilePicPath);

                Optional<Utilizator> friend = findOne(friendID);

                if (friendID != 0) {
                    utilizator.addFriend(friend.get());
                    friend.get().addFriend(utilizator);
                    if (!users.contains(utilizator))
                        users.add(utilizator);
                    else {
                        for (Iterator<Utilizator> it = users.iterator(); it.hasNext(); ) {
                            Utilizator u = it.next();
                            if (u.equals(utilizator))
                                u.addFriend(friend.get());
                        }
                    }
                    if (!users.contains(friend.get())) {
                        users.add(friend.get());
                    } else {
                        for (Iterator<Utilizator> it = users.iterator(); it.hasNext(); ) {
                            Utilizator u = it.next();
                            if (u.equals(friend.get()))
                                u.addFriend(utilizator);
                        }
                    }
                }
                else
                    users.add(utilizator);

            }
                return users;

        }

        catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }


    @Override
    public Optional<Utilizator> save(Utilizator entity) throws Exception {

        if(entity ==  null)
            throw new IllegalArgumentException("entity must not be null");
        this.validator.validate(entity);

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO users(id, nume, prenume, username, h_password, salt) VALUES (?,?,?,?,?,?)");
             ){
            statement.setLong(1, entity.getId());
            statement.setString(2, entity.getFirstName());
            statement.setString(3, entity.getLastName());
            statement.setString(4, entity.getUsername());
            statement.setString(5, entity.getPassword());
            statement.setString(6, entity.getSalt());
            statement.executeUpdate();
            return Optional.empty();

        }
        catch(SQLException e){
            return Optional.of(entity);
        }

    }

    @Override
    public Optional<Utilizator> delete(Long aLong) throws Exception {

        if(aLong == null)
            throw new IllegalArgumentException("id must not be null");
        Optional<Utilizator> o = findOne(aLong);
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE id=? ");
        ){
            statement.setLong(1, aLong);
            statement.executeUpdate();

        }
        catch(SQLException e){
        }
        return o;
    }

    @Override
    public Optional<Utilizator> update(Utilizator entity) {
        return Optional.empty();
    }


}
