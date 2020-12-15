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
public interface  ITokenRepository extends PagingAndSortingRepository<Token, String>{
    
    Token findByToken(String token); 
    
    @Transactional
    Token deleteByToken(String token);
}
