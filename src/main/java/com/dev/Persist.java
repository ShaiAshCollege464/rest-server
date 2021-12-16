package com.dev;

import com.dev.objects.PostObject;
import com.dev.objects.UserObject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class Persist {
    private final SessionFactory sessionFactory;

    @Autowired
    public Persist (SessionFactory sf) {
        this.sessionFactory = sf;
    }

    public String getTokenByUsernameAndPassword(String username, String password) {
        String token = null;
        Session session = sessionFactory.openSession();
        UserObject userObject = (UserObject) session.createQuery("FROM UserObject WHERE username = :username AND password = :password")
                .setParameter("username", username)
                .setParameter("password", password)
                .uniqueResult();
        session.close();
        if (userObject != null) {
            token = userObject.getToken();
        }
        return token;
    }

    public boolean addAccount (UserObject userObject) {
        boolean success = false;
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(userObject);
        transaction.commit();
        session.close();
        if (userObject.getId() > 0) {
            success = true;
        }
        return success;
    }

    public boolean addPost (String token, String content) {
        boolean success = false;
        Integer userId = getUserIdByToken(token);
        if (userId != null) {
            PostObject postObject = new PostObject();
            UserObject userObject = new UserObject();
            postObject.setUserObject(userObject);
            postObject.setContent(content);
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(postObject);
            transaction.commit();
            session.close();
            if (postObject.getId() > 0) {
                success = true;
            }
        }
        return success;
    }

    public Integer getUserIdByToken (String token) {
        Integer id = null;
        Session session = sessionFactory.openSession();
        UserObject userObject = (UserObject) session.createQuery("FROM UserObject WHERE token = :token")
                .setParameter("token", token)
                .uniqueResult();
        session.close();
        if (userObject != null) {
            id = userObject.getId();
        }
        return id;
    }

    public List<PostObject> getPostsByUser (String token) {
        List<PostObject> postObjects = null;
        Session session = sessionFactory.openSession();
        postObjects = (List<PostObject>)session.createQuery(
                "FROM PostObject " +
                        "WHERE userObject.token = :token " +
                        "ORDER BY id DESC ")
                .setParameter("token", token)
                .list();
        session.close();
        return postObjects;
    }

    public boolean removePost (String token, int postId) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.createQuery("DELETE FROM PostObject WHERE id = :id AND userObject.token = :token")
                .setParameter("id", postId)
                .setParameter("token", token)
                .executeUpdate();
        transaction.commit();
        session.close();
        return true;
    }
}
