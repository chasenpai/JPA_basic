package hello.persistence;

import hello.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Detached {

    public static void main(String[] args) {

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {

            //영속 상태
            Member findMember = entityManager.find(Member.class, 20L);
            findMember.setName("MemberABC");

            //준영속 상태 - 영속성 컨텍스트에서 관리되지 않기 때문에 변경 감지가 작동되지 않는다
            entityManager.detach(findMember); //특정 엔티티만 영속성에서 분리
//            entityManager.clear(); //영속성 컨텍스트를 완전히 초기화
//            entityManager.close(); //영속성 컨텍스트를 종료

            Member findMember2 = entityManager.find(Member.class, 20L); //다시 select 쿼리가 나감

            transaction.commit(); //커밋 시점에 update 쿼리가 날라가지 않음
        }catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        }finally {
            entityManager.close();
            entityManagerFactory.close();
        }
    }

}
