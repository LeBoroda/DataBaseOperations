package tables;

import db.IDBExecutor;

public class GroupTable extends AbsTable {
    public GroupTable(IDBExecutor dbExecutor) {
        super("StGroup", dbExecutor);
    }
}
