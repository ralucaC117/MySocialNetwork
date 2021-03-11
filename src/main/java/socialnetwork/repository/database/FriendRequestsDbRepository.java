package socialnetwork.repository.database;

import socialnetwork.domain.*;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class FriendRequestsDbRepository implements Repository<Tuple<Long, Long>, FriendRequest> {
    private String url;
    private String username;
    private String password;
    private Validator<FriendRequest> validator;

    public FriendRequestsDbRepository(String url, String username, String password, Validator<FriendRequest> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Optional<FriendRequest> findOne(Tuple<Long, Long> longLongTuple) {
        Optional<FriendRequest> o = Optional.empty();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendshiprequests WHERE ufrom= ? AND uto= ? ");) {

                  statement.setLong(1, longLongTuple.getLeft());
                  statement.setLong(2, longLongTuple.getRight());
                  ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    Long from = resultSet.getLong(1);
                    Long to = resultSet.getLong(2);
                    String status = resultSet.getString(3);

                    FriendRequest friendRequest = new FriendRequest();
                    friendRequest.setId(new Tuple<Long, Long>(from, to));
                    if(status.equals("PENDING"))
                        friendRequest.setStatus(Status.PENDING);
                    if(status.equals("APPROVED"))
                        friendRequest.setStatus(Status.APPROVED);
                    if(status.equals("REJECTED"))
                        friendRequest.setStatus(Status.REJECTED);
                    o=Optional.of(friendRequest);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return o;
    }

    @Override
    public Iterable<FriendRequest> findAll() {

        Set<FriendRequest> friendRequests = new HashSet<FriendRequest>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM friendshiprequests ");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Long from = resultSet.getLong(1);
                Long to = resultSet.getLong(2);
                String status = resultSet.getString(3);
                LocalDate date = resultSet.getDate(4).toLocalDate();


                FriendRequest friendRequest = new FriendRequest();
                friendRequest.setId(new Tuple<Long, Long>(from, to));

                if(status.equals("APPROVED"))
                    friendRequest.setStatus(Status.APPROVED);
                else if(status.equals("REJECTED"))
                    friendRequest.setStatus(Status.REJECTED);
                friendRequest.setDate(date);
                friendRequests.add(friendRequest);
            }

            return friendRequests;
        } catch (SQLException exception) {
            //exception.printStackTrace();
        }
        return friendRequests;
    }

    @Override
    public Optional<FriendRequest> save(FriendRequest friendRequest) throws Exception {
        if (friendRequest == null)
            throw new IllegalArgumentException("friend request must not be null");
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO friendshiprequests(ufrom, uto, status,fr_date) VALUES(?,?,?,?)");
             ) {

            statement.setLong(1, friendRequest.getId().getLeft());
            statement.setLong(2, friendRequest.getId().getRight());
            statement.setString(3, friendRequest.getStatus().toString());
            statement.setDate(4, Date.valueOf(friendRequest.getDate()));
            statement.executeUpdate();
            return Optional.empty();


        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return Optional.of(friendRequest);
    }

    @Override
    public Optional<FriendRequest> delete(Tuple<Long, Long> longLongTuple) throws Exception {
        if(longLongTuple == null)
            throw new IllegalArgumentException("id must not be null");
        Optional<FriendRequest> o = findOne(longLongTuple);
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("DELETE FROM friendshiprequests WHERE ufrom= ? and uto = ? ");
        ){
            statement.setLong(1, longLongTuple.getLeft());
            statement.setLong(2, longLongTuple.getRight());
            statement.executeUpdate();

        }
        catch(SQLException e){
        }
        return o;
    }

    @Override
    public Optional<FriendRequest> update(FriendRequest friendRequest) {

        if (friendRequest == null)
            throw new IllegalArgumentException("friend request must not be null");
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("UPDATE friendshiprequests SET status = ? WHERE ufrom = ? and uto = ?");
             ) {

            statement.setString(1, friendRequest.getStatus().toString());
            statement.setLong(2, friendRequest.getId().getLeft());
            statement.setLong(3, friendRequest.getId().getRight());
            statement.executeUpdate();
            return Optional.empty();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }


        return Optional.of(friendRequest);
    }
}
