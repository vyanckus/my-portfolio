package org.example;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class Main {
    private static SessionFactory sessionFactory;

    static {
        try {
            StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure("hibernate.cfg.xml").build();
            Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
            sessionFactory = metadata.getSessionFactoryBuilder().build();
        } catch (Exception e) {
            System.err.println("Ошибка при создании SessionFactory: " + e.getMessage());
            throw new ExceptionInInitializerError(e);
        }
    }
    public static void main(String[] args) {
        DataConverter dataConverter = new DataConverter(sessionFactory);
        dataConverter.convertPurchaseListToLinkedPurchaseList();

        if (sessionFactory != null) {
            try {
                sessionFactory.close();
            } catch (Exception e) {
                System.err.println("Ошибка при закрытии SessionFactory: " + e.getMessage());
            }
        }
    }
}