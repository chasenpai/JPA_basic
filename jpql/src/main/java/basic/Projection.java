package basic;

import basic.dto.MemberDto;
import basic.entity.Address;
import basic.entity.Member;
import basic.entity.Order;
import basic.entity.Team;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

public class Projection {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            /**
             * 프로젝션
             * - select 절에 조회할 대상을 지정하는 것
             * - 대상으로 엔티티, 임베디드 타입, 스칼라 타입(숫자, 문자등 기본 데이터 타입) 가능
             */

            Member member = new Member();
            member.setName("member1");
            member.setAge(10);
            em.persist(member);

            em.flush();
            em.clear();

            //엔티티 프로젝션
            List<Member> result1 = em.createQuery("select m from Member m", Member.class).getResultList();
            //update 쿼리가 나감 - 영속성 컨텍스트 안에서 관리됨
            result1.get(0).setAge(20);
            //연관된 객체를 받을 수 있음 - 실제 쿼리는 join 을 사용한다(묵시적 조인, 해당 코드만 보고 예측이 안될 수 있음)
            List<Team> result2 = em.createQuery("select m.team from Member m", Team.class).getResultList();
            //조인을 직접 사용할 수 있다
            List<Team> result3 = em.createQuery("select t from Member m join m.team t", Team.class).getResultList();

            //임베디드 타입 프로젝션 - 값 타입의 한계로 인해 소속된 엔티티를 정해줘야 한다
            List<Address> result4 = em.createQuery("select o.address from Order o", Address.class).getResultList();

            //스칼라 타입 프로젝션
            List result5 = em.createQuery("select m.name, m.age from Member m").getResultList();


            //여러 값 조회
            //Object[] 타입으로 조회
            Object object1 = result5.get(0);
            Object[] objects1 = (Object[]) object1;
            System.out.println(objects1[0]);
            System.out.println(objects1[1]);

            //Query 타입으로 조회
            List<Object[]> result6 = em.createQuery("select m.name, m.age from Member m").getResultList();
            Object[] objects2 = result6.get(0);
            System.out.println(objects2[0]);
            System.out.println(objects2[1]);

            //new 명령어로 조회 - 단순 값을 DTO 로 바로 조회
            List<MemberDto> result7 = em.createQuery(
                    "select new basic.dto.MemberDto(m.name, m.age) from Member m", MemberDto.class
                    ).getResultList();
            MemberDto dto = result7.get(0);
            System.out.println(dto);

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
