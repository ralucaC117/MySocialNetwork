package socialnetwork.domain;

import java.time.LocalDateTime;

public class EventDTO {
    private String name;
    private String host;
    private String description;
    private String date;

    public EventDTO(String name, String host, String description, String date){
        this.name = name;
        this.host = host;
        this.description = description;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
