package ex4.proxy;

import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {

            //1
            //getReference 를 호출하는 시점엔 쿼리가 나가지 않는다
            Member findMember = entityManager.getReference(Member.class, 34L);
            //데이터베이스 조회를 미루는 가짜 클래스 - 프록시
            System.out.println("findMember = " + findMember.getClass());
            //실제 사용하는 시점에 쿼리가 나간다 - 프록시 객체 초기화
            //프록시 객체는 처음 사용할 때 한번만 초기화
            //프록시 객체를 초기화 할 때 프록시 객체가 실제 엔티티로 바뀌는 것은 아니고
            //프록시 객체를 통해서 실제 엔티티에 접근 가능
            printMember(findMember);

            //2
            Member member1 = new Member();
            member1.setName("member1");
            entityManager.persist(member1);

            Member member2 = new Member();
            member2.setName("member2");
            entityManager.persist(member2);

            entityManager.flush();
            entityManager.clear();

            Member find1 = entityManager.find(Member.class, member1.getId());
            Member find2 = entityManager.getReference(Member.class, member2.getId());
            //프록시 객체는 원본 엔티티를 상속받기 때문에 타입 체크 시 주의해야 한다
            System.out.println("find1 == find2 : " + (find1.getClass() == find2.getClass())); //false
            System.out.println("find1 == find2 : " + (find1 instanceof Member)); //true
            System.out.println("find1 == find2 : " + (find2 instanceof Member)); //true

            //3
            Member member3 = new Member();
            member3.setName("member3");
            entityManager.persist(member3);

            entityManager.flush();
            entityManager.clear();

            Member find3 = entityManager.find(Member.class, member3.getId());
            System.out.println("find3 = " + find3.getClass());
            //영속성 컨텍스트에 찾는 엔티티가 이미 있으면 getReference 로 호출해도 프록시가 아닌 실제 엔티티 반환
            Member reference = entityManager.getReference(Member.class, member3.getId());
            System.out.println("reference = " + reference.getClass());

            //4
            Member member4 = new Member();
            member4.setName("member4");
            entityManager.persist(member4);

            entityManager.flush();
            entityManager.clear();

            Member reference4 = entityManager.getReference(Member.class, member4.getId());
            System.out.println("reference4 = " + reference4.getClass());
            //프록시가 한번 초기화되면 find 룰 호출해도 프록시를 반환해버린다
            Member find4 = entityManager.find(Member.class, member4.getId());
            System.out.println("find4 = " + find4.getClass());
            System.out.println("reference4 == find4 : " + (reference4 == find4)); //true

            //5
            Member member5 = new Member();
            member5.setName("member5");
            entityManager.persist(member5);

            entityManager.flush();
            entityManager.clear();

            Member reference5 = entityManager.getReference(Member.class, member5.getId());
            System.out.println("reference5 = " + reference5.getClass());

            //could not initialize proxy - 영속성 컨텍스트의 도움을 받을 수 없는 준영속 상태일 때
            //프록시를 초기화 하면 문제가 발생한다 - LazyInitializationException 예외 발생
//            entityManager.detach(reference5);
//            entityManager.close();
//            entityManager.clear();
//            System.out.println("name = " + reference5.getName());

            //6
            Member member6 = new Member();
            member6.setName("member6");
            entityManager.persist(member6);

            entityManager.flush();
            entityManager.clear();

            //프록시 확인
            Member reference6 = entityManager.getReference(Member.class, member6.getId());
            PersistenceUnitUtil persistenceUtil = entityManagerFactory.getPersistenceUnitUtil();
            //프록시 강제 초기화
            Hibernate.initialize(reference6);
            //프록시 클래스 확인
            System.out.println("reference6 = " + reference6.getClass());
            //프록시 인스턴스 초기화 여부
            System.out.println("isLoaded = " + persistenceUtil.isLoaded(reference6));

            transaction.commit();
        }catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        }finally {
            entityManager.close();
            entityManagerFactory.close();
        }

    }

    private static void printMember(Member member) {
        String memberName = member.getName();
        System.out.println("memberName = " + memberName);
    }

    private static void printMemberAndTeam(Member member) {
        String memberName = member.getName();
        System.out.println("memberName = " + memberName);
        
        Team team = member.getTeam();
        String teamName = team.getName();
        System.out.println("teamName = " + teamName);
    }

}
