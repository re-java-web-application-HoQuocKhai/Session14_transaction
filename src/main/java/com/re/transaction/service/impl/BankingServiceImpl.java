package com.re.transaction.service.impl;

import com.re.transaction.entity.Account;
import com.re.transaction.entity.TransactionLog;
import com.re.transaction.repository.AccountRepository;
import com.re.transaction.repository.TransactionLogRepository;
import com.re.transaction.service.BankingService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BankingServiceImpl implements BankingService {
    private final AccountRepository accountRepository;
    private final TransactionLogRepository transactionLogRepository;
    private final SessionFactory sessionFactory;

    public void transferMoney(Long fromId, Long toId, double amount) {
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();

        try {
            // First-Level Cache test: Query fromId twice
            System.out.println("--- Test First-Level Cache: Query 1 ---");
            Account fromAccount = accountRepository.findById(fromId);
            System.out.println("--- Test First-Level Cache: Query 2 ---");
            Account fromAccountCached = accountRepository.findById(fromId); // Hibernate should not query DB again

            Account toAccount = accountRepository.findById(toId);

            if (fromAccount == null || toAccount == null) {
                throw new RuntimeException("Tài khoản không tồn tại");
            }

            if (fromAccount.getBalance() < amount) {
                throw new RuntimeException("Số dư của bạn không đủ");
            }

            fromAccount.setBalance(fromAccount.getBalance() - amount);
            toAccount.setBalance(toAccount.getBalance() + amount);

            accountRepository.update(fromAccount);
            accountRepository.update(toAccount);

            TransactionLog transactionLog = new TransactionLog();
            transactionLog.setFromAccountId(fromId);
            transactionLog.setToAccountId(toId);
            transactionLog.setAmount(amount);
            transactionLog.setStatus("SUCCESS");
            transactionLog.setCreatedAt(LocalDate.now());

            transactionLogRepository.save(transactionLog);

            // Giả lập lỗi để test rollback nếu chuyển đúng số tiền là 9999
            if (amount == 9999) {
                throw new RuntimeException("Giả lập lỗi hệ thống để test rollback!");
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e; // Ném lại lỗi để Controller xử lý
        }
    }

    public List<Account> getAccounts() {
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        try {
            List<Account> accounts = accountRepository.findAll();
            tx.commit();
            return accounts;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public List<TransactionLog> getTransactions() {
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        try {
            List<TransactionLog> logs = transactionLogRepository.findAll();
            tx.commit();
            return logs;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        }
    }
}
