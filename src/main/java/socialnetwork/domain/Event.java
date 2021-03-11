package socialnetwork.domain;

import jdk.vm.ci.meta.Local;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Event extends Entity<Long>{

    private String name;
    private String description;
    private LocalDateTime date;
    private Long hostId;

    private List<Tuple<Long,Boolean>> participants = new ArrayList<>();

    public List<Tuple<Long,Boolean>> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Tuple<Long,Boolean>> participants) {
        this.participants = participants;
    }

    public Event(Long hostId, String name, String description, LocalDateTime date){
        this.hostId = hostId;
        this.name = name;
        this.description = description;
        this.date = date;
    }

    public Event(){};

    public void addParticipant(Long id, Boolean notification){
        this.participants.add(new Tuple(id, notification));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(name, event.name) &&
                Objects.equals(description, event.description) &&
                Objects.equals(date, event.date) &&
                Objects.equals(hostId, event.hostId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, date, hostId);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Long getHostId() {
        return hostId;
    }

    public void setHostId(Long hostId) {
        this.hostId = hostId;
    }
}
