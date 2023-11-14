package advanced;

import advanced.entity.Member;
import advanced.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class EntityDirectly {

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

            //엔티티의 아이디를  사용
            String query1 = "select count(m.id) from Member m";
            long result1 = em.createQuery(query1, Long.class).getSingleResult();
            //엔티티를 직접 사용 - JPQL 에서 엔티티를 직접 사용하면 SQL 에서 해당 엔티티의 기본키 값을 사용
            //둘다 같은 쿼리가 실행됨
            String query2 = "select count(m) from Member m";
            long result2 = em.createQuery(query2, Long.class).getSingleResult();

            //엔티티 직접 사용 - 기본키 값
            //엔티티를 파라미터로 전달
            String query3 = "select m from Member m where m = :member";
            List<Member> result3 = em.createQuery(query3, Member.class)
                                    .setParameter("member", memberA)
                                    .getResultList();
            //식별자를 직접 전달
            String query4 = "select m from Member m where m.id = :memberId";
            List<Member> result4 = em.createQuery(query4, Member.class)
                    .setParameter("memberId", memberA.getId())
                    .getResultList();


            //엔티티 직접 사용 - 외래키 값
            //엔티티를 파라미터로 전달
            String query5 = "select m from Member m where m.team = :team";
            List<Member> result5 = em.createQuery(query5, Member.class)
                                    .setParameter("team", teamA)
                                    .getResultList();
            //식별자를 직접 전달
            String query6 = "select m from Member m where m.team.id = :teamId";
            List<Member> result6 = em.createQuery(query6, Member.class)
                    .setParameter("teamId", teamA.getId())
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
