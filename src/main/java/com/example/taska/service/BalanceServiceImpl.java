package com.example.taska.service;

import com.example.taska.exception.AccountNotFoundException;
import com.example.taska.model.Account;
import com.example.taska.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class BalanceServiceImpl implements BalanceService, InitializingBean {
    private final AccountRepository repository;
    private static final AtomicInteger readCount = new AtomicInteger(0);
    private static final AtomicInteger writeCount = new AtomicInteger(0);
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public BalanceServiceImpl(AccountRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Optional<Long> getBalance(Long id) {
        if(lock.isWriteLocked()) {
            System.out.println("Write Lock Present.");
        }
        lock.readLock().lock();
        try {
            return repository.findAccountById(id).map(Account::getBalance);
        } finally {
            readCount.incrementAndGet();
            lock.readLock().unlock();
        }
    }

    @Override
    @Transactional
    //@CacheEvict(value="accounts")
    public void changeBalance(Long id, Long amount) {
        lock.writeLock().lock();
        try {
            repository.findAccountById(id).ifPresentOrElse(t -> {t.setBalance(t.getBalance() + amount); repository.save(t);}, () -> {throw new AccountNotFoundException(id);});
        } finally {
            writeCount.incrementAndGet();
            lock.writeLock().unlock();
        }
    }

    @Transactional
    public List<Account> findAll() {
        if(lock.isWriteLocked()) {
            logger.info("Write lock is present");
        }
        lock.readLock().lock();
        try {
            return repository.findAll();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void afterPropertiesSet() {
        new Thread(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            while (true) {
                logger.info("Read count = " + readCount);
                logger.info("Write count = " + writeCount);
                logger.info("Request count = " + readCount.addAndGet(writeCount.intValue()));
                readCount.set(0);
                writeCount.set(0);
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}
