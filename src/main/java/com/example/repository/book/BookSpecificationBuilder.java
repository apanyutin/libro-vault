package com.example.repository.book;

import com.example.dto.BookSearchParameters;
import com.example.model.Book;
import com.example.repository.SpecificationBuilder;
import com.example.repository.SpecificationProviderManager;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@AllArgsConstructor
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    public Specification<Book> build(BookSearchParameters searchParameters) {
        Specification<Book> specification = Specification.where(null);
        if (searchParameters.authors() != null && searchParameters.authors().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider("author")
                    .getSpecification(searchParameters.authors()));
        }
//        if (searchParameters.priceRanges() != null && searchParameters.priceRanges().length > 0) {
//            specification = specification.and(bookSpecificationProviderManager
//                    .getSpecificationProvider("price")
//                    .getSpecification(searchParameters.priceRanges()));
//        }
        return specification;
    }
}
