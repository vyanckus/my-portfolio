(function() {


    let studentsList = [
        {
            name: 'Иван',
            surname: 'Иванов',
            patronymic: 'Иванович',
            birthDate: new Date(1998, 5, 10), 
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

    function getStudentItem(studentObj) {
        const tr = document.createElement('tr');

            const fio = `${studentObj.surname} ${studentObj.name} ${studentObj.patronymic}`;

            const today = new Date();
            const birthDate = studentObj.birthDate;
            let age = today.getFullYear() - birthDate.getFullYear();
            const m = today.getMonth() - birthDate.getMonth();
            if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate())) {
                age--;
            }

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

    function renderStudentsTable(studentsArray) {
        const tbody = document.querySelector('#studentsTable tbody');
        tbody.innerHTML = '';

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
        validationMessages.innerHTML = '';

        let errors = [];

        if (!name) errors.push('Имя обязательно для заполнения.');
        if (!surname) errors.push('Фамилия обязательна для заполнения.');
        if (!patronymic) errors.push('Отчество обязательно для заполнения.');
        if (!birthDate) errors.push('Дата рождения обязательна для заполнения.');
        if (!startYear) errors.push('Год начала обучения обязателен для заполнения.');
        if (!faculty) errors.push('Факультет обязателен для заполнения.');

        const birthDateObj = new Date(birthDate);
        const now = new Date();
        const minDate = new Date(1900, 0, 1);

        if (birthDateObj > now) errors.push('Дата рождения не может быть в будущем.');
        if (birthDateObj < minDate) errors.push('Дата рождения не может быть раньше 01.01.1900.');

        const startYearNum = parseInt(startYear);
        if (startYearNum < 2000 || startYearNum > new Date().getFullYear()) errors.push('Год начала обучения должен быть между 2000 и текущим годом.');

        if (errors.length > 0) {
            let message = '<ul>';
            for (const error of errors) {
                message += `<li>${error}</li>`;
            }
            message += '</ul>';
            validationMessages.innerHTML = message;
            return false;
        }

        return true;
    }

    function addStudent(event) {
        event.preventDefault();

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

            studentsList.push(newStudent);
            renderStudentsTable(studentsList);

            document.getElementById('addStudentForm').reset();
            document.getElementById('validationMessages').innerHTML = '';
        }
    }

    function sortStudents(sortField) {

        let sortedStudents = [...studentsList];

        sortedStudents.sort((a, b) => {
            let valueA, valueB;

            switch (sortField) {
                case 'fio':
                    valueA = `${a.surname} ${a.name} ${a.patronymic}`;
                    valueB = `${b.surname} ${b.name} ${b.patronymic}`;
                    break;
                case 'faculty':
                    valueA = a.faculty;
                    valueB = b.faculty;
                    break;
                case 'birthDate':
                    valueA = a.birthDate;
                    valueB = b.birthDate;
                    break;
                case 'studyYears':
                    valueA = a.startYear;
                    valueB = b.startYear;
                    break;
                default:
                    return 0;
            }

            if (valueA < valueB) {
                return -1;
            }
            if (valueA > valueB) {
                return 1;
            }
            return 0;
            
            // return valueA - valueB;
        });

        renderStudentsTable(sortedStudents); 
    }

    function filterStudents() {
        const filterFIO = document.getElementById('filterFIO').value.trim().toLowerCase();
        const filterFaculty = document.getElementById('filterFaculty').value.trim().toLowerCase();
        const filterStartYear = document.getElementById('filterStartYear').value.trim();
        const filterEndYear = document.getElementById('filterEndYear').value.trim();

        let filteredStudents = [...studentsList];

        if (filterFIO) {
            filteredStudents = filteredStudents.filter(student =>
                `${student.surname} ${student.name} ${student.patronymic}`.toLowerCase().includes(filterFIO)
            );
        }

        if (filterFaculty) {
            filteredStudents = filteredStudents.filter(student =>
                student.faculty.toLowerCase().includes(filterFaculty)
            );
        }

        if (filterStartYear) {
            filteredStudents = filteredStudents.filter(student =>
                student.startYear == parseInt(filterStartYear)
            );
        }

        if (filterEndYear) {
            filteredStudents = filteredStudents.filter(student => {
                const endYear = student.startYear + 4;
                return endYear == parseInt(filterEndYear);
            });
        }

        renderStudentsTable(filteredStudents);
    }

    document.addEventListener('DOMContentLoaded', function() {
        renderStudentsTable(studentsList);

        const addStudentForm = document.getElementById('addStudentForm');
        addStudentForm.addEventListener('submit', addStudent);

        const tableHeaders = document.querySelectorAll('#studentsTable th');
        tableHeaders.forEach(header => {
            header.addEventListener('click', function() {
                const sortField = this.dataset.sort;
                if (sortField) {
                    sortStudents(sortField);
                }
            });
        });

        const filterInputs = document.querySelectorAll('.filters input');
        filterInputs.forEach(input => {
            input.addEventListener('input', filterStudents);
        });
    });
})();
