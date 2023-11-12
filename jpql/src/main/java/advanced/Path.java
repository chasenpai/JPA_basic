package advanced;

import advanced.entity.Member;
import advanced.entity.Team;
import basic.enums.MemberType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Path {

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

            //경로 표현식 - .(점)을 찍어 객체 그래프를 탐색하는 것
           String ex = "select " +
                            "m.name " + //상태 필드
                        "from " +
                            "Member m " +
                        "join " +
                            "m.team t " + //단일 값 연관 필드
                        "join " +
                            "m.orders o " + //컬렉션 값 연관 필드
                        "where " +
                            "t.name = 'A'";

            //상태 필드 - 경로 탐색의 끝, 탐색 불가
            String query1 = "select m.name from Member m";
            List<String> result1 = em.createQuery(query1, String.class).getResultList();

            //단일 값 연관 경로 - 묵시적 내부 조인 발생, 탐색 가능
            String query2 = "select m.team from Member m";
            List<Team> result2 = em.createQuery(query2, Team.class).getResultList();

            //컬렉션 값 연관 경로 - 묵시적 내부 조인 발생, 탐색 불가
            String query3 = "select t.members from Team t";
            Collection result3 = em.createQuery(query3, java.util.Collection.class).getResultList();
            //From 절에서 명시적 조인을 통해 별칭을 얻으면 별칭을 통해 탐색 가능
            String query4 = "select m.name from Team t join t.members m";
            List<String> result4 = em.createQuery(query4, String.class).getResultList();

            /**
             * 경로 탐색을 사용한 묵시적 조인 시 주의사항
             * - 항상 내부 조인
             * - 컬렉션은 경로 탐색의 끝, 명시적 조인을 통해 별칭을 얻어야함
             * - 경로 탐색은 주로 select, where 절에서 사용하지만 묵시적 조인으로 인해
             * - SQL 의 from (join) 절에 영향을 준다
             * - 가급적 묵시적 조인 대신에 명시적 조인 사용
             * - 조인은 SQL 튜닝에 중요 포인트
             * - 묵시적 조인은 조인이 일어나는 상황을 한눈에 파악하기 어려움
             */

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
