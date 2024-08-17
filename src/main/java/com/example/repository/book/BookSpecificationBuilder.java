package com.example.repository.book;

import com.example.dto.BookSearchParameters;
import com.example.model.Book;
import com.example.repository.SpecificationBuilder;
import com.example.repository.SpecificationProviderManager;
import com.example.repository.book.spec.AuthorSpecificationProvider;
import com.example.repository.book.spec.TitleSpecificationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    public Specification<Book> build(BookSearchParameters searchParameters) {
        Specification<Book> specification = Specification.where(null);
        if (searchParameters.authors() != null && searchParameters.authors().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider(AuthorSpecificationProvider.AUTHOR)
                    .getSpecification(searchParameters.authors()));
        }
        if (searchParameters.wordsFromTitle() != null
                && searchParameters.wordsFromTitle().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider(TitleSpecificationProvider.TITLE)
                    .getSpecification(searchParameters.wordsFromTitle()));
        }
        return specification;
    }
}
