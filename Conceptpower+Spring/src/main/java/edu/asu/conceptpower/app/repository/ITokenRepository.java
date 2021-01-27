package edu.asu.conceptpower.app.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.asu.conceptpower.app.model.Token;

/**
 * Token repository
 *  
 * @author Keerthivasan Krishnamurthy
 * 
 */
@Repository
@Transactional
public interface  ITokenRepository extends PagingAndSortingRepository<Token, String>{
    
    Token findByToken(String token); 
    
    Token deleteByToken(String token);
}
