package com.example.repository.impl;

import com.example.exception.DataProcessingException;
import com.example.model.Book;
import com.example.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {
    private final SessionFactory sessionFactory;

    @Override
    public Book save(Book book) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.persist(book);
            transaction.commit();
            return book;
        } catch (Exception exception) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't save into DB the book: " + book, exception);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Book", Book.class).getResultList();
        } catch (Exception exception) {
            throw new DataProcessingException("Can't find all books in DB", exception);
        }

    }

    @Override
    public Book getById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.find(Book.class, id);
        } catch (Exception exception) {
            throw new EntityNotFoundException("Can't get book by id: " + id);
        }
    }
}
