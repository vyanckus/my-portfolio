package org.example;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

@AllArgsConstructor
public class DataConverter {
    private final SessionFactory sessionFactory;

    public void convertPurchaseListToLinkedPurchaseList() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            List<PurchaseList> purchaseLists = session.createQuery("FROM PurchaseList", PurchaseList.class)
                    .getResultList();

            for (PurchaseList purchaseList : purchaseLists) {
                Student student = session.createQuery("FROM Student WHERE name = :studentName", Student.class)
                        .setParameter("studentName", purchaseList.getStudentName())
                        .uniqueResult();
                Course course = session.createQuery("FROM Course WHERE name = :courseName", Course.class)
                        .setParameter("courseName", purchaseList.getCourseName())
                        .uniqueResult();

                if (student != null && course != null) {
                    LinkedPurchaseListKey id = new LinkedPurchaseListKey(student.getId(), course.getId());
                    LinkedPurchaseList linkedPurchaseList = new LinkedPurchaseList(id, student, course);
                    session.persist(linkedPurchaseList);
                } else {
                    System.err.println("Не найден студент или курс: " + purchaseList.getStudentName()
                            + " - " + purchaseList.getCourseName());
                }
            }
            transaction.commit();
        } catch (Exception e) {
            System.err.println("Ошибка при конвертации данных: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
