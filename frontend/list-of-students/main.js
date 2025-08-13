(function() {
    // Этап 1. В HTML файле создайте верстку элементов, которые будут статичны(неизменны).

    // Этап 2. Создайте массив объектов студентов.Добавьте в него объекты студентов, например 5 студентов.

    let studentsList = [
        // Добавьте сюда объекты студентов
        {
            name: 'Иван',
            surname: 'Иванов',
            patronymic: 'Иванович',
            birthDate: new Date(1998, 5, 10), // Месяцы в JavaScript начинаются с 0 (0 - январь, 11 - декабрь)
            startYear: 2016,
            faculty: 'Информатика'
        },
        {
            name: 'Петр',
            surname: 'Петров',
            patronymic: 'Петрович',
            birthDate: new Date(2000, 2, 15),
            startYear: 2018,
            faculty: 'Математика'
        },
        {
            name: 'Елена',
            surname: 'Смирнова',
            patronymic: 'Сергеевна',
            birthDate: new Date(1999, 10, 20),
            startYear: 2017,
            faculty: 'Филология'
        },
        {
            name: 'Алексей',
            surname: 'Кузнецов',
            patronymic: 'Дмитриевич',
            birthDate: new Date(2001, 7, 5),
            startYear: 2019,
            faculty: 'Информатика'
        },
        {
            name: 'Мария',
            surname: 'Попова',
            patronymic: 'Алексеевна',
            birthDate: new Date(2002, 1, 1),
            startYear: 2022,
            faculty: 'Экономика'
        }
    ]

    // Этап 3. Создайте функцию вывода одного студента в таблицу, по аналогии с тем, как вы делали вывод одного дела в модуле 8. Функция должна вернуть html элемент с информацией и пользователе.У функции должен быть один аргумент - объект студента.

    function getStudentItem(studentObj) {
        const tr = document.createElement('tr');

            // Вычисляем ФИО
            const fio = `${studentObj.surname} ${studentObj.name} ${studentObj.patronymic}`;

            // Вычисляем возраст
            const today = new Date();
            const birthDate = studentObj.birthDate;
            let age = today.getFullYear() - birthDate.getFullYear();
            const m = today.getMonth() - birthDate.getMonth();
            if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate())) {
                age--;
            }

            // Вычисляем годы обучения
            const endYear = studentObj.startYear + 4;
            const currentYear = new Date().getFullYear();
            let studyYears = `${studentObj.startYear}-${endYear} (${currentYear < endYear ? (currentYear - studentObj.startYear + ' курс') : 'закончил'})`;


            tr.innerHTML = `
                <td>${fio}</td>
                <td>${studentObj.faculty}</td>
                <td>${birthDate.toLocaleDateString()} (${age} лет)</td>
                <td>${studyYears}</td>
            `;
            return tr;
    }

    // Этап 4. Создайте функцию отрисовки всех студентов. Аргументом функции будет массив студентов.Функция должна использовать ранее созданную функцию создания одной записи для студента.Цикл поможет вам создать список студентов.Каждый раз при изменении списка студента вы будете вызывать эту функцию для отрисовки таблицы.

    function renderStudentsTable(studentsArray) {
        const tbody = document.querySelector('#studentsTable tbody');
        tbody.innerHTML = ''; // Очищаем таблицу перед отрисовкой

        for (const student of studentsArray) {
            tbody.append(getStudentItem(student));
        }
    }

    function validateForm() {
        const name = document.getElementById('name').value.trim();
        const surname = document.getElementById('surname').value.trim();
        const patronymic = document.getElementById('patronymic').value.trim();
        const birthDate = document.getElementById('birthDate').value;
        const startYear = document.getElementById('startYear').value;
        const faculty = document.getElementById('faculty').value.trim();
        const validationMessages = document.getElementById('validationMessages');
        validationMessages.innerHTML = ''; // Очищаем сообщения об ошибках

        let errors = [];

        if (!name) errors.push('Имя обязательно для заполнения.');
        if (!surname) errors.push('Фамилия обязательна для заполнения.');
        if (!patronymic) errors.push('Отчество обязательно для заполнения.');
        if (!birthDate) errors.push('Дата рождения обязательна для заполнения.');
        if (!startYear) errors.push('Год начала обучения обязателен для заполнения.');
        if (!faculty) errors.push('Факультет обязателен для заполнения.');

        // Проверка даты рождения
        const birthDateObj = new Date(birthDate);
        const now = new Date();
        const minDate = new Date(1900, 0, 1);

        if (birthDateObj > now) errors.push('Дата рождения не может быть в будущем.');
        if (birthDateObj < minDate) errors.push('Дата рождения не может быть раньше 01.01.1900.');

        // Проверка года начала обучения
        const startYearNum = parseInt(startYear);
        if (startYearNum < 2000 || startYearNum > new Date().getFullYear()) errors.push('Год начала обучения должен быть между 2000 и текущим годом.');

        if (errors.length > 0) {
            let message = '<ul>';
            for (const error of errors) {
                message += `<li>${error}</li>`;
            }
            message += '</ul>';
            validationMessages.innerHTML = message;
            return false; // Валидация не прошла
        }

        return true; // Валидация прошла
    }

    function addStudent(event) {
        event.preventDefault(); // Предотвращаем отправку формы по умолчанию

        if (validateForm()) {
            const name = document.getElementById('name').value.trim();
            const surname = document.getElementById('surname').value.trim();
            const patronymic = document.getElementById('patronymic').value.trim();
            const birthDate = new Date(document.getElementById('birthDate').value);
            const startYear = parseInt(document.getElementById('startYear').value);
            const faculty = document.getElementById('faculty').value.trim();

            const newStudent = {
                name: name,
                surname: surname,
                patronymic: patronymic,
                birthDate: birthDate,
                startYear: startYear,
                faculty: faculty
            };

            studentsList.push(newStudent); // Добавляем студента в массив
            renderStudentsTable(studentsList); // Перерисовываем таблицу

            // Очищаем форму
            document.getElementById('addStudentForm').reset();
            document.getElementById('validationMessages').innerHTML = '';
        }
    }


    // Этап 5. Создайте функцию сортировки массива студентов и добавьте события кликов на соответствующие колонки.

    function sortStudents(sortField) {
        //  Копируем массив, чтобы не изменять исходный
        let sortedStudents = [...studentsList];

        sortedStudents.sort((a, b) => {
            let valueA, valueB;

            switch (sortField) {
                case 'fio':
                    // Сортировка по ФИО
                    valueA = `${a.surname} ${a.name} ${a.patronymic}`;
                    valueB = `${b.surname} ${b.name} ${b.patronymic}`;
                    break;
                case 'faculty':
                    // Сортировка по факультету
                    valueA = a.faculty;
                    valueB = b.faculty;
                    break;
                case 'birthDate':
                    // Сортировка по дате рождения
                    valueA = a.birthDate;
                    valueB = b.birthDate;
                    break;
                case 'studyYears':
                    // Сортировка по году начала обучения
                    valueA = a.startYear;
                    valueB = b.startYear;
                    break;
                default:
                    return 0; // Если поле не распознано, не сортируем
            }

            // Сравниваем значения
            if (valueA < valueB) {
                return -1;
            }
            if (valueA > valueB) {
                return 1;
            }
            return 0;
            
            // return valueA - valueB;
        });

        renderStudentsTable(sortedStudents); // Отображаем отсортированную таблицу
    }

    // Этап 6. Создайте функцию фильтрации массива студентов и добавьте события для элементов формы.

    function filterStudents() {
        const filterFIO = document.getElementById('filterFIO').value.trim().toLowerCase();
        const filterFaculty = document.getElementById('filterFaculty').value.trim().toLowerCase();
        const filterStartYear = document.getElementById('filterStartYear').value.trim();
        const filterEndYear = document.getElementById('filterEndYear').value.trim();

        let filteredStudents = [...studentsList]; // Копируем массив

        // Фильтрация по ФИО
        if (filterFIO) {
            filteredStudents = filteredStudents.filter(student =>
                `${student.surname} ${student.name} ${student.patronymic}`.toLowerCase().includes(filterFIO)
            );
        }

        // Фильтрация по факультету
        if (filterFaculty) {
            filteredStudents = filteredStudents.filter(student =>
                student.faculty.toLowerCase().includes(filterFaculty)
            );
        }

        // Фильтрация по году начала обучения
        if (filterStartYear) {
            filteredStudents = filteredStudents.filter(student =>
                student.startYear == parseInt(filterStartYear)
            );
        }

        // Фильтрация по году окончания обучения
        if (filterEndYear) {
            filteredStudents = filteredStudents.filter(student => {
                const endYear = student.startYear + 4;
                return endYear == parseInt(filterEndYear);
            });
        }

        renderStudentsTable(filteredStudents); // Отображаем отфильтрованную таблицу
    }


    // Этап 5. К форме добавления студента добавьте слушатель события отправки формы, в котором будет проверка введенных данных.Если проверка пройдет успешно, добавляйте объект с данными студентов в массив студентов и запустите функцию отрисовки таблицы студентов, созданную на этапе 4.
    
    document.addEventListener('DOMContentLoaded', function() {
        renderStudentsTable(studentsList); // Отображаем таблицу при загрузке страницы

        const addStudentForm = document.getElementById('addStudentForm');
        addStudentForm.addEventListener('submit', addStudent); // Привязываем функцию addStudent к событию submit формы

        // Сортировка по клику на заголовки таблицы
        const tableHeaders = document.querySelectorAll('#studentsTable th');
        tableHeaders.forEach(header => {
            header.addEventListener('click', function() {
                const sortField = this.dataset.sort;
                if (sortField) {
                    sortStudents(sortField);
                }
            });
        });

        // Фильтрация по изменению в полях фильтра
        const filterInputs = document.querySelectorAll('.filters input');
        filterInputs.forEach(input => {
            input.addEventListener('input', filterStudents); // Событие input срабатывает при каждом изменении значения
        });
    });
})();
