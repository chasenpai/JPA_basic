package basic;

import basic.entity.Member;
import basic.entity.Team;
import basic.enums.MemberType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Arrays;
import java.util.List;

public class Type {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Team teamA = new Team();
            teamA.setName("A");
            em.persist(teamA);

            Member memberA = new Member();
            memberA.setName("A");
            memberA.setAge(20);
            memberA.setTeam(teamA);
            memberA.setType(MemberType.ADMIN);
            em.persist(memberA);

            em.flush();
            em.clear();

            /**
             * JPQL 타입 표현
             * - 문자 : 'HELLO', 'She"s'
             * - 숫자 : 10L, 10D, 10F
             * - boolean : true, false
             * - enum : basic.enums.MemberType.Admin (패키지명 포함)
             * - 엔티티 타입 : TYPE(i) = Book (상속 관계에서 사용)
             */

            //문자, 숫자, boolean
            String query1 = "select m.name, 'HELLO', 3, true from Member m";
            List<Object[]> result1 = em.createQuery(query1).getResultList();
            Object[] objects = result1.get(0);
            System.out.println(Arrays.toString(objects));

            //enum - 하드코딩
            String query2 = "select m from Member m where m.type = basic.enums.MemberType.ADMIN";
            List<Member> result2 = em.createQuery(query2, Member.class).getResultList();

            //enum - 파라미터 바인딩
            String query3 = "select m from Member m where m.type = :userType";
            List<Member> result3 = em.createQuery(query3, Member.class)
                    .setParameter("userType", MemberType.ADMIN)
                    .getResultList();

            //엔티티 타입(예시)
            String query4 = "select i from Item i where type(i) = Book";

            /**
             * JPQL 기타 표현식
             * - SQL 과 문법이 같은 식
             * - EXISTS, IN
             * - AND, OR, NOT
             * - =, >, >=, <, <=, <>
             * - BETWEEN, LIKE, IS NULL ..
             */
            String query5 = "select m from Member m where m.age between 0 and 20";
            List<Member> result5 = em.createQuery(query5, Member.class).getResultList();

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
