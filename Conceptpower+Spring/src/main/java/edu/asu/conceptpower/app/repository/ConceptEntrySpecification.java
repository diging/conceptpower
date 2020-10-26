package edu.asu.conceptpower.app.repository;

import org.springframework.data.jpa.domain.Specification;

import edu.asu.conceptpower.app.model.ConceptEntry;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ConceptEntrySpecification {

    public static Specification<ConceptEntry> customFieldSearch(String fieldName, String fieldQuery) {
      return new Specification<ConceptEntry>() {
        private static final long serialVersionUID = 1L;

        @Override
        public Predicate toPredicate(Root<ConceptEntry> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            return criteriaBuilder.like(criteriaBuilder.lower(root.<String>get(fieldName)), getLikePattern(fieldQuery));
        }
        
        private String getLikePattern(final String searchTerm) {
            StringBuilder pattern = new StringBuilder();
            pattern.append(searchTerm.toLowerCase());
            pattern.append("%");
            return pattern.toString();
        }
        
      };
    }

  }