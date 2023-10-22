package hello.start;

import hello.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class EntityManagerFactory {

    public static void main(String[] args) {

        //엔티티 매니저 팩토리는 데이터베이스당 하나만 생성되어 애플리케이션 전체에서 사용된다
        //쓰레드간에 공유가 되는게 아닌 사용하고 버린다
        javax.persistence.EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello"); //persistence.xml 에서 작성한 persistence-unit name
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        //JPA 에서 데이터를 변경하는 모든 작업은 트랜잭션 안에서 이루어져야 한다
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
            //저장
            /**
            Member saveMember = new Member();
            saveMember.setId(2L);
            saveMember.setName("MemberB");
            entityManager.persist(saveMember);
             **/

            //조회
            /**
            Member findMember = entityManager.find(Member.class, 1L);
            System.out.println("member id = " + findMember.getId());
            System.out.println("member name = " + findMember.getName());
             **/

            //삭제
            /**
            Member findMember = entityManager.find(Member.class, 1L);
            entityManager.remove(findMember);
            **/

            //변경
            Member findMember = entityManager.find(Member.class, 2L);
            findMember.setName("MemberC"); //persist() X - 트랜잭션 커밋 직전에 변경을 체크하고 업데이트 쿼리를 날림

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
