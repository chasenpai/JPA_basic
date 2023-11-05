package value_type;

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

            Member member1 = new Member();
            member1.setName("memberA");
            member1.setHomeAddress(address);
            entityManager.persist(member1);

            Member member2 = new Member();
            member2.setName("memberB");
            member2.setHomeAddress(address);
            entityManager.persist(member2);

            //update 쿼리가 두번 나간다 - 임베디드 타입 같은 값 타입을 여러 엔티티에서 공유하면 부작용 발생
            //공유해서 사용하고 싶다면 address 를 엔티티로 만들어 사용해야 한다
            member1.getHomeAddress().setCity("newCity");

            entityManager.flush();
            entityManager.clear();


            Address address2 = new Address("city2", "street2", "2000");

            Member member3 = new Member();
            member3.setName("memberC");
            member3.setHomeAddress(address2);
            entityManager.persist(member3);

            //인스턴스를 복사해서 사용
            Address copyAddress = new Address(address2.getCity(), address2.getStreet(), address2.getZipcode());

            Member member4 = new Member();
            member4.setName("memberD");
            member4.setHomeAddress(copyAddress);
            entityManager.persist(member4);

            //update 쿼리가 한번 나감
            member4.getHomeAddress().setCity("newCity");

            /**
             * 객체 타입의 한계
             * - 항상 값을 복사해서 사용하면 공유 참조로 인해 발생하는 부작용을 피할 수 있다
             * - 문제는 임베디드 타입처럼 직접 정의한 값 타입은 자바의 기본 타입이 아니라 객체 타입이다
             * - 자바 기본 타입에 값을 대입하면 값을 복사한다
             * - 객체 타입은 참조 값을 직접 대입하는 것을 막을 방법이 없다
             * - 객체의 공유 참조는 피할 수 없다
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
