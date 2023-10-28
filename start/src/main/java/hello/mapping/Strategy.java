package hello.mapping;

import hello.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Strategy {

    public static void main(String[] args) {

        javax.persistence.EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello"); //persistence.xml 에서 작성한 persistence-unit name
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {

            Member saveMember1 = new Member();
            saveMember1.setName("MemberA");
            System.out.println("-----------------");
            entityManager.persist(saveMember1);
            //IDENTITY 전략은 예외적으로 persist 시점에 즉시 insert 쿼리를 날리고 식별자를 조회
            //SEQUENCE 전략일 경우 시퀸스의 다음 값을 조회해오는 쿼리를 날리고 insert 는 실제 커밋시점에 날린다
            //allocationSize 속성으로 시퀸스 값을 미리 여러개 할당하여 성능을 최적화 할 수 있다
            System.out.println("member id = " + saveMember1.getId());
            System.out.println("-----------------");

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
