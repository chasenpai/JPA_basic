package basic;

import basic.entity.Member;

import javax.persistence.*;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            /**
             * JPQL 문법
             * - select m from Member as m where m.age > 18
             * - 엔티티와 속성은 대소문자 구분 O
             * - JPQL 키워드는 대소문자 구분 X
             * - 엔티티 이름 사용, 테이블이 아님
             * - 별칭은 필수(as 생략 가능)
             */

            Member member = new Member();
            member.setName("member1");
            member.setAge(10);
            em.persist(member);

            //TypeQuery - 반환 타입이 명확할 때 사용
            TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);
            TypedQuery<String> query2 = em.createQuery("select m.name from Member m", String.class);
            //Query - 반환 타입이 명확하지 않을 때 사용
            Query query3 = em.createQuery("select m.name, m.age from Member m");

            //결과가 하나 이상일 때 리스트로 반환 - 결과가 없으면 빈 리스트 반환
            List<Member> result1 = query1.getResultList();
            //결과가 정학히 하나일 때 단일 객체 반환 - 결과가 없거나 둘 이상이면 예외 발생
            Member result2 = query1.getSingleResult();

            //이름 기준 파라미터 바인딩
            TypedQuery<Member> query4 = em.createQuery("select m from Member m where m.name = :name", Member.class);
            query4.setParameter("name", "member1");
            Member result4 = query4.getSingleResult();
            //위치 기준 파라미터 바인딩 - 비추
            TypedQuery<Member> query5 = em.createQuery("select m from Member m where m.name = ?1", Member.class);
            query5.setParameter(1, "member1");
            Member result5 = query5.getSingleResult();

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
