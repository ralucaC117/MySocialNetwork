package socialnetwork.service;

import socialnetwork.domain.Message;
import socialnetwork.domain.Utilizator;
import socialnetwork.repository.Repository;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MessagesService {
    private Repository<Long, Message> repository;

    public MessagesService(Repository<Long, Message> repository){
        this.repository = repository;
    }

    public Optional<Message> addMessage(Message message) throws Exception {
        return repository.save(message);
    }

    public List<Long> findUsersFromComversations(Long id){
        Iterable<Message> all = repository.findAll();
        List<Message> allMessages = new ArrayList<>();
        all.forEach(allMessages::add);

        List<Long> users = allMessages.stream()
                .filter(     x->   (x.getFrom()==id || x.getTo().contains(id))  )
                .map(x->{return x.getFrom();})
                .collect(Collectors.toList());
        return users;

    }

    public Optional<List<Message>> findConversation(Long id, Long id2){
        Iterable<Message> all = repository.findAll();
        List<Message> allMessages = new ArrayList<>();
        all.forEach(allMessages::add);

        List<Message> inbox = allMessages.stream()
                .filter(x->(x.getFrom()==id && x.getTo().contains(id2) || x.getFrom()==id2 && x.getTo().contains(id)))
                .sorted(Comparator.comparing(Message::getDate))
                .collect(Collectors.toList());
        if(inbox.isEmpty())
            return  Optional.empty();
        else
            return Optional.of(inbox);
    }

    public Optional<Message> getOne(Long id){ return  repository.findOne(id);}

    public Iterable<Message> getAll(){return repository.findAll();}



}
