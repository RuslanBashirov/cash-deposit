package org.bashirov.cashdeposit.repository;

import org.bashirov.cashdeposit.entity.Profiles;
import org.bashirov.cashdeposit.service.ProfilesService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ProfilesServiceImpl implements ProfilesService {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void saveProfile(Profiles profile) {
        Session currentSession = sessionFactory.openSession();
        Transaction tx = currentSession.beginTransaction();

        currentSession.save(profile);
        tx.commit();
        currentSession.close();
    }

    @Override
    public void increaseDepositOnPercentInSeparateThread(Profiles profile) {
        long upperLimit = (long) (profile.getCash() * 2.07);

        Thread increaseDepositThread = new Thread(() -> {
            long cash;
            while ((cash = (long) (profile.getCash() * 1.1)) <= upperLimit) {
                try {
                    TimeUnit.SECONDS.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Session currentSession = sessionFactory.openSession();
                Transaction tx = currentSession.beginTransaction();

                profile.setCash(cash);
                currentSession.update(profile);

                tx.commit();
                currentSession.close();
            }
        });

        increaseDepositThread.start();
    }
}
