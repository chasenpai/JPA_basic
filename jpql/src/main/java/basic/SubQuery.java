package basic;

import basic.entity.Member;
import basic.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class SubQuery {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Team teamA = new Team();
            teamA.setName("A");
            em.persist(teamA);

            Member memberA = new Member();
            memberA.setName("A");
            memberA.setAge(20);
            memberA.setTeam(teamA);
            em.persist(memberA);

            em.flush();
            em.clear();

            /**
             * 서브 쿼리 지원 함수
             * - [NOT] EXISTS(sub) : 서브 쿼리에 결과가 존재하면 참
             * - ALL(sub) : 모두 만족하면 참
             * - ANY|SOME(sub) : 조건을 하나라도 만족하면 참
             * - [NOT] IN(sub) : 서브 쿼리의 결과 중 하나라도 같은 것이 있으면 참
             */

            //서브 쿼리 예제
            //팀 A 소속인 회원
            String query1 = "select m from Member m where exists(select t from m.team t where t.name = 'A')";
            List<Member> result1 = em.createQuery(query1, Member.class).getResultList();

            //나이가 평균보다 많은 회원
            String query2 = "select m from Member m where m.age > (select avg(m2.age) from Member m2)";
            List<Member> result2 = em.createQuery(query2, Member.class).getResultList();

            //어떤 팀이든 팀에 소속된 회원
            String query3 = "select m from Member m where m.team = ANY(select t from Team t)";
            List<Member> result3 = em.createQuery(query3, Member.class).getResultList();

            /**
             * JPA 서브 쿼리의 한계
             * - WHERE, HAVING 절에서만 서브 쿼리 사용 가능
             * - SELECT 절도 가능(하이버네이터에서 지원)
             * - 하이버네이터6 부터는 FROM 절 서브 쿼리도 지원
             * - 조인으로 풀 수 있으면 풀어서 해결하는 것이 좋음
             */

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
