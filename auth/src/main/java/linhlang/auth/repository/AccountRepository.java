package linhlang.auth.repository;

import linhlang.commons.persistence.CommonRepository;
import linhlang.auth.model.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.generated.tables.records.AccountRecord;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.*;

import static org.jooq.generated.Tables.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AccountRepository implements CommonRepository<Account, String> {

    private final DSLContext dsl;

    /**
     * write model into db record
     *
     * @param account
     * @return
     */
    private AccountRecord writeDB(Account account) {
        return new AccountRecord(
                account.getId(),
                account.getUsername(),
                account.getPassword(),
                account.getStatus().name(),
                account.getCreateTime()
        );
    }

    /**
     * read db record into model
     *
     * @param results db result
     * @return list model
     */
    private List<Account> readDB(Result<Record> results) {
        Map<String, Account> accountMap = new HashMap<>();
        List<Account> result = new ArrayList<>();

        for (Record record : results) {
            String accountId = record.get(ACCOUNT.ID);
            Account account = accountMap.get(accountId);
            if (account == null) {
                account = new Account(
                        record.get(ACCOUNT.ID),
                        record.get(ACCOUNT.USERNAME),
                        record.get(ACCOUNT.PASSWORD),
                        Account.Status.valueOf(record.get(ACCOUNT.STATUS)),
                        new HashSet<String>(),
                        record.get(ACCOUNT.CREATE_TIME)
                );
                result.add(account);
                accountMap.put(accountId, account);
            }

            account.getRoles().add(record.get(ROLE.CODE));
        }

        return result;
    }

    /**
     * find user by username
     *
     * @param username username
     * @return return account
     */
    public Account findByUsername(String username) {
        Result<Record> records = dsl.select()
                .from(ACCOUNT)
                .leftJoin(ACCOUNT_ROLE).on(ACCOUNT.ID.eq(ACCOUNT_ROLE.ACCOUNT_ID))
                .leftJoin(ROLE).on(ROLE.CODE.eq(ACCOUNT_ROLE.ROLE_CODE))
                .where(ACCOUNT.USERNAME.likeIgnoreCase(username))
                .limit(1)
                .fetch();
        List<Account> accounts = readDB(records);
        return accounts.isEmpty() ? null : accounts.getFirst();
    }

    /**
     * check username existed or not
     *
     * @param username username
     * @return existed or not
     */
    public boolean isExistedUsername(String username) {
        Record1<Integer> record = dsl.select(DSL.count())
                .from(ACCOUNT)
                .where(ACCOUNT.USERNAME.likeIgnoreCase(username))
                .fetchOne();
        if (record == null) {
            return false;
        }
        return record.value1() > 0;
    }

    @Override
    public void save(Account data) {
        log.info("saving account: {}", data);
        List<Query> queries = new LinkedList<>();
        queries.add(
                dsl.insertInto(ACCOUNT).set(writeDB(data))
        );
        data.getRoles().forEach(role -> queries.add(
                dsl.insertInto(ACCOUNT_ROLE)
                        .set(ACCOUNT_ROLE.ID, UUID.randomUUID().toString())
                        .set(ACCOUNT_ROLE.ACCOUNT_ID, data.getId())
                        .set(ACCOUNT_ROLE.ROLE_CODE, role)
        ));

        dsl.batch(queries).execute();
    }

    @Override
    public Account load(String s) {
        Result<Record> results = dsl.select()
                .from(ACCOUNT)
                .leftJoin(ACCOUNT_ROLE).on(ACCOUNT.ID.eq(ACCOUNT_ROLE.ACCOUNT_ID))
                .leftJoin(ROLE).on(ROLE.CODE.eq(ACCOUNT_ROLE.ROLE_CODE))
                .where(ACCOUNT.ID.eq(s))
                .fetch();

        List<Account> accounts = readDB(results);
        return accounts.isEmpty() ? null : accounts.getFirst();
    }

    @Override
    public void delete(String s) {
        dsl.delete(ACCOUNT)
                .where(ACCOUNT.ID.eq(s))
                .execute();
    }
}
