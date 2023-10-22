package hello.persistence;

import hello.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Flush {

    public static void main(String[] args) {

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {

            /**
             * 플러시
             * - 영속성 컨텍스트의 변경내용을 데이터베이스에 반영
             * - 변경 감지 & 수정된 엔티티 쓰기 지연 SQL 저장소에 등록
             * - 쓰지 지연 SQL 저장소의 쿼리를 데이터베이스에 전송
             */
            Member member = new Member(20L, "member20");
            entityManager.persist(member);

            entityManager.flush(); //플러시를 호출해도 1차 캐시는 유지 - 영속성 컨텍스트를 비우지 않음
            System.out.println("---------------------");

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
