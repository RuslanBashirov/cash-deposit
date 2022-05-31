package org.bashirov.cashdeposit.repository;

import lombok.extern.slf4j.Slf4j;
import org.bashirov.cashdeposit.entity.Phones;
import org.bashirov.cashdeposit.entity.Users;
import org.bashirov.cashdeposit.service.UsersService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UsersServiceImpl implements UsersService {

    @Value("#{T(java.lang.Integer).parseInt('${max.elements.on.page}')}")
    private int MAX_ELEMENTS_ON_PAGE;

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Users> readAllUsers(int page) {
        Session currentSession = sessionFactory.getCurrentSession();

        Query<Users> theQuery = currentSession.createQuery("from Users", Users.class);
        theQuery.setFirstResult(page);
        theQuery.setMaxResults(MAX_ELEMENTS_ON_PAGE);

        List<Users> users = theQuery.getResultList();
        currentSession.close();
        return users;
    }

    @Override
    public Users readUserById(long userId) {
        Session currentSession = sessionFactory.getCurrentSession();

        Users user = currentSession.get(Users.class, userId);
        currentSession.close();
        return user;
    }

    @Override
    public List<Users> readUsersByName(String name, int page) {
        Session currentSession = sessionFactory.getCurrentSession();

        Query<Users> theQuery = currentSession.createQuery("from Users where name like :name", Users.class);
        theQuery.setParameter("name", '%'+name+'%');
        theQuery.setFirstResult(page);
        theQuery.setMaxResults(MAX_ELEMENTS_ON_PAGE);

        List<Users> users = theQuery.getResultList();
        currentSession.close();
        return users;
    }

    @Override
    public List<Users> readUsersByAgeBetween(int fromAge, int toAge, int page) {
        Session currentSession = sessionFactory.getCurrentSession();

        Query<Users> theQuery = currentSession.createQuery("from Users where age >= :fromAge and age <= :toAge", Users.class);
        theQuery.setParameter("fromAge", fromAge);
        theQuery.setParameter("toAge", toAge);
        theQuery.setFirstResult(page);
        theQuery.setMaxResults(MAX_ELEMENTS_ON_PAGE);

        List<Users> users = theQuery.getResultList();
        currentSession.close();
        return users;
    }

    @Override
    public Users readUsersByEmail(String email) {
        log.debug("Attempting to find Users for email = {}", email);
        Session currentSession = sessionFactory.getCurrentSession();

        Query<Users> theQuery = currentSession.createQuery("from Users where email = :email", Users.class);
        theQuery.setParameter("email", email);

        Users user = theQuery.getSingleResult();
        log.debug("Got Users for email = {}", user);
        currentSession.close();
        return user;
    }

    @Override
    public List<Users> readUsersByPhoneValue(String phoneValue, int page) {
        Session currentSession = sessionFactory.getCurrentSession();

        Query<Users> theQuery = currentSession.createQuery("select u from Users as u inner join u.phones as ph " +
                "where ph.value = :phoneValue", Users.class);
        theQuery.setParameter("phoneValue", phoneValue);
        theQuery.setFirstResult(page);
        theQuery.setMaxResults(MAX_ELEMENTS_ON_PAGE);

        List<Users> users = theQuery.getResultList();
        currentSession.close();
        return users;
    }

    @Override
    public void saveUser(Users user) {
        Session currentSession = sessionFactory.getCurrentSession();
        Transaction tx = currentSession.beginTransaction();

        currentSession.save(user);
        tx.commit();
        currentSession.close();
    }

    @Override
    public void savePhone(Phones phone) {
        Session currentSession = sessionFactory.getCurrentSession();
        Transaction tx = currentSession.beginTransaction();

        currentSession.save(phone);
        tx.commit();
        currentSession.close();
    }

    @Override
    public void update(long userId, Users updatedUser) {
        Session currentSession = sessionFactory.getCurrentSession();
        Transaction tx = currentSession.beginTransaction();

        Users user = currentSession.get(Users.class, userId);
        user.setName(updatedUser.getName());
        user.setAge(updatedUser.getAge());
        user.setEmail(updatedUser.getEmail());

        currentSession.update("users", user);
        tx.commit();
        currentSession.close();
    }

    @Override
    public void delete(long userId) {
        Session currentSession = sessionFactory.getCurrentSession();
        Transaction tx = currentSession.beginTransaction();

        Users user = currentSession.get(Users.class, userId);
        currentSession.remove(user);
        tx.commit();
        currentSession.close();
    }
}
