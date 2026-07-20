package com.backend.forum.repositories;

import com.backend.forum.models.Comment;
import com.backend.forum.models.Post;
import com.backend.forum.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CommentRepositoryImpl implements CommentRepository {
    private final SessionFactory sessionFactory;

    public CommentRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    private Session session(){
        return sessionFactory.getCurrentSession();
    }
    @Override
    public Comment save(Comment comment) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(comment);
        return comment;
    }

    @Override
    public Comment update(Comment comment) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(comment);
        return comment;
    }

    @Override
    public void delete(Comment comment) {
        Session session = sessionFactory.getCurrentSession();
        session.remove(comment);
    }

    @Override
    public Optional<Comment> findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return Optional.ofNullable(session.find(Comment.class, id));
    }

    @Override
    public List<Comment> findAll() {
        Session session = sessionFactory.getCurrentSession();
        String hql = "FROM Comment c";
        return session
                .createQuery(hql, Comment.class)
                .getResultList();
    }

    @Override
    public List<Comment> findByPost(Post post) {
        Session session = sessionFactory.getCurrentSession();

        return session
                .createQuery("FROM Comment c WHERE c.post = :post", Comment.class)
                .setParameter("post", post)
                .getResultList();    }

    @Override
    public List<Comment> findByAuthor(User author) {
        Session session = sessionFactory.getCurrentSession();

        return session
                .createQuery("FROM Comment c WHERE c.author = :author", Comment.class)
                .setParameter("author", author)
                .getResultList();
    }

    @Override
    public List<Comment> findReplies(Long parentCommentId) {

        Session session = sessionFactory.getCurrentSession();

        return session
                .createQuery(
                        "FROM Comment c WHERE c.parentComment.id = :parentCommentId",
                        Comment.class)
                .setParameter("parentCommentId", parentCommentId)
                .getResultList();
    }

    @Override
    public List<Comment> findByPostId(Long postId) {
        String hql = "FROM Comment c WHERE c.post.id = :postId";

        return session()
                .createQuery(hql, Comment.class)
                .setParameter("postId", postId)
                .getResultList();
    }

    @Override
    public List<Comment> findByAuthorId(Long authorId) {
        String hql = "FROM Comment c WHERE c.author.id = :authorId";

        return session()
                .createQuery(hql, Comment.class)
                .setParameter("authorId", authorId)
                .getResultList();
    }
}
