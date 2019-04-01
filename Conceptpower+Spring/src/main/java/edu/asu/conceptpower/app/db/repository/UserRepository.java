package edu.asu.conceptpower.app.db.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import edu.asu.conceptpower.app.model.User;
@Repository

public interface UserRepository extends CrudRepository<User, Integer> {

}
