package ex2.v2;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class V2Main {

    public static void main(String[] args) {

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {

            Team team = new Team();
            team.setName("teamA");
            entityManager.persist(team);

            Member member = new Member();
            member.setName("member1");
            member.setTeam(team);
            entityManager.persist(member);

            entityManager.flush();
            entityManager.clear();

            Member findMember = entityManager.find(Member.class, member.getId());
            //양방향 매핑 - 반대 방향으로 객체 그래프 탐색
            List<Member> members = findMember.getTeam().getMembers();
            for (Member m : members) {
                System.out.println("m = " + m.getName());
            }

            //양방향 매핑시 가장 많이 하는 실수
            Member member2 = new Member();
            member2.setName("member2");
            entityManager.persist(member2);

            Team team2 = new Team();
            team2.setName("teamB");
            //역방향(주인이 아닌 방향)만 연관관계 설정 - member 의 team_id 가 null
            team2.getMembers().add(member2);
            entityManager.persist(team2);

            entityManager.flush();
            entityManager.clear();

            //양방향 매핑시 연관관계의 주인에 값을 입력해야 한다
            //순수한 객체 관계를 고려하면 항상 양쪽다 값을 입력해야 한다
            Team team3 = new Team();
            team3.setName("teamC");
            entityManager.persist(team3);

            Member member3 = new Member();
            member3.setName("member3");

//            team3.getMembers().add(member3); - 연관관계 편의 메서드를 만들어서 사용하자
            //연관관계 주인에 값 설정
            member3.setTeam(team3);
            entityManager.persist(member3);

//            entityManager.flush();
//            entityManager.clear();

            //flush & clear 를 하지 않았다면 1차 캐시에서 가져온다
            Team findTeam3 = entityManager.find(Team.class, team3.getId());
            System.out.println("-----------------");
            List<Member> members3 = findTeam3.getMembers(); //실제 사용하는 시점에 members 를 조회하는 쿼리를 날림 - 지연로딩
            for (Member m : members3) {
                //양쪽다 값을 입력하지 않았더라면 컬렉션엔 아무것도 없기 때문에 출력되지 않는다
                System.out.println("m = " + m.getName());
            }
            System.out.println("-----------------");

            /**
             * 양방향 매핑 정리
             * - 단방향 매핑만으로도 이미 연관관계 매핑은 완료
             * - 양방향은 반대 방향으로 조회 기능이 추가된 것 뿐
             * - JPQL 에서 역방향 탐색할 일이 많음
             * - 단방향 매핑을 하고 양방향 매핑은 필요할 때 추가하면 됨(테이블에 영향x)
             */
            transaction.commit();
        }catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        }finally {
            entityManager.close();
            entityManagerFactory.close();
        }

    }

}
