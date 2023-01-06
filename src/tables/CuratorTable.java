package tables;

import db.IDBExecutor;

public class CuratorTable extends AbsTable {
    public CuratorTable(IDBExecutor dbExecutor) {
        super("Curator", dbExecutor);
    }
}
