package ex;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            /**
             * JPQL
             * - 테이블이 아닌 객체를 대상으로 검색하는 객체 지향 쿼리
             * - SQL 을 추상화해서 특정 데이터베이스 SQL 에 의존 X
             * - 한마디로 정의하면 객체 지향 SQL
             * - 하지만 동적쿼리를 작성하기 힘들다
             */
            String query1 = "select m from Member m where m.name like '%hello%'";
            List<Member> result1 = em.createQuery(query1, Member.class).getResultList();

            /**
             * Criteria
             * - 문자가 아닌 자바코드로 JPQL 을 작성할 수 있다
             * - JPQL 빌더 역할
             * - JPA 공식 기능
             * - 하지만 너무 복잡하고 실용성이 없다 > queryDsl 사용 권장
             */
            //Criteria 사용 준비
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Member> query2 = cb.createQuery(Member.class);
            //루트 클래스 지정
            Root<Member> m = query2.from(Member.class);
            //쿼리 생성
            CriteriaQuery<Member> cq = query2.select(m).where(cb.equal(m.get("name"), "hello"));
            List<Member> result2 = em.createQuery(cq).getResultList();

            /**
             * 네이티브 쿼리
             * - JPA 가 제공하는 SQL 을 직접 사용하는 기능
             * - JPQL 로 해결할 수 없는 특정 데이터베이스에 의존적인 기능
             */
            String query3 = "SELECT * FROM MEMBER WHERE USERNAME = 'hello'";
            List<Member> result3 = em.createNativeQuery(query3, Member.class).getResultList();

            tx.commit();
        }catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        }finally {
            em.close();
            emf.close();
        }

    }

}
