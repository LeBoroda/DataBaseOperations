package data;

public class Student {
    private int id;
    private String fio;
    private char sex;
    private int idGroup;

    public Student(int id, String fio, char sex, int idGroup) {
        this.id = id;
        this.fio = fio;
        this.sex = sex;
        this.idGroup = idGroup;
    }

    public int getId() {
        return id;
    }

    public String getFio() {
        return fio;
    }

    public char getSex() {
        return sex;
    }

    public int getIdGroup() {
        return idGroup;
    }

    public String toString(){
        return String.format("'%s', '%s', '%s', '%s'", getId(), getFio(), getSex(), getIdGroup());
    }
}
