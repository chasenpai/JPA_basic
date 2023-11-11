package basic;

import basic.dto.MemberDto;
import basic.entity.Address;
import basic.entity.Member;
import basic.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class Paging {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            /**
             * 페이징
             * - JPA 는 페이징을 두가지 API 로 추상화 해버림
             * - setFirstResult : 조회 시작 위치
             * - setMaxResults : 조회할 데이터 수
             */

            for(int i = 0; i < 100; i++) {
                Member member = new Member();
                member.setName("member" + i);
                member.setAge(i);
                em.persist(member);
            }

            em.flush();
            em.clear();

            //DB 방언에 따른 페이징 쿼리를 날려준다
            String query1 = "select m from Member m order by m.age desc";
            List<Member> result1 = em.createQuery(query1, Member.class)
                    .setFirstResult(50)
                    .setMaxResults(20)
                    .getResultList();

            for (Member member : result1) {
                System.out.println(member);
            }

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
