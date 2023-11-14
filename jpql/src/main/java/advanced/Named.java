package advanced;

import advanced.entity.Member;
import advanced.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class Named {

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
            memberA.setAge(20);
            memberA.setTeam(teamA);
            em.persist(memberA);

            em.flush();
            em.clear();

            /**
             * Named 쿼리
             * - 미리 정의해서 이름을 부여해두고 사용하는 JPQL
             * - 정적 쿼리
             * - 애노테이션, XML 에 정의
             * - 애플리케이션 로딩 시점에 초기화 후 재사용
             * - 애플리케이션 로딩 시점에 쿼리를 검증(막강한 기능)
             */
            List<Member> result3 = em.createNamedQuery("Member.findByName", Member.class)
                                    .setParameter("name", "MemberA")
                                    .getResultList();


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
