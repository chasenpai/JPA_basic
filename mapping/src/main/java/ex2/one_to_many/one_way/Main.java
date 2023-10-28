package ex2.one_to_many.one_way;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Main {

    public static void main(String[] args) {

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        //일대다 단방향
        try {

            Member member = new Member();
            member.setName("member1");
            entityManager.persist(member);

            Team team = new Team();
            team.setName("teamA");
            //member 의 team_id 를 업데이트 하는 쿼리가 추가로 날라간다
            team.getMembers().add(member);
            entityManager.persist(team);

            /**
             * 일대다 단방향 매핑의 단점
             * - 엔티티가 관리하는 외래키가 다른 테이블에 있다는 것 자체가 단점
             * - 연관관계 관리를 위해 추가로 업데이트 쿼리를 실행해야 함
             * - 그냥 다대일 양방향 매핑을 사용하자
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
