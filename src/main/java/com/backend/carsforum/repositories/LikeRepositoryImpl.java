package com.backend.carsforum.repositories;

import com.backend.carsforum.models.Like;
import com.backend.carsforum.models.Post;
import com.backend.carsforum.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class LikeRepositoryImpl implements LikeRepository {

    private final SessionFactory sessionFactory;

    public LikeRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Like save(Like like) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(like);
        return like;
    }

    @Override
    public void delete(Like like) {
        Session session = sessionFactory.getCurrentSession();
        session.remove(like);
    }

    @Override
    public Optional<Like> findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Like like = session.find(Like.class, id);
        return Optional.ofNullable(like);
    }

    @Override
    public Optional<Like> findByUserAndPost(User user, Post post) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "FROM Like l WHERE l.user = :user AND l.post = :post";
        Like like = session.createQuery(hql, Like.class)
                .setParameter("user", user)
                .setParameter("post", post)
                .uniqueResult();
        return Optional.ofNullable(like);
    }

    @Override
    public boolean existsByUserAndPost(User user, Post post) {
        return findByUserAndPost(user,post).isPresent();
    }

    @Override
    public long countByPost(Post post) {
        Session session = sessionFactory.getCurrentSession();

        String hql = """
            SELECT COUNT(l)
            FROM Like l
            WHERE l.post = :post
            """;

        return session.createQuery(hql, Long.class)
                .setParameter("post", post)
                .getSingleResult();
    }
}
