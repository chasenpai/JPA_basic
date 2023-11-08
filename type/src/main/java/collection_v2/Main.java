package collection_v2;

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

            /**
             * 값 타입 컬렉션 대안
             * - 실무에서는 상황에 따라 값 타입 컬렉션 대신 일대다 관계를 고려
             * - 일대다 관계를 위한 엔티티를 만들고 여기에 값 타입을 사용
             * - 영속성 전이 + 고아 객체 제거를 사용해서 값 타입 컬렉션 처럼 사용
             */
            Member member = new Member();
            member.setName("memberA");
            member.setHomeAddress(new Address("city1", "street1", "10000"));

            member.getFavoriteFoods().add("치킨");
            member.getFavoriteFoods().add("피자");
            member.getFavoriteFoods().add("족발");

            member.getAddressHistory().add(new AddressEntity("old1", "oldStreet1", "11000"));
            member.getAddressHistory().add(new AddressEntity("old2", "oldStreet2", "22000"));

            entityManager.persist(member);

            /**
             * 엔티티 타입
             * - 식별자 O
             * - 생명 주기 관리
             * - 공유
             *
             * 값 타입
             * - 식별자 X
             * - 생명 주기를 엔티티에 의존
             * - 공유하지 않는것이 안전(복사해서 사용)
             * - 불변 객체로 만드는 것이 안전
             *
             * 값 타입은 정말 값 타입이라 판단될 때만 사용하고 엔티티와 값 타입을 혼동해서 엔티티를 값 타입으로
             * 만들면 안된다. 그리고 식별자가 필요하고 지속해서 값을 추적 & 변경해야 한다면 그것은
             * 값 타입이 아닌 엔티티다
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
