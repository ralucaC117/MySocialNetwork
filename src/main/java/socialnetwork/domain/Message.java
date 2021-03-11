package socialnetwork.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Message extends Entity<Long> {
    private Long from;
    private List<Long> to = new ArrayList<>();
    private String message = null;
    private LocalDateTime date;
    private Message reply =null;

    public Message(){}

    public Message(Long from, String message) {
        this.from = from;
        this.message = message;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public List<Long> getTo() {
        return to;
    }

    public void setTo(List<Long> to) {
        this.to = to;
    }

    public void addTo(Long to){
        this.to.add(to);
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Message getReply() {
        return reply;
    }

    public void setReply(Message reply) {
        this.reply = reply;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return Objects.equals(from, message1.from) &&
                Objects.equals(message, message1.message) &&
                Objects.equals(date, message1.date) &&
                Objects.equals(reply, message1.reply);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, message, date, reply);
    }

    @Override
    public String toString() {
        DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return "Message{" +
                "MessageID="+ this.getId() +
                ", from=" + from +
                ", to=" + to +
                ", message='" + message + '\'' +
                ", date=" + date.format(myFormat) +
                ", replyTo: '" + reply.getMessage() + '\''+
                '}';
    }
}
