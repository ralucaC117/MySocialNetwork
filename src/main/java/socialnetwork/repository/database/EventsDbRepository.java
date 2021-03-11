package socialnetwork.repository.database;

import jdk.vm.ci.meta.Local;
import socialnetwork.domain.Event;
import socialnetwork.domain.Message;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

public class EventsDbRepository implements Repository<Long, Event> {

    private String url;
    private String username;
    private String password;
    private Validator<Event> validator;

    public EventsDbRepository(String url, String username, String password, Validator<Event> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Optional<Event> findOne(Long aLong) {
        Optional<Event> o = Optional.empty();
        Event event = new Event();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT events.id, events.e_name, events.e_date, events.host, events.e_description, eventsusers.u_id, eventsusers.notif  FROM events left join  eventsusers on events.id=eventsusers.e_id ");
             ResultSet resultSet = statement.executeQuery();
        )
        {
            while(resultSet.next()) {
                Long id = resultSet.getLong(1);
                if(id == aLong){
                    String name = resultSet.getString(2);
                    Timestamp date = resultSet.getTimestamp(3);
                    Long hostId = resultSet.getLong(4);
                    String description = resultSet.getString(5);

                    Long participant = resultSet.getLong(6);
                    Boolean notifications = resultSet.getBoolean(7);

                    //event = new Event(hostId, name, description, date.toLocalDateTime());
                    event.setName(name);
                    event.setHostId(hostId);
                    event.setDescription(description);
                    event.setDate(date.toLocalDateTime());
                    event.setId(aLong);
                    event.addParticipant(participant, notifications);

                    o = Optional.of(event);}
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return o;
    }

    @Override
    public Iterable<Event> findAll() {

        Set<Event> events = new HashSet<Event>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT events.id, events.e_name, events.e_date, events.host, events.e_description, eventsusers.u_id, eventsusers.notif  FROM events left join  eventsusers on events.id=eventsusers.e_id ");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong(1);
                String name =  resultSet.getString(2);
                Timestamp date = resultSet.getTimestamp(3);
                Long hostId = resultSet.getLong(4);
                String description = resultSet.getString(5);

                Long participant = resultSet.getLong(6);
                Boolean notifications = resultSet.getBoolean(7);


                Event event = new Event();
                //Event event = new Event(hostId, name, description, date.toLocalDateTime());
                event.setName(name);
                event.setHostId(hostId);
                event.setDescription(description);
                event.setDate(date.toLocalDateTime());
                event.setId(id);

                if(!events.contains(event)){
                    event.addParticipant(participant, notifications);
                    events.add(event);
                }
                else
                    for (Iterator<Event> it = events.iterator(); it.hasNext(); ) {
                        Event event1 = it.next();
                        if (event1.equals(event))
                            event1.addParticipant(participant, notifications);
                    }
            }

            return events;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return events;
    }

    @Override
    public Optional<Event> save(Event entity) throws Exception {

        if(entity ==  null)
            throw new IllegalArgumentException("entity must not be null");
        this.validator.validate(entity);

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO events(e_name, e_date, host, e_description) VALUES (?,?,?,?)");
        ){

            statement.setString(1, entity.getName());
            statement.setTimestamp(2, Timestamp.valueOf(entity.getDate()));
            statement.setLong(3, entity.getHostId());
            statement.setString(4, entity.getDescription());
            statement.executeUpdate();
            return Optional.empty();
        }
        catch(SQLException e){
            return Optional.of(entity);
        }

    }

    @Override
    public Optional<Event> delete(Long aLong) throws Exception {
        return Optional.empty();
    }


    public Optional<Tuple<Long, Long>> saveUserToEvent(Long e_id, Long u_id){
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("INSERT INTO eventsusers(e_id, u_id, notif) VALUES (?,?,?)");
        ){

                statement.setLong(1, e_id);
                statement.setLong(2, u_id);
                statement.setBoolean(3, false);
                statement.executeUpdate();
                return  Optional.of(new Tuple<>(e_id, u_id));
        }
        catch (SQLException e){
        }
        return Optional.empty();
    }

    public Optional<Tuple<Long,Long>> deleteUserFromEvent(Long e_id, Long u_id){
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("DELETE FROM eventsusers WHERE e_id=? AND u_id=?");
        ){

            statement.setLong(1, e_id);
            statement.setLong(2, u_id);
            statement.executeUpdate();
            return Optional.empty();
        }
        catch (SQLException e){
        }
        return  Optional.of(new Tuple<>(e_id, u_id));

    }

    public void answerNotification(Long e_id, Long u_id, Boolean notif) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("UPDATE eventsusers SET notif = ? WHERE e_id = ? and u_id = ?");) {

            statement.setBoolean(1,notif);
            statement.setLong(2, e_id);
            statement.setLong(3, u_id);

            statement.executeUpdate();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public Optional<Event> update(Event entity) {
        return  Optional.empty();

    }
}
