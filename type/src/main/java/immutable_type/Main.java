package immutable_type;

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

        try {

            Address address = new Address("city1", "street1", "1000");

            /**
             * 불변 객체
             * - 객체 타입은 수정할 수 없게 만들면 부작용을 원천 차단할 수 있따
             * - 값 타입은 불변 객체로 설계해야 한다
             * - 불변 객체는 생성 시점 이후 절대 값을 변경할 수 없는 객체이다
             * - 생성자로만 값을 설정하고 setter 를 만들지 않거나 private 접근자로 만든다
             */
            Member member1 = new Member();
            member1.setName("memberA");
            member1.setHomeAddress(address);
            entityManager.persist(member1);

            //setter 로 값을 변경하는 것 자체가 불가능 - 불변 객체로 만들어 부작용을 방지
//            member1.getHomeAddress().setCity("newCity");


            //값 타입의 비교 - 값 타입은 동등성 비교를 해야 한다
            Address addressA = new Address("city", "street", "1000");
            Address addressB = new Address("city", "street", "1000");
            System.out.println("addressA == addressB : " + (addressA == addressB)); //동일성 비교 false
            System.out.println("addressA equals addressB : " + (addressA.equals(addressB))); //동등성 비교 true

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
