package socialnetwork.repository.database;

import socialnetwork.domain.UserDTO;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.paging.Page;
import socialnetwork.repository.paging.Pageable;
import socialnetwork.repository.paging.Paginator;
import socialnetwork.repository.paging.PagingRepository;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UtilizatorDbPageRepository extends UtilizatorDbRepository implements PagingRepository<Long, Utilizator> {
    public UtilizatorDbPageRepository(String url, String username, String password, Validator<Utilizator> validator) {
        super(url, username, password, validator);
    }

    @Override
    public Page<Utilizator> findAll(Pageable pageable) {
        Paginator<Utilizator> utilizatorPaginator = new Paginator<>(pageable, findAll());
        return utilizatorPaginator.paginate();
    }

    public Page<Utilizator> findAllFiltered(Pageable pageable, String filter){
        Paginator<Utilizator> utilizatorPaginator = new Paginator<>(pageable,
                StreamSupport.stream(findAll().spliterator(), false)
                .filter(utilizator -> utilizator.getUsername().contains(filter))
                .collect(Collectors.toList())
        );
        return utilizatorPaginator.paginate();
    }

    public Page<Utilizator> findAllFriendsOfUser(Pageable pageable, Long id){
        Paginator<Utilizator> utilizatorPaginator = new Paginator<>(pageable,
               StreamSupport.stream(findAll().spliterator(), false)
                .filter(utilizator -> utilizator.getId() == id)
                .findFirst()
                .get()
                .getFriends()
                .stream()
                       .map(utilizator -> findOne(utilizator.getId()).get())
               .collect(Collectors.toList())

        );
        return utilizatorPaginator.paginate();
    }
}
