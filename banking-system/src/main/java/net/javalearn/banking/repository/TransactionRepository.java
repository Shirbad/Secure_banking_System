package net.javalearn.banking.repository;

import net.javalearn.banking.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findBySourceAccountIdOrTargetAccountId(Long sourceAccountId, Long targetAccountId);

    List<Transaction> findByFraudFlaggedTrue();

    List<Transaction> findByFraudFlaggedTrueAndSourceAccountIdOrFraudFlaggedTrueAndTargetAccountId(
            Long sourceAccountId,
            Long targetAccountId
    );

    long countByTimestampAfterAndSourceAccountIdOrTimestampAfterAndTargetAccountId(
            LocalDateTime sourceTimestamp,
            Long sourceAccountId,
            LocalDateTime targetTimestamp,
            Long targetAccountId
    );
}
