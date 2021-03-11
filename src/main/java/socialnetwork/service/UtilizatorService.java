package socialnetwork.service;

import socialnetwork.domain.UserDTO;
import socialnetwork.domain.Utilizator;
import socialnetwork.repository.Repository;
import socialnetwork.repository.database.UtilizatorDbPageRepository;
import socialnetwork.repository.database.UtilizatorDbRepository;
import socialnetwork.repository.paging.Page;
import socialnetwork.repository.paging.Pageable;
import socialnetwork.repository.paging.PageableImplementation;
import socialnetwork.repository.paging.Paginator;
import sun.nio.ch.Util;


import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UtilizatorService {
    //private Repository<Long, Utilizator> repo;
    UtilizatorDbPageRepository repo;

    public UtilizatorService(UtilizatorDbPageRepository repo) {
        this.repo = repo;
    }

    public Page<Utilizator> getPagedUsers(int pageNumber) {
        Pageable pageable = new PageableImplementation(pageNumber,5);
        return  repo.findAll(pageable);
    }

    public Page<Utilizator> getPagedAndFilteredUsers(int pageNumber, String filter){
        Pageable pageable = new PageableImplementation(pageNumber, 5);
        return repo.findAllFiltered(pageable, filter);
    }

    public Page<Utilizator> getPagedFriendsOfUser(int pageNumber, Long id){
        Pageable pageable = new PageableImplementation(pageNumber, 5);
        return repo.findAllFriendsOfUser(pageable, id);
    }
    public Optional<Utilizator> addUtilizator(Utilizator utilizator) throws Exception {
        Optional<Utilizator> o = repo.save(utilizator);
        if(o.isPresent())
            throw new Exception("Utilizator existent! ID-ul trebuie sa fie unic");
        return o;
    }

    public Optional<Utilizator> deleteUtilizator(Long id) throws Exception {
        Optional<Utilizator> o = repo.delete(id);
        if(o.isEmpty())
            throw new Exception("Utilizatorul nu exista!");
        return o;};

    public Optional<Utilizator> getOne(Long id){ return repo.findOne(id); }

    public Iterable<Utilizator> getAll(){
        return repo.findAll();
    }

    public byte[] longToBytes(Long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }

    public Long createSalt(){
        byte[] bytes = new byte[10];
        SecureRandom random = new SecureRandom();
        Long salt = random.nextLong();
        return salt;
    }
    public String generateHash(String pass, String alg, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(alg);
        digest.reset();
        digest.update(salt);
        byte[] hash = digest.digest(pass.getBytes());
        StringBuilder parola = new StringBuilder();
        for (byte b : hash) {
            parola.append(String.format("%02x", b));
        }
        return parola.toString();
    }

    /*public List<Utilizator> filterUsersName(String s) {
        Iterable<Utilizator> students = repo.findAll();

        List<Utilizator> filteredUsers = StreamSupport.stream(students.spliterator(), false)
                .filter(user -> user.getFirstName().contains(s)).collect(Collectors.toList());
//        Set<Utilizator> filteredUsers1= new HashSet<>();
//        students.forEach(filteredUsers1::add);
//        filteredUsers1.removeIf(student -> !student.getFirstName().contains(s));

        return filteredUsers;
    }*/
     /*public List<Utilizator> getAllUsers() {
        Iterable<Utilizator> students = repo.findAll();
        return StreamSupport.stream(students.spliterator(), false).collect(Collectors.toList());
    }*/
}
