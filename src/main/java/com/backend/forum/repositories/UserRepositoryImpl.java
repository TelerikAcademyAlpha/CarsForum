package com.backend.forum.repositories;

import com.backend.forum.models.User;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl  implements UserRepository{

    private final SessionFactory sessionFactory;

    public UserRepositoryImpl (SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    private Session session(){
        return sessionFactory.getCurrentSession();
    }

    @Override
    public User findById(Long id) {
        return session().find(User.class, id);
    }

    @Override
    public User findByUsername(String username) {
        if (username == null) return null;
        String hql = "FROM User u WHERE lower(u.username) = :uName";
        return session()
                .createQuery(hql, User.class)
                .setParameter("uName", username.toLowerCase())
                .setMaxResults(1)
                .uniqueResult();
    }

    @Override
    public User findByEmail(String email) {
        if (email == null) return null;
        String hql = "FROM User u WHERE lower(u.email) = :eMail";
        return session()
                .createQuery(hql, User.class)
                .setParameter("eMail", email.toLowerCase())
                .setMaxResults(1)
                .uniqueResult();
    }

    @Override
    public void save(User user) {
        session().persist(user);
    }

    @Override
    public void update(User user) {
        session().merge(user);
    }

    @Override
    public void delete(User user) {
        session().remove(user);
    }

    @Override
    public boolean existsByUsername(String username) {
        if (username == null) return false;
        String hql = "SELECT count(u) FROM User u WHERE lower(u.username) = :uName";
        Long count = session().createQuery(hql, Long.class)
                .setParameter("uName", username.toLowerCase())
                .uniqueResult();
        return count != null && count > 0;
    }

    @Override
    public boolean existsByEmail(String email) {
        if (email == null) return false;
        String hql = "SELECT count(u) FROM User u WHERE lower(u.email) = :eMail";
        Long count = session().createQuery(hql, Long.class)
                .setParameter("eMail", email.toLowerCase())
                .uniqueResult();
        return count != null && count > 0;
    }

    @Override
    public long countAll() {
        Long count = session().createQuery("SELECT COUNT(u) FROM User u", Long.class).uniqueResult();
        return count == null ? 0 : count;
    }

    @Override
    @Transactional
    public List<User> findAll() {
        Session session = sessionFactory.getCurrentSession();
        String hql = "FROM User";
        return session.createQuery(hql,User.class).getResultList();
    }


}
