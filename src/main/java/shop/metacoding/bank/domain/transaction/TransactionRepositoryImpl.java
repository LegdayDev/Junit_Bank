package shop.metacoding.bank.domain.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

interface Dao{
    List<Transaction> findTransactionList(@Param("accountId") Long accountId,
                                          @Param("gubun") String gubun,
                                          @Param("page") Integer page);
}

@RequiredArgsConstructor
public class TransactionRepositoryImpl implements Dao{
    private final EntityManager em;

    @Override
    public List<Transaction> findTransactionList(Long accountId, String gubun, Integer page) {
        // 동적쿼리 생성(gubun 값을 가지고 동적쿼리 생성 : DEPOSIT, WITHDRAW, ALL)
        // JPQL 문법 사용
        String sql = "";

        sql += "select t from Transaction t ";

        if(gubun.equals("WITHDRAW")){ // 출금
            sql += "join fetch t.withdrawAccount wa ";
            sql += "where t.withdrawAccount.id = :withdrawAccountId";
        }else if(gubun.equals("DEPOSIT")){
            sql += "join fetch t.depositAccount da ";
            sql += "where t.depositAccount.id = :depositAccountId";
        }else{
            sql += "left join fetch t.withdrawAccount wa ";
            sql += "left join fetch t.depositAccount da ";
            sql += "where t.withdrawAccount.id = :withdrawAccountId ";
            sql += "or ";
            sql += "t.depositAccount.id = :depositAccountId";
        }

        TypedQuery<Transaction> query = em.createQuery(sql, Transaction.class);

        if(gubun.equals("WITHDRAW")){
            query = query.setParameter("withdrawAccountId",accountId);
        }else if(gubun.equals("DEPOSIT")){
            query = query.setParameter("depositAccountId", accountId);
        }else{
            query = query.setParameter("withdrawAccount", accountId);
            query = query.setParameter("depositAccount", accountId);
        }

        query.setFirstResult(page*5);
        query.setMaxResults(5);

        return query.getResultList();
    }
}
