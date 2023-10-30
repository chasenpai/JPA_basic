package ex3.inheritance_table_per_class;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Main {

    public static void main(String[] args) {

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {

            Movie movie = new Movie();
            movie.setDirector("DirectorA");
            movie.setActor("ActorA");
            movie.setName("MovieA");
            movie.setPrice(10000);
            entityManager.persist(movie);

            entityManager.flush();
            entityManager.clear();

            //만약 item id 로 찾아야한다면 자식 테이블을 모두 select 해야 하기 때문에 쿼리가 매우 복잡해진다
            Item findItem = entityManager.find(Item.class, movie.getId());
            System.out.println("findItem = " + findItem.getName());

            transaction.commit();
        }catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        }finally {
            entityManager.close();
            entityManagerFactory.close();
        }

    }

}
