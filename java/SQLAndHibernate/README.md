# SQLAndHibernate

Этот проект демонстрирует создание приложения, работающего с базой данных MySQL при помощи Hibernate, создающего таблицы в базе данных и работающего с данными в этих таблицах.

## Описание проекта

Приложение создает таблицы в базе данных MySQL на основе классов-сущностей, соответствующих схемам таблиц, представленным на диаграмме (см. ниже). Затем приложение заполняет таблицы готовыми данными и преобразует данные из одной таблицы в другую, используя Hibernate.

## Функциональность

*   **Создание таблиц в базе данных:** Автоматическое создание таблиц в базе данных MySQL на основе классов-сущностей Hibernate.
*   **Заполнение таблиц данными:** Заполнение таблиц готовым дампом данных.
*   **Преобразование данных:** Конвертация данных из таблицы `PurchaseList` в таблицу `LinkedPurchaseList` с использованием составного ключа.
*   **Использование составного ключа:** Создание и использование составного ключа для таблицы `LinkedPurchaseList`, состоящего из `student_id` и `course_id`.

## Используемые технологии

*   Java
*   Hibernate
*   MySQL

## Сущности

Следующие классы-сущности представляют таблицы в базе данных:

*   `Students`: Содержит информацию о студентах (id, name, age, registration_date).
*   `Courses`: Содержит информацию о курсах (id, name, duration, type, description, teacher_id, students_count, price, price_per_hour).
*   `Subscriptions`: Содержит информацию о подписках студентов на курсы (student_id, course_id, subscription_date).
*   `Teachers`: Содержит информацию о преподавателях (id, name, salary, age).
*   `PurchaseList`: Содержит информацию о покупках (student_name, course_name, price, subscription_date).
*   `LinkedPurchaseList`: Содержит информацию о связях студентов и курсов (student_id, course_id) - *составной ключ*.

## Схема базы данных

<img width="1600" height="498" alt="unnamed_XFj0QJw" src="https://github.com/user-attachments/assets/33e1d27f-bc59-4936-ac8d-d18aced7e58f" />


## Инструкции по установке и запуску

1.  **Предварительные требования:**
    *   Установленная Java Development Kit (JDK).
    *   Установленный Apache Maven (или другая система сборки).
    *   Установленный MySQL Server.

2.  **Клонируйте репозиторий:**

3.  **Создайте пустую базу данных MySQL:**

    Создайте пустую базу данных MySQL, к которой будет подключаться приложение (например, `skillbox_2`).

4.  **Настройте подключение к базе данных:**

    Убедитесь, что файл конфигурации Hibernate (`src/main/resources/hibernate.cfg.xml`) содержит правильные параметры подключения к вашей базе данных MySQL:

    ```xml
    <property name="connection.url">jdbc:mysql://localhost:3306/skillbox_2?useSSL=false</property>
    <property name="connection.username">root</property>
    <property name="connection.password">6028+Vyan</property>
    ```

    *Если вы используете другие имя пользователя, пароль или имя базы данных, измените соответствующие значения в файле `src/main/resources/hibernate.cfg.xml`.*

5.  **Настройте Hibernate для обновления схемы:**

    Убедитесь, что в файле `src/main/resources/hibernate.cfg.xml` параметр `hbm2ddl.auto` установлен в значение `update`:

    ```xml
    <property name="hbm2ddl.auto">update</property>
    ```

    *Это позволит Hibernate автоматически обновлять схему базы данных в соответствии с вашими классами-сущностями.*

    *Другие возможные значения `hbm2ddl.auto`:*
    *   `validate` - проверить схему, не вносить изменения в базу данных.
    *   `create` - создать схему, уничтожая предыдущие данные.
    *   `create-drop` - удалить схему при закрытии SessionFactory.

6.  **Подключите библиотеку Hibernate:**

    Убедитесь, что библиотека Hibernate подключена к вашему проекту. При использовании Maven, добавьте необходимые зависимости в файл `pom.xml`. Пример:

    ```xml
    <dependency>
        <groupId>org.hibernate</groupId>
        <artifactId>hibernate-core</artifactId>
        <version>5.6.15.Final</version> <!-- Актуальную версию можно найти на сайте Hibernate -->
    </dependency>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.30</version> <!-- Актуальную версию можно найти на сайте MySQL Connector -->
    </dependency>
    ```

7.  **Запустите приложение:**

    Запустите приложение. При первом запуске Hibernate автоматически создаст таблицы в вашей базе данных (или обновит существующие).

8.  **Залейте дамп с данными:**

    Залейте в созданные таблицы готовый дамп с данными из файла `skillbox_dump_wfk.sql` (например, используя MySQL Workbench или командную строку MySQL).  Пример:

    ```bash
    mysql -u root -p skillbox_2 < skillbox_dump_wfk.sql
    ```

9.  **Запустите код для конвертации данных:**

    Запустите код, который конвертирует данные из таблицы `PurchaseList` в таблицу `LinkedPurchaseList`. Убедитесь, что данные успешно сконвертированы и таблица `LinkedPurchaseList` заполнена корректно.

## Автор

Вянцкус Фёдор
