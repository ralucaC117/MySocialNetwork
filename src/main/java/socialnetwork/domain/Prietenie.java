package socialnetwork.domain;

import java.time.LocalDateTime;


public class Prietenie extends Entity<Tuple<Long,Long>> {

    LocalDateTime date;

    public Prietenie(Tuple<Long, Long> tuple) {
        super.setId(tuple);
        date = LocalDateTime.now();
    }

    /**
     * @return the date when the friendship was created
     */
    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date){
        this.date = date;
    }
    @Override
    public String toString() {
        return "Prietenie{" + super.toString() +
                '}';
    }
}
