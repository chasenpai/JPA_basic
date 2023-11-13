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

            /**
             * 페치 조인의 한계
             * - 페치 조인 대상에는 별칭을 줄 수 없다(하이버네이트는 가능하지만 가급적 사용 X)
             * - 둘 이상의 컬렉션은 페치 조인 할 수 없다(1대 다대다 > 곱하기 곱하기.. 예상하지 못한 데이터 뻥튀기 되어버림)
             * - 컬렉션을 페치 조인하면 페이징 API 를 사용할 수 없다
             * - 일대일, 다대일 같은 단일 값 연관 필드들은 페치 조인해도 페이징 가능
             * - 하이버네이트는 경고 로그를 남기고 메모리에서 페이징(매우 위험)
             */
            //페치 조인은 별칭을 주지 않는게 관례
            //만약 멤버가 5명인데 3명만 불러온다? 그리고 3명만 따로 조작 -> 예기치 못한 동작 발생 가능(CASCADE 등)
            //JPA 가 의도한 동작이 설계가 아니다 - 객체 그래프 탐색은 모든 멤버가 다 나온다는 걸 가정하고 설계되어 있음
            String query5 = "select t from Team t join fetch t.members m";
            List<Team> result5 = em.createQuery(query5, Team.class).getResultList();

            //컬렉션을 페치 조인 시 페이징 쿼리를 확인할 수 없다
            //WARN: HHH000104: firstResult/maxResults specified with collection fetch; applying in memory!
            String query6 = "select t from Team t join fetch t.members";
            List<Team> result6 = em.createQuery(query6, Team.class)
                    .setFirstResult(0)
                    .setMaxResults(1)
                    .getResultList();

            em.flush();
            em.clear();

            //컬렉션 페이징 사용 시 페치 조인을 사용하지 않고 @BatchSize 활용 - 지연 로딩으로 인한 N + 1 해결
            String query7 = "select t From Team t";
            List<Team> result7 = em.createQuery(query7, Team.class)
                    .setFirstResult(0)
                    .setMaxResults(2)
                    .getResultList();
            for (Team team : result7) {
                System.out.println("team = " + team.getName());
                for (Member member : team.getMembers()) {
                    System.out.println("member = " + member.getName());
                }
            }

            /**
             * 페치 조인의 특징과 실무 활용
             * - 연관된 엔티티들을 SQL 한번으로 조회 - 성능 최적화
             * - 엔티티에 직접 적용하는 글로벌 로딩 전략보다 우선 처리
             * - 실무에서 글로벌 로딩 전략은 모두 지연 로딩
             * - 최적화가 필요한 곳은 페치 조인 적용
             * - 하지만 모든 것을 페치 조인으로 해결할 순 없음
             * - 페치 조인은 객체 그래프를 유지할 때 사용하면 효과적
             * - 여러 테이블을 조인해서 엔티티가 가진 모양이 아닌 전혀 다른 결과가 필요하다면
             * - 페치 조인보다 일반 조인을 사용하고 필요한 데이터들만 조회해서 DTO 반환이 효과적임
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
