package socialnetwork.domain;

import java.time.LocalDate;

public class FriendRequest extends Entity<Tuple<Long, Long>>{

    private Utilizator from;
    private Utilizator to;
    private Status status;
    private LocalDate date;

    public FriendRequest(){
        this.status = Status.PENDING;
    }

    public Utilizator getFrom() {
        return from;
    }

    public void setFrom(Utilizator from) {
        this.from = from;
    }

    public Utilizator getTo() {
        return to;
    }

    public void setTo(Utilizator to) {
        this.to = to;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
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
        return "FriendRequest{" +
//                "from=" + from.getFirstName() + " " + from.getLastName() + " " +
//                ", to=" + to.getFirstName() + " " + to.getLastName() + " " +
                "from=" + getId().getLeft().toString()+" "+
                "to=" + getId().getRight().toString()+" "+
                ", status=" + status.toString() +
                '}';
    }
}
