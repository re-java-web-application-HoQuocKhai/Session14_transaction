package com.re.transaction.repository;

import com.re.transaction.entity.Account;
import com.re.transaction.entity.TransactionLog;

import java.util.List;

public interface TransactionLogRepository {
    TransactionLog findById(Long id);
    void save(TransactionLog transactionLog);
    void update(TransactionLog transactionLog);
    List<TransactionLog> findAll();
}
