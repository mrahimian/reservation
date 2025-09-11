package ir.azki.data.user;

import ir.azki.tables.daos.UsersDao;
import ir.azki.tables.pojos.UsersEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository {
    private final UsersDao usersDao;

    public UserRepository(UsersDao usersDao) {
        this.usersDao = usersDao;
    }

    public Optional<UsersEntity> findByUsername(String username) {
        return usersDao.fetchOptionalByUsername(username);
    }

    public Optional<UsersEntity> findByEmail(String email) {
        return usersDao.fetchOptionalByEmail(email);
    }
}
