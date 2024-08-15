package com.example.repository.book.spec;

import com.example.model.Book;
import com.example.repository.SpecificationProvider;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return "title";
    }

    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> {
            Predicate[] predicates = new Predicate[params.length];
            for (int i = 0; i < params.length; i++) {
                predicates[i] = criteriaBuilder.like(root.get("title"), "%" + params[i] + "%");
            }
            return criteriaBuilder.and(predicates);
        };
    }
}
