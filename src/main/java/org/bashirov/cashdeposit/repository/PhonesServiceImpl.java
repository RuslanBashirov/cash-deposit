package org.bashirov.cashdeposit.repository;

import org.bashirov.cashdeposit.entity.Phones;
import org.bashirov.cashdeposit.service.PhonesService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhonesServiceImpl implements PhonesService {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void savePhones(Phones phone) {
        Session currentSession = sessionFactory.openSession();
        Transaction tx = currentSession.beginTransaction();

        currentSession.save(phone);
        tx.commit();
        currentSession.close();
    }
}
