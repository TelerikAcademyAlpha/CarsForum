package com.backend.carsforum.repositories;

import com.backend.carsforum.models.Tag;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TagRepositoryImpl implements TagRepository {

    private final SessionFactory sessionFactory;

    public TagRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<Tag> findById(Long id) {
        Session session = sessionFactory.getCurrentSession();

        Tag tag = session.find(Tag.class, id);

        return Optional.ofNullable(tag);
    }

    @Override
    public Optional<Tag> findByName(String name) {
        Session session = sessionFactory.getCurrentSession();

        return session.createQuery(
                        "FROM Tag WHERE name = :name", Tag.class)
                .setParameter("name", name.trim().toLowerCase())
                .uniqueResultOptional();
    }

    @Override
    public Tag save(Tag tag) {
        Session session = sessionFactory.getCurrentSession();

        session.persist(tag);

        return tag;
    }

    @Override
    public Tag update(Tag tag) {
        Session session = sessionFactory.getCurrentSession();

        return session.merge(tag);
    }

    @Override
    public List<Tag> findAll() {
        Session session = sessionFactory.getCurrentSession();

        return session.createQuery(
                        "FROM Tag", Tag.class)
                .getResultList();
    }
}
