package basic;

import basic.entity.Member;
import basic.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class Join {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Team teamA = new Team();
            teamA.setName("A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("B");
            em.persist(teamB);

            Member memberA = new Member();
            memberA.setName("A");
            memberA.setAge(20);
            memberA.setTeam(teamA);
            em.persist(memberA);

            Member memberB = new Member();
            memberB.setName("C");
            memberB.setAge(20);
            memberB.setTeam(teamB);
            em.persist(memberB);

            em.flush();
            em.clear();

            //내부 조인 - inner 생략 가능
            String query1 = "select m from Member m inner join m.team t";
            List<Member> result1 = em.createQuery(query1, Member.class).getResultList();

            //외부 조인 - outer 생략 가능
            String query2 = "select m from Member m left outer join m.team t";
            List<Member> result2 = em.createQuery(query2, Member.class).getResultList();

            //세타 조인
            String query3 = "select m from Member m, Team t where m.name = t.name";
            List<Member> result3 = em.createQuery(query3, Member.class).getResultList();

            //ON 절 - 조인 대상 필터링
            String query4 = "select m from Member m left join m.team t on t.name = 'A'";
            List<Member> result4 = em.createQuery(query4, Member.class).getResultList();
            for (Member member : result4) {
                //엥?
                //쿼리 상으로 모든 Member 를 가져오고 Member 와 Team 의 join 은 A 팀으로 제한하지만
                //Member 엔티티는 이미 Team 엔티티를 참조하고 있기 때문에 A Team 이 아닌 Member 의 팀 이름도 출력된다
                //객체 지향 프로그래밍과 SQL 간의 차이점이라 볼 수 있다?
                System.out.println(member.getTeam().getName());
            }

            //ON 절 - 연관관계가 없는 엔티티 외부 조인
            String query5 = "select m from Member m join Team t on m.name = t.name";
            List<Member> result5 = em.createQuery(query5, Member.class).getResultList();
            for (Member member : result5) {
                System.out.println(member);
            }

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
