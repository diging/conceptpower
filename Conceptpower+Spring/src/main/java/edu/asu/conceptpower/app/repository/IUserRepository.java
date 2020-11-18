package edu.asu.conceptpower.app.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.asu.conceptpower.app.model.User;

/**
 * Token repository
 *  
 * @author Keerthivasan Krishnamurthy
 * 
 */
@Repository
public interface IUserRepository extends PagingAndSortingRepository<User, String>{

    User findByUserAndPw(String name, String pw);
    
    User findByUser(String name);
    
    List<User> findAll();
    
    @Transactional
    void deleteByUser(String name);
    
    List<User> findByEmail(String email);
}
