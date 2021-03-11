package socialnetwork.service;

import socialnetwork.domain.*;
import socialnetwork.repository.Repository;

import javax.swing.text.html.Option;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FriendRequestsService {

    private Repository<Tuple<Long, Long>, FriendRequest> repository;

    public FriendRequestsService(Repository<Tuple<Long, Long>, FriendRequest> repository){
        this.repository = repository;
    }

    public Optional<FriendRequest> sendFriendRequest(Long from, Long to) throws Exception {

        Optional<FriendRequest> fromTo = this.repository.findOne(new Tuple<>(from, to));
//        if(fromTo.isPresent() && fromTo.get().getStatus().equals(Status.PENDING))
//            throw new Exception("Friend request already sent!");
//        Optional<FriendRequest> toFrom = this.repository.findOne(new Tuple<>(to, from));
//        if(toFrom.isPresent() && toFrom.get().getStatus().equals(Status.PENDING))
//            throw new Exception("Please answer this request!");
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setId(new Tuple<>(from, to));
        friendRequest.setDate(LocalDate.now());
        return this.repository.save(friendRequest);
    }

    public void answerFriendRequest(Long from, Long to, String repsonse) throws Exception {
        Optional<FriendRequest> thisFriendRequest = repository.findOne(new Tuple<>(from, to));
        if(thisFriendRequest.isPresent() && thisFriendRequest.get().getStatus().equals(Status.APPROVED) || thisFriendRequest.get().getStatus().equals(Status.REJECTED))
            throw new Exception("You already answered this request!");

        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setId(new Tuple<>(from, to));
        if(repsonse.equals("approve"))
            friendRequest.setStatus(Status.APPROVED);
        else if(repsonse.equals("reject"))
            friendRequest.setStatus(Status.REJECTED);
        if(repository.findOne(new Tuple<Long, Long>(from, to)).isPresent())
            repository.update(friendRequest);
        else
            throw new Exception("The friendship request doesn't exist!");

    }

    public List<FriendRequest> getAll(Long id){
        Iterable<FriendRequest> friendRequests = repository.findAll();
        List<FriendRequest> allFriendRequests = new ArrayList<FriendRequest>();
        friendRequests.forEach(allFriendRequests::add);

        return allFriendRequests.stream()
                .filter(x->(x.getId().getRight()==id || x.getId().getLeft()==id))
                .collect(Collectors.toList());
    }

    public void removeFriendship(Long from, Long to) throws Exception {
        repository.delete(new Tuple<>(from, to));
    }

    public Optional<FriendRequest> getOne(Tuple<Long, Long> longLongTuple){
        return repository.findOne(longLongTuple);
    }

}
