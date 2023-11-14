package advanced;

import advanced.entity.Member;
import advanced.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class Treat {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            //TYPE - 조회 대상을 특정 자식으로 한정
            String jpql1 = "select i from Item i where type(i) in (Book, Movie)";
            String sql1 = "select i from Item where i.DTYPE in ('B', 'M')";

            //TREAT - 자바의 타입 캐스팅과 유사, 상속 구조에서 부모 타입을 특정 자식 타입으로 다룰 때 사용
            String jpql2 = "select i from Item i where treat(i as Book).author = 'kim'";
            String sql2 = "select i. * from Item i where i.DTYPE = 'B' and i.author = 'kim'";

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
