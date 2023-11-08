package collection;

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

            Member member = new Member();
            member.setName("memberA");
            member.setHomeAddress(new Address("city1", "street1", "10000"));

            member.getFavoriteFoods().add("치킨");
            member.getFavoriteFoods().add("피자");
            member.getFavoriteFoods().add("족발");

            member.getAddressHistory().add(new Address("old1", "oldStreet1", "11000"));
            member.getAddressHistory().add(new Address("old2", "oldStreet2", "22000"));

            //값 타입은 본인 스스로의 생명주기가 없다 - 영속성 전이가 이루어진다
            entityManager.persist(member);

            entityManager.flush();
            entityManager.clear();

            //조회 시 쿼리를 보면 member 만 가져온다 - 값 타입 컬렉션은 기본이 지연 로딩
            Member findMember = entityManager.find(Member.class, member.getId());

            //값 타입은 불변타입이기 때문에 업데이트 시 통째로 바꿔야 한다
            findMember.setHomeAddress(new Address("newCity", "street1", "10000"));

            findMember.getFavoriteFoods().remove("치킨");
            findMember.getFavoriteFoods().add("제육");

            //equals & hashCode 로 확인하고 동일한 객체를 제거한다
            //delete > insert - 주인과 연관된 모든 데이터를 삭제하고 컬렉션을 새로 저장한다
            findMember.getAddressHistory().remove(new Address("old1", "oldStreet1", "11000"));
            findMember.getAddressHistory().add(new Address("new1", "newStreet1", "12000"));

            /**
             * 값 타입 컬렉션의 제약사항
             * - 값 타입은 엔티티와 다르게 식별자 개념이 없어서 변경하면 추적이 어렵다
             * - 값 타입 컬렉션에 변경 사항이 발생하면 주인 엔티티와 연관된 모든 데이터를
             * - 삭제하고 값 타입 컬렉션에 있는 현재 값을 모두 다시 저장한다
             * - 값 타입 컬렉션을 매핑하는 테이블은 모든 컬럼을 묶어서 기본키를 구성해야 한다
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
