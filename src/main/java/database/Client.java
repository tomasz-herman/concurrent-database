package database;

public class Client implements Runnable{
    private Table currentTable;

    public void run() {

    }

    public void setTable(Table table){
        this.currentTable = table;
    }

    public void writeToTable(DataRow dataRow){
        currentTable.write(dataRow);
    }

    public DataRow readFromTable(Long id){
        return currentTable.read(id);
    }
}
