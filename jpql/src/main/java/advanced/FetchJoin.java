package advanced;

import advanced.entity.Member;
import advanced.entity.Team;
import basic.enums.MemberType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Collection;
import java.util.List;

public class FetchJoin {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Team teamA = new Team();
            teamA.setName("TeamA");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("TeamB");
            em.persist(teamB);

            Member memberA = new Member();
            memberA.setName("MemberA");
            memberA.setAge(20);
            memberA.setTeam(teamA);
            em.persist(memberA);

            Member memberB = new Member();
            memberB.setName("MemberB");
            memberB.setAge(20);
            memberB.setTeam(teamA);
            em.persist(memberB);

            Member memberC = new Member();
            memberC.setName("MemberC");
            memberC.setAge(20);
            memberC.setTeam(teamB);
            em.persist(memberC);

            em.flush();
            em.clear();

            /**
             * 페치 조인
             * - SQL 의 조인 종류 X
             * - JPQL 에서 성능 최적활르 위해 제공하는 기능
             * - 연관된 엔티티나 컬렉션(객체 그래프)을 SQL 한번에 함께 조회하는 기능
             * - join fetch 명령어 사용
             */

            //페치 조인 미사용
            //일반 조인은 연관된 엔티티를 함께 조회하지 않는다
            String query1 = "select m from Member m join m.team";
            List<Member> result1 = em.createQuery(query1, Member.class).getResultList();
            for (Member member : result1) {
                //실제 팀을 호출하는 시점에 쿼리가 나가는 지연 로딩이 적용
                //만약 회원이 1000명이라면 엄청나게 많은 쿼리가 추가로 나감 - N + 1 문제
                System.out.println("member = " + member.getName() + ", team = " + member.getTeam().getName());
            }

            //엔티티 페치 조인 - 회원을 조회하면서 연관된 팀도 함께 조회하는 한방 쿼리
            //실제 SQL 은 팀도 함께 SELECT 된다
            String query2 = "select m from Member m join fetch m.team";
            List<Member> result2 = em.createQuery(query2, Member.class).getResultList();
            for (Member member : result2) {
                //페치 조인으로 함께 조회하기 때문에 지연 로딩 X - 프록시가 아닌 진짜 데이터
                System.out.println("member = " + member.getName() + ", team = " + member.getTeam().getName());
            }

            //컬렉션 페치 조인
            String query3 = "select t From Team t join fetch t.members";
            List<Team> result3 = em.createQuery(query3, Team.class).getResultList();
            for (Team team : result3) {
                //일대다 관계이기 때문에 뻥티기 될 수 있음 -JPA 는 DB 에서 결과가 나온 수만큼 컬렉션 개수를 돌려줘야 함
                System.out.println("team = " + team.getName());
                for (Member member : team.getMembers()) {
                    System.out.println("member = " + member.getName());
                }
            }

            //페치 조인과 DISTINCT
            //SQL 은 100% 완전히 똑같아야 중복이 제거 된다
            //JPA 에서 DISTINCT 가 추가로 애플리케이션에서 중복 제거 시도 - 같은 식별자를 가진 Team 엔티티를 제거
            //하이버네이트6 부터는 DISTINCT 명령어를 사용하지 않아도 애플리케이션에서 자동으로 중복 제거
            String query4 = "select distinct t From Team t join fetch t.members";
            List<Team> result4 = em.createQuery(query4, Team.class).getResultList();
            for (Team team : result4) {
                System.out.println("team = " + team.getName());
                for (Member member : team.getMembers()) {
                    System.out.println("member = " + member.getName());
                }
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
