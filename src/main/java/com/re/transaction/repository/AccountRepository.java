package com.re.transaction.repository;

import com.re.transaction.entity.Account;
import org.hibernate.Session;

import java.util.List;

public interface AccountRepository {
    Account findById(Long id);
    void update(Account account);
    List<Account> findAll();
}
