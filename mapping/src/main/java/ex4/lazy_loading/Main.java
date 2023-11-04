package ex4.lazy_loading;

import org.hibernate.Hibernate;

import javax.persistence.*;

public class Main {

    public static void main(String[] args) {

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {

            Team team1 = new Team();
            team1.setName("teamA");
            entityManager.persist(team1);

            Member member1 = new Member();
            member1.setName("memberA");
            member1.setTeam(team1);
            entityManager.persist(member1);

            entityManager.flush();
            entityManager.clear();

            //지연 로딩을 사용하여 프록시로 조회
            Member findMember1 = entityManager.find(Member.class, member1.getId());
            System.out.println("findTeam = " + findMember1.getTeam().getClass());
            
            //실제 사용하는 시점에 쿼리가 나간다
            //즉시 로딩의 경우 프록시로 조회되지 않고 조인으로 함께 가져온다
            String teamName = findMember1.getTeam().getName();
            System.out.println("teamName = " + teamName);

            //단순히 Member 만 사용하는 로직이라면 ? > 지연 로딩이 좋다
            //Member 와 Team 을 자주 함께 사용한다면 ? > 즉시 로딩이 좋다

            /**
             * 하지만?
             * 가급적 지연 로딩만 사용하는 것이 좋다(특히 실무)
             * 즉시 로딩을 적용하면 예상하지 못한 SQL 이 발생할 수 있다
             * 즉시 로딩은 JPQL 에서 N + 1 문제를 일으킨다
             * @ManyToOne & @OneToOne 은 기본이 즉시 로딩이므로 지연 로딩으로 설정 해준다
             * @OneToMany, @ManyToMany 는 기본이 지연 로딩이다
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
