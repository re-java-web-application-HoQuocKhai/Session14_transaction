package com.re.transaction.repository.impl;

import com.re.transaction.entity.Account;
import com.re.transaction.entity.TransactionLog;
import com.re.transaction.repository.TransactionLogRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TransactionLogRepositoryImpl implements TransactionLogRepository {
    @Autowired
    private SessionFactory sessionFactory;

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public TransactionLog findById(Long id) {
        return  getSession().find(TransactionLog.class, id);
    }

    @Override
    public void save(TransactionLog transactionLog) {
        getSession().persist(transactionLog);
    }

    @Override
    public void update( TransactionLog transactionLog) {
        getSession().merge(transactionLog);
    }

    @Override
    public List<TransactionLog> findAll() {
        return getSession().createQuery("FROM TransactionLog", TransactionLog.class).getResultList();
    }
}
