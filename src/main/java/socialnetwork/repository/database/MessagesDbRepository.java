package socialnetwork.repository.database;

import socialnetwork.domain.Message;
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
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

public class MessagesDbRepository implements Repository<Long, Message>{
    private String url;
    private String username;
    private String password;
    private Validator<Utilizator> validator;

    public MessagesDbRepository(String url, String username, String password, Validator validator){
        this.url=url;
        this.username=username;
        this.password=password;
        this.validator=validator;
    }

    @Override
    public Optional<Message> findOne(Long aLong) {
        Optional<Message> o = Optional.empty();
        Message m;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT messages.id, messages.mfrom, messages.mdate, messages.mtext, messages.reply, receivers.id_user FROM messages left join  receivers on messages.id=receivers.id_mesaj ");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Long id = resultSet.getLong(1);

                if(id == aLong){
                    Long from = resultSet.getLong(2);
                    LocalDate date = resultSet.getDate(3).toLocalDate();
                    String message = resultSet.getString(4);
                    Long isReply = resultSet.getLong(5);
                    Long to = resultSet.getLong(6);
                    m = new Message(from,message);
                    m.setId(id);
                    m.setDate(date.atTime(LocalTime.from(LocalDateTime.now())));
                    m.addTo(to);
                    if(isReply != -1 && isReply != 0){
                        Message reply = new Message();
                        reply.setId(isReply);
                        m.setReply(reply);
                    }
                    o=Optional.of(m);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return o;

    }

    @Override
    public Iterable<Message> findAll() {

        Set<Message> messages = new HashSet<Message>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT messages.id, messages.mfrom, messages.mtext, messages.mdate, messages.reply, receivers.id_user  FROM messages left join  receivers on messages.id=receivers.id_mesaj ");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong(1);
                Long from = resultSet.getLong(2);
                String message = resultSet.getString(3);
                Timestamp date = resultSet.getTimestamp(4);
                Long isReply = resultSet.getLong(5);
                Long to = resultSet.getLong(6);

                Message m = new Message(from,message);
                m.setId(id);
                m.setDate(date.toLocalDateTime());
                if(isReply != -1 && isReply != 0){
                    Message reply = this.findOne(isReply).get();
                    m.setReply(reply);
                }
                else{
                    Message reply = new Message();
                    reply.setId(-1L);
                    m.setReply(reply);
                }

                if(!messages.contains(m)){
                    m.addTo(to);
                    messages.add(m);
                }
                else
                    for (Iterator<Message> it = messages.iterator(); it.hasNext(); ) {
                        Message message1 = it.next();
                        if (message1.equals(m))
                            message1.addTo(to);
                    }
            }

            return messages;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return messages;
    }

    @Override
    public Optional<Message> save(Message entity) throws Exception {

        Long id = null;
        if(entity ==  null)
            throw new IllegalArgumentException("entity must not be null");
        //validare
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO messages(mfrom, mdate, mtext, reply) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        ){
            //statement.setLong(1, entity.getId());
            statement.setLong(1, entity.getFrom());
            //statement.setDateTime(2, Date.valueOf(LocalDateTime.now()));
            statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            statement.setString(3, entity.getMessage());
            statement.setLong(4, entity.getReply().getId());
            statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            if(rs.next())
             id=rs.getLong(1);

        }
        catch(SQLException e){
            return Optional.of(entity);
        }
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("INSERT INTO receivers(id_mesaj, id_user) VALUES (?,?)");
        ){
            for(Long to:entity.getTo()){
                statement.setLong(1, id);
                statement.setLong(2, to);
                statement.executeUpdate();
            }
            return Optional.empty();
        }
        catch (SQLException e){
            return Optional.of(entity);
        }

    }

    @Override
    public Optional<Message> delete(Long aLong) throws Exception {
        return Optional.empty();
    }

    @Override
    public Optional<Message> update(Message entity) {
        return Optional.empty();
    }
}
