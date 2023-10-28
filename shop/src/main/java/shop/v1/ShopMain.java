package shop.v1;


import shop.v1.domain.Order;
import shop.v1.domain.OrderItem;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class ShopMain {

    public static void main(String[] args) {

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {

            //양방향 연관관계가 아니여도 개발하는데 아무런 문제가 없다
            //실무에서 JPQL 을 사용하다 보면 양방향으로 참조해야 할 때가 많다
            //가능하면 단방향으로 해라
            //핵심은 단방향을 잘 설계하는 것이다
            Order order = new Order();
            entityManager.persist(order);
//            order.addOrderItem(new OrderItem());
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            entityManager.persist(orderItem);

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
