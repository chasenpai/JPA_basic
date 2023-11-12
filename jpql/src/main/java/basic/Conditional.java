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

public class Conditional {

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

            //CASE - 기본
            String query1 =
                    "select " +
                        "case " +
                            "when m.age <= 10 then '학생요금' " +
                            "when m.age >= 60 then '경로요금' " +
                            "else '일반요금' " +
                        "end " +
                    "from " +
                        "Member m";
            List<String> result1 = em.createQuery(query1, String.class).getResultList();
            System.out.println(result1);

            //CASE - 단순
            String query2 =
                    "select " +
                            "case t.name " +
                            "when 'A' then '인센티브120%' " +
                            "when 'B' then '인센티브140%' " +
                            "else '인센티브100%' " +
                            "end " +
                    "from " +
                        "Team t";
            List<String> result2 = em.createQuery(query2, String.class).getResultList();
            System.out.println(result2);

            //COALESCE - 하나씩 조회해서 null 이 아니면 반환
            //사용자의 이름이 없으면 X 를 반환
            String query3 = "select coalesce(m.name, 'X') from Member m";
            List<String> result3 = em.createQuery(query3, String.class).getResultList();
            System.out.println(result3);

            //NULLIF - 두 값이 같으면 null 반환, 다르면 첫번째 값 반환
            //사용자 이름이 관리자면 null 을 반환, 나머지는 본인 이름 반환
            String query4 = "select nullif(m.name, '관리자') from Member m";
            List<String> result4 = em.createQuery(query4, String.class).getResultList();
            System.out.println(result4);

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
