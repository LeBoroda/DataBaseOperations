package data;

public class Group {

    private int id;
    private String name;
    private int idCurator;

    public Group(int id, String name, int idCurator) {
        this.id = id;
        this.name = name;
        this.idCurator = idCurator;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getIdCurator() {
        return idCurator;
    }

    public String toString() {
        return String.format("'%s', '%s', '%s'", getId(), getName(), getIdCurator());
    }
}
