package socialnetwork.service;

import socialnetwork.domain.Prietenie;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.Utilizator;
import socialnetwork.repository.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FriendshipsService {
    private Repository<Tuple<Long, Long>, Prietenie> repo;
    private Repository<Long, Utilizator> repoUsers;

    public FriendshipsService(Repository<Tuple<Long, Long>, Prietenie> repo, Repository<Long, Utilizator> repoUsers){
        this.repo = repo;
        this.repoUsers = repoUsers;
    }

    public Optional<Prietenie> addFriendship(Prietenie prietenie) throws Exception {
        if(repo.findOne(new Tuple(prietenie.getId().getRight(), prietenie.getId().getLeft())).isPresent()
         || repo.findOne(new Tuple(prietenie.getId().getLeft(), prietenie.getId().getRight())).isPresent())
            throw new Exception("Prietenie existenta!");
        prietenie.setDate(LocalDateTime.now());
        return repo.save(prietenie);
    }

    public Optional<Prietenie> deletePrietenie(Long id1, Long id2) throws Exception {
        if(repo.findOne(new Tuple<>(id1, id2)).isEmpty() && repo.findOne(new Tuple<>(id2, id1)).isEmpty())
            throw new Exception("Prietenia nu exista!");
        Tuple<Long, Long> id = new Tuple<>(id1, id2);
        return repo.delete(id);

    };

    public Optional<Prietenie> getOne(Tuple<Long, Long> id){ return repo.findOne(id); }

    public Map<Utilizator, LocalDate> friendsOfUser(Utilizator u){
        Iterable<Prietenie> friendships = repo.findAll();
        List<Prietenie> allFriendships = new ArrayList<Prietenie>();
        friendships.forEach(allFriendships::add);
        Map<Utilizator, LocalDate> friends = allFriendships.stream()
                .filter(x->(x.getId().getRight().equals(u.getId()) || x.getId().getLeft().equals(u.getId())))
                .map(x->{if(x.getId().getLeft().equals(u.getId())) return new Tuple<Long, LocalDate>(x.getId().getRight(),x.getDate().toLocalDate());
                else if(x.getId().getRight().equals(u.getId())) return new Tuple<Long, LocalDate>(x.getId().getLeft(), x.getDate().toLocalDate());
                    return null;
                })
                .collect(Collectors.toMap(x->repoUsers.findOne((x.getLeft())).get(), x->x.getRight()));
        return friends;
    }

    public Map<Utilizator, LocalDate> friendsOfUserFromMonth(Utilizator u, int month, int year){
        Iterable<Prietenie> friendships = repo.findAll();
        List<Prietenie> allFriendships = new ArrayList<Prietenie>();
        friendships.forEach(allFriendships::add);
        Map<Utilizator, LocalDate> friends = allFriendships.stream()
                .filter(x->(x.getId().getRight().equals(u.getId()) || x.getId().getLeft().equals(u.getId())))
                .filter(x->(x.getDate().getMonthValue()==month) && (x.getDate().getYear())==year)
                .map(x->{if(x.getId().getLeft().equals(u.getId())) return new Tuple<Long, LocalDate>(x.getId().getRight(),x.getDate().toLocalDate());
                else if(x.getId().getRight().equals(u.getId())) return new Tuple<Long, LocalDate>(x.getId().getLeft(), x.getDate().toLocalDate());
                    return null;
                })
                .collect(Collectors.toMap(x->repoUsers.findOne((x.getLeft())).get(), x->x.getRight()));
        return friends;
    }

    public List<Prietenie> getAll(){
        return StreamSupport.stream(repo.findAll().spliterator(), false).collect(Collectors.toList());
    }
}
