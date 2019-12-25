package ir.mctab.hibernatearticle.features.usermanagement.impl;

import ir.mctab.hibernatearticle.config.hibernate.HibernateUtil;
import ir.mctab.hibernatearticle.core.share.AuthenticationService;
import ir.mctab.hibernatearticle.entities.User;
import ir.mctab.hibernatearticle.features.usermanagement.usecases.LoginUseCase;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import java.util.List;

public class LoginUseCaseImpl implements LoginUseCase {
    public User login(String username, String password) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory(); // get session
        Session session = sessionFactory.openSession();
        session.beginTransaction(); // transaction start
        Query query = session.createQuery("from User where username = ?1 and password = ?2 ");
        query.setParameter(1, username);
        query.setParameter(2, password);
        List<User> userList = query.list();
        session.close();
        if (userList.size() != 0) {
            User user = userList.get(0);
            AuthenticationService.getInstance().setLoginUser(user);
            return user;
        }
        return null;
    }
}
