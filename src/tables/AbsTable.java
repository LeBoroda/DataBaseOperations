package tables;

import db.IDBExecutor;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbsTable {
    private final String tableName;
    private final IDBExecutor dbExecutor;
    private String request;

    public AbsTable(String name, IDBExecutor dbExecutor) {
        this.tableName = name;
        this.dbExecutor = dbExecutor;
    }

    public void createTable(List<String> columns) {
        deleteTable();
        dbExecutor.execute(String.format("CREATE TABLE IF NOT EXISTS %s (%s);", tableName, String.join(",", columns)));
    }

    public void insert(Object obj) {
        dbExecutor.execute(String.format("INSERT INTO %s (%s) VALUES (%s);", tableName, getTableColumns(), obj.toString()));
    }

    public void deleteTable() {
        dbExecutor.execute(String.format("DROP TABLE IF EXISTS %s;", tableName));
    }

    public void updateTable(String newData, String oldData) {
        dbExecutor.execute(String.format("UPDATE %s SET %s WHERE %s", tableName, newData, oldData));
    }

    public List<String> simpleSelect(String result) {
        request = String.format("SELECT %s FROM %s;", result, tableName);
        return getTableData(request);
    }

    public List<String> selectWhere(String result, String condition) {
        request = String.format("SELECT %s FROM %s WHERE %s;", result, tableName, condition);
        return getTableData(request);
    }

    public List<String> selectJoin(String result, String joinTable, String joinCondition) {
        request = String.format("SELECT %s FROM %s JOIN %s ON %s;", result, tableName, joinTable, joinCondition);
        return getTableData(request);
    }

    public List<String> selectDoubleJoin(String result, String firstJoinTable, String firstJoinCondition, String secondJoinTable, String secondJoinCondition) {
        request = String.format("SELECT %s FROM %s JOIN %s ON %s JOIN %s ON %s;",
                result, tableName, firstJoinTable, firstJoinCondition, secondJoinTable, secondJoinCondition);
        return getTableData(request);
    }

    public List<String> selectWhereIn(String result, String searchField1, String anotherResult, String anotherTable, String searchField2, String condition) {
        request = String.format("SELECT %s FROM %s WHERE %s IN(SELECT %s FROM %s WHERE %s IN(%s));",
                result, tableName, searchField1, anotherResult, anotherTable, searchField2, condition);
        return getTableData(request);
    }

    private String getTableColumns() {
        ResultSet resultSet = dbExecutor.executeQuery(String.format("SELECT * FROM %s;", tableName));
        try {
            List<String> columns = new ArrayList<>();
            ResultSetMetaData rmd = resultSet.getMetaData();
            int numberOfColumns = rmd.getColumnCount();
            for (int i = 1; i <= numberOfColumns; i++) {
                columns.add(rmd.getColumnLabel(i));
            }
            return String.join(",", columns);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> getTableData(String request) {
        ResultSet resultSet = dbExecutor.executeQuery(request);
        List<String> result = new ArrayList<>();
        try {
            ResultSetMetaData rmd = resultSet.getMetaData();
            int numberOfColumns = rmd.getColumnCount();
            while (resultSet.next()) {
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i <= numberOfColumns; i++) {
                    sb.append(resultSet.getString(i)).append(" ");
                }
                result.add(sb.toString());

            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

}
