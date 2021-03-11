package socialnetwork.repository.database;

import socialnetwork.domain.FriendRequest;
import socialnetwork.domain.Prietenie;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class FriendshipsDbRepository implements Repository<Tuple<Long, Long>, Prietenie> {
    private String url;
    private String username;
    private String password;
    private Validator<Prietenie> validator;

    public FriendshipsDbRepository(String url, String username, String password, Validator<Prietenie> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Optional<Prietenie> findOne(Tuple<Long, Long> longLongTuple) {
        Optional<Prietenie> o = Optional.empty();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT id1, id2, f_date from friendships WHERE id1= ? AND id2= ? OR id1=? AND id2=?");){

                statement.setLong(1, longLongTuple.getLeft());
                statement.setLong(2, longLongTuple.getRight());
                statement.setLong(3, longLongTuple.getRight());
                statement.setLong(4, longLongTuple.getLeft());

                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    Long id1 = resultSet.getLong("id1");
                    Long id2 = resultSet.getLong("id2");
                    Date date = resultSet.getDate("f_date");

                    LocalDateTime dateTime = date.toLocalDate().atTime(17,00);
                    Tuple<Long, Long> id = new Tuple(id1, id2);

                    Prietenie prietenie = new Prietenie(id);
                    prietenie.setDate(dateTime);
                    o = Optional.of(prietenie);
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return o;
    }

    @Override
    public Iterable<Prietenie> findAll() {
        Set<Prietenie> friendships = new HashSet<Prietenie>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM friendships");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id1 = resultSet.getLong(1);
                Long id2 = resultSet.getLong(2);
                LocalDate date = resultSet.getDate(3).toLocalDate();

                Prietenie p = new Prietenie(new Tuple<>(id1, id2));
                //p.setDate(date.atTime(LocalTime.from(LocalDateTime.now())));
                //date.atTime(17,00);

                LocalDateTime dateTime = date.atTime(17,00);
                p.setDate(dateTime);

                friendships.add(p);
            }

            return friendships;
        } catch (SQLException exception) {
            //exception.printStackTrace();
        }

        return friendships;

    }

    @Override
    public Optional<Prietenie> save(Prietenie entity) throws Exception {
        if (entity == null)
            throw new IllegalArgumentException("entity must not be null");
        this.validator.validate(entity);

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO friendships(id1, id2, f_date) VALUES (?,?,?)");
        ) {
            statement.setLong(1, entity.getId().getLeft());
            statement.setLong(2, entity.getId().getRight());
            statement.setDate(3, Date.valueOf(entity.getDate().toLocalDate()));
            statement.executeUpdate();
            return Optional.empty();

        } catch (SQLException e) {
            //e.printStackTrace();
        }

        return Optional.of(entity);
    }

    @Override
    public Optional<Prietenie> delete(Tuple<Long, Long> longLongTuple) throws Exception {
        if (longLongTuple == null)
            throw new IllegalArgumentException("id must not be null");
        Optional<Prietenie> o = findOne(longLongTuple);
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("DELETE FROM friendships WHERE (id1=? AND id2=?) OR (id2=? AND id1=?) ");
        ) {

            statement.setLong(1, longLongTuple.getLeft());
            statement.setLong(2, longLongTuple.getRight());
            statement.setLong(3, longLongTuple.getLeft());
            statement.setLong(4, longLongTuple.getRight());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return o;
    }

    @Override
    public Optional<Prietenie> update(Prietenie entity) {
        return Optional.empty();
    }


}
