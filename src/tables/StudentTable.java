package tables;

import db.IDBExecutor;

public class StudentTable extends AbsTable {
    public StudentTable(IDBExecutor dbExecutor) {
        super("Student", dbExecutor);
    }
}
