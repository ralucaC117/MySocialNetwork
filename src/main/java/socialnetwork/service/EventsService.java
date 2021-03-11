package socialnetwork.service;

import socialnetwork.domain.Event;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.Utilizator;
import socialnetwork.repository.Repository;
import socialnetwork.repository.database.EventsDbRepository;

import java.util.Optional;

public class EventsService {

    //private Repository<Long, Event> repository;
    private EventsDbRepository repository;

    //public EventsService(Repository<Long, Event> repository){
       // this.repository = repository;
   // }

    public EventsService(EventsDbRepository repository){
        this.repository = repository;
    }
    public Iterable<Event> getAll(){
        return repository.findAll();
    }

    public Optional<Event> getOne(Long id){
        return  repository.findOne(id);
    }

    public Optional<Event> addEvent(Event event) throws Exception {
        Optional<Event> o = repository.save(event);
        return o;
    }

    public Optional<Tuple<Long, Long>> addUserToEvent(Long e_id, Long u_id){
        return repository.saveUserToEvent(e_id, u_id);
    }

    public Optional<Tuple<Long, Long>> removeUserFromEvent(Long e_id, Long u_id){
        return repository.deleteUserFromEvent(e_id, u_id);
    }

    public void updateNotificationResponse(Long e_id, Long u_id, Boolean answer){
        repository.answerNotification(e_id, u_id, answer);
    }
}
