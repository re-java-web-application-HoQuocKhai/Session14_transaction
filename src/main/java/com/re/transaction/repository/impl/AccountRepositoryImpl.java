package com.re.transaction.repository.impl;

import com.re.transaction.entity.Account;
import com.re.transaction.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {
    @Autowired
    private SessionFactory sessionFactory;

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Account findById(Long id) {
        return  getSession().find(Account.class, id);
    }

    @Override
    public void update( Account account) {
        getSession().merge(account);
    }

    @Override
    public List<Account> findAll() {
        return getSession().createQuery("FROM Account", Account.class).getResultList();
    }
}
