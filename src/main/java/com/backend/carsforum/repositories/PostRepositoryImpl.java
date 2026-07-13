package com.backend.carsforum.repositories;

import com.backend.carsforum.models.Post;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class PostRepositoryImpl implements PostRepository{
    private final SessionFactory sessionFactory;

    public PostRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session session() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Post findById(Long id) {
        if(id == null ) return null;

        return session().find(Post.class, id);
    }

    @Override
    public void save(Post post) {
        session().persist(post);
    }

    @Override
    public void update(Post post) {
        session().merge(post);
    }

    @Override
    public void delete(Post post) {
        session().remove(post);
    }

    @Override
    public List<Post> findByAuthorId(Long authorId) {
        if (authorId == null) return Collections.emptyList();
        String hql = "FROM Post p WHERE p.author.id = :authorId ORDER BY p.createdAt DESC";
        return session().createQuery(hql, Post.class)
                .setParameter("authorId", authorId)
                .getResultList();
    }

    @Override
    public List<Post> findByKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) return Collections.emptyList();
        String hql = "FROM Post p WHERE lower(p.title) LIKE :kw OR lower(p.content) LIKE :kw ORDER BY p.createdAt DESC";
        String like = "%" + keyword.toLowerCase() + "%";
        return session().createQuery(hql, Post.class)
                .setParameter("kw", like)
                .getResultList();
    }

    @Override
    public List<Post> findLatest(int limit) {
        if (limit <= 0) limit = 10;
        String hql = "FROM Post p ORDER BY p.createdAt DESC";
        return session().createQuery(hql, Post.class)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public List<Post> findTopCommented(int limit) {
        if (limit <= 0) limit = 10;
        String hql = "SELECT p FROM Post p LEFT JOIN p.comments c GROUP BY p.id ORDER BY COUNT(c) DESC";
        return session().createQuery(hql, Post.class)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public long countAll() {
        Long count = session().createQuery("SELECT count(p) FROM Post p", Long.class).uniqueResult();
        return count == null ? 0L : count;
    }
}

