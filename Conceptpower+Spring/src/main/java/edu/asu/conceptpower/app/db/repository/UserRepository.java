package edu.asu.conceptpower.app.db.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.app.model.User;
@Repository
@Component
@Service
public interface UserRepository extends CrudRepository<User, Integer> {

}
