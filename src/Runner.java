import data.Curator;
import data.Group;
import data.Student;
import db.IDBExecutor;
import db.MySqlExecutor;
import tables.AbsTable;
import tables.CuratorTable;
import tables.GroupTable;
import tables.StudentTable;

import java.util.ArrayList;
import java.util.List;

public class Runner {

    public static void main(String... args) {
        IDBExecutor dbExecutor = new MySqlExecutor();

        try {

            AbsTable studentTable = new StudentTable(dbExecutor);
            AbsTable groupTable = new GroupTable(dbExecutor);
            AbsTable curatorTable = new CuratorTable(dbExecutor);

// Создать таблицу Student Колонки id, fio, sex, id_group

            List<String> columnsStudentTable = new ArrayList<>();
            columnsStudentTable.add("id int primary key");
            columnsStudentTable.add("fio varchar(20)");
            columnsStudentTable.add("sex varchar(1)");
            columnsStudentTable.add("id_group int");

            studentTable.createTable(columnsStudentTable);

//Создать таблицу Group Колонки id, name, id_curator

            List<String> columnsGroupTable = new ArrayList<>();
            columnsGroupTable.add("id int primary key");
            columnsGroupTable.add("name varchar(10)");
            columnsGroupTable.add("id_curator int");

            groupTable.createTable(columnsGroupTable);

//Создать таблицу Curator Колонки id, fio

            List<String> columnsCuratorTable = new ArrayList<>();
            columnsCuratorTable.add("id int primary key");
            columnsCuratorTable.add("fio varchar(20)");

            curatorTable.createTable(columnsCuratorTable);

// Заполнить таблицы данными(15 студентов, 3 группы, 4 куратора)

            List<Student> students = new ArrayList<>();
            students.add(new Student(1, "Frodo", 'm', 1));
            students.add(new Student(2, "Sam", 'm', 1));
            students.add(new Student(3, "Merry", 'm', 1));
            students.add(new Student(4, "Pippin", 'm', 1));
            students.add(new Student(5, "Bilbo", 'm', 1));
            students.add(new Student(6, "Vasya", 'm', 2));
            students.add(new Student(7, "Thorin", 'm', 2));
            students.add(new Student(8, "Balin", 'm', 2));
            students.add(new Student(9, "Durin", 'm', 2));
            students.add(new Student(10, "Thror", 'm', 2));
            students.add(new Student(11, "Elrond", 'm', 3));
            students.add(new Student(12, "Legolas", 'm', 3));
            students.add(new Student(13, "Galadriel", 'f', 3));
            students.add(new Student(14, "Arwen", 'f', 3));
            students.add(new Student(15, "Tauriel", 'f', 3));

            List<Group> groups = new ArrayList<>();
            groups.add(new Group(1, "Hobbits", 1));
            groups.add(new Group(2, "Dwarves", 2));
            groups.add(new Group(3, "Elves", 3));

            List<Curator> curators = new ArrayList<>();
            curators.add(new Curator(1, "Gandalf"));
            curators.add(new Curator(2, "Durin"));
            curators.add(new Curator(3, "Elrond"));
            curators.add(new Curator(4, "Aragorn"));

            for (Student student : students) {
                studentTable.insert(student);
            }

            for (Group group : groups) {
                groupTable.insert(group);
            }

            for (Curator curator : curators) {
                curatorTable.insert(curator);
            }

//Вывести на экран информацию о всех студентах включая название группы и имя куратора

            List<String> fullStudentInfo = studentTable.selectDoubleJoin("Student.id, Student.fio, sex, StGroup.name, Curator.fio",
                    "StGroup", "Student.id_group=StGroup.id", "Curator", "StGroup.id_curator=Curator.id");
            System.out.println("Full students info:");
            printInfo(fullStudentInfo);
            printSeparator();

// Вывести на экран количество студентов

            System.out.println("Number of students - " + String.join(",", studentTable.simpleSelect("COUNT(*)")));
            printSeparator();

//Вывести студенток

            List<String> femaleStudents = studentTable.selectWhere("fio", "sex='f'");
            System.out.println("Female students:");
            printInfo(femaleStudents);
            printSeparator();

//Обновить данные по группе сменив куратора

            groupTable.updateTable("id_curator=4", "id_curator=3");

//Вывести список групп с их кураторами

            List<String> groupsInfo = groupTable.selectJoin("StGroup.id, StGroup.name, Curator.fio", "Curator", "StGroup.id_curator=Curator.id");
            System.out.println("Groups with curators");
            printInfo(groupsInfo);
            printSeparator();

//Используя вложенные запросы вывести на экран студентов из определенной группы(поиск по имени группы)

            List<String> chosenGroupStudents = studentTable.selectWhereIn("fio", "Student.id_group", "StGroup.id", "StGroup", "StGroup.name",
                    "'Hobbits'");
            System.out.println("These are my lovely hobbits:");
            printInfo(chosenGroupStudents);

        } finally {
            dbExecutor.close();
        }
    }

    public static void printSeparator() {
        System.out.println("--------------------------------------");
    }

    public static void printInfo(List<String> data) {
        for (String info : data) {
            System.out.println(info);
        }
    }
}
