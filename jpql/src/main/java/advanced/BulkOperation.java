package advanced;

import advanced.entity.Member;
import advanced.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class BulkOperation {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Team teamA = new Team();
            teamA.setName("TeamA");
            em.persist(teamA);

            Member memberA = new Member();
            memberA.setName("MemberA");
            memberA.setAge(10);
            memberA.setTeam(teamA);
            em.persist(memberA);

            Member memberB = new Member();
            memberB.setName("MemberB");
            memberB.setAge(10);
            memberB.setTeam(teamA);
            em.persist(memberB);

//            em.flush();
//            em.clear();

            /**
             * 벌크 연산
             * - 만약 재고가 10개 미만인 모든 상품의 가격을 10% 상승시키려면?
             * - JPA 변경 감지를 사용하기엔 너무 많은 SQL 이 실행된다
             * - 변경된 데이터가 100건이라면 100번의 update 쿼리가 나감
             * - 벌크 연산을 사용하면 쿼리 한방으로 여러 테이블 로우 변경
             */
            String query1 = "update Member m set m.age = 20"; //flush 자동 호출
            long result1 = em.createQuery(query1).executeUpdate(); //영향받은 엔티티 수 반환

            /**
             * 벌크 연산 주의
             * - 벌크 연산은 영속성 컨텍스트를 무시하고 데이터베이스에 직접 쿼리를 날림
             * - 해결책으로 벌크 연산을 먼저 실행, 벌크 연산 수행 후 영속성 컨텍스트 초기화
             */
            System.out.println("memberA age = " + memberA.getAge());
            System.out.println("memberB age = " + memberB.getAge());

            em.clear();

            Member findMemberA = em.find(Member.class, memberA.getId());
            Member findMemberB = em.find(Member.class, memberB.getId());

            System.out.println("memberA age = " + findMemberA.getAge());
            System.out.println("memberB age = " + findMemberB.getAge());

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
