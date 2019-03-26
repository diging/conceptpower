package edu.asu.conceptpower.app.db.repository;

import org.springframework.data.repository.CrudRepository;

import edu.asu.conceptpower.app.model.User;

public interface UserRepository extends CrudRepository<User, Integer> {

}
