package hello;

import com.sun.security.jgss.GSSUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class PersistenceContext {

    public static void main(String[] args) {

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello"); //persistence.xml 에서 작성한 persistence-unit name
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {

            /**
             * 영속성 컨텍스트(Persistence Context)
             * - 논리적인 개념으로 눈에 보이지 않는다
             * - 엔티티 매니저를 통해서 영속성 컨텍스트에 접근
             * 이점
             * - 1차 캐시
             * - 동일성 보장
             * - 트랜잭션을 지원하는 쓰기 지연
             * - 변경 감지와 지연 로딩
             */

            //비영속(new/transient) - 영속성 컨텍스트와 전혀 관계가 없는 새로운 상태
            Member member = new Member();
            member.setId(15L);
            member.setName("MemberA");

            //영속(managed) - 영속성 컨텍스트에 관리되는 상태
            System.out.println("--before persist--");
            entityManager.persist(member); //1차 캐시에 저장
            System.out.println("--after persist--");

            //1차 캐시에서 조회 - select 쿼리가 날라가지 않음
            Member findMember1 = entityManager.find(Member.class, 15L);
            System.out.println("findMember1 = " + findMember1);

            //영속 엔티티의 동일성 보장
            Member findMember2 = entityManager.find(Member.class, 15L);
            System.out.println("findMember1 == findMember2 = " + (findMember1 == findMember2));

            //변경 감지(더티 체킹) - 커밋 직전에 변경을 체크(스냅샷)하고 업데이트 쿼리를 날림
            Member findMember3 = entityManager.find(Member.class, 2L);
            findMember3.setName("MemberB");

            //준영속(detached) - 영속성 컨텍스트에 저장되었다가 분리된 상태
//            entityManager.detach(member);

            //삭제(removed) - 삭제된 상태
//            entityManager.remove(member);

            //트랜잭션을 지원하는 쓰기 지연 - 커밋하는 순간 insert sql 을 날린다
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
