package ru.clevertec.comment.util;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Component;


/**
 * Component class responsible for indexing data in the database.
 *
 * @author Sergey Leshkevich
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class DatabaseIndexer {

    private final EntityManagerFactory entityManagerFactory;

    /**
     * Method annotated with {@code @PostConstruct} to perform data indexing upon bean initialization.
     *
     * @throws InterruptedException If the indexing process is interrupted.
     */
    @PostConstruct
    private void indexData() throws InterruptedException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        SearchSession searchSession = Search.session(entityManager);
        MassIndexer indexer = searchSession.massIndexer();
        indexer.startAndWait();
    }
}

