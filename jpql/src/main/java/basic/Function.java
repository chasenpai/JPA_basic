package basic;

import basic.entity.Member;
import basic.entity.Team;
import basic.enums.MemberType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class Function {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Team teamA = new Team();
            teamA.setName("B");
            em.persist(teamA);

            Member memberA = new Member();
            memberA.setName("관리자");
            memberA.setAge(60);
            memberA.setTeam(teamA);
            memberA.setType(MemberType.ADMIN);
            em.persist(memberA);

            em.flush();
            em.clear();

            /**
             * JPQL 기본 함수
             * - CONCAT
             * - SUBSTRING
             * - TRIM
             * - LOWER, UPPER
             * - LENGTH
             * - LOCATE
             * - ABS, SQRT, MOD
             * - SIZE, INDEX (JPA 용도)
             */

            //concat
//            String query1 = "select concat('a', 'b') from Member m";
            String query1 = "select 'a' || 'b' from Member m"; //하이버네이트 구현체는 || 로 대체 가능
            List<String> result1 = em.createQuery(query1, String.class).getResultList();
            System.out.println(result1);

            //substring
            String query2 = "select substring(m.name, 2, 3) from Member m";
            List<String> result2 = em.createQuery(query2, String.class).getResultList();
            System.out.println(result2);

            //locate
            String query3 = "select locate('c', 'abcdefg') from Member m";
            List<Integer> result3 = em.createQuery(query3, Integer.class).getResultList();
            System.out.println(result3);

            //size - 컬렉션의 사이즈 반환
            //index - 컬렉션의 위치 값을 반환
            String query4 = "select size(t.members) from Team t";
            List<Integer> result4 = em.createQuery(query4, Integer.class).getResultList();
            System.out.println(result4);

            //사용자 정의 함수 호출(예시)
//            String query5 = "select function('group_concat', m.name) from Member m";
            String query5 = "select group_concat(m.name) from Member m"; //하이버네이트 구현체는 이렇게 사용 가능
            List<String> result5 = em.createQuery(query5, String.class).getResultList();
            System.out.println(result5);

            tx.commit();
        }catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        }finally {
            em.close();
            emf.close();
        }

    }

}
