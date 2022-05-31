package org.bashirov.cashdeposit.service;

import org.bashirov.cashdeposit.entity.Profiles;
import org.bashirov.cashdeposit.entity.Users;
import org.hibernate.Session;

public interface ProfilesService {
    void increaseDepositOnPercentInSeparateThread(Profiles profile);
    void saveProfile(Profiles profile);
}
