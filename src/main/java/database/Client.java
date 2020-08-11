package database;

import java.util.Random;

public class Client implements Runnable{
    private Table currentTable;
    private Random r = new Random();


    public Client(Table currentTable) {
        this.currentTable = currentTable;
    }

    public void run() {
        while(true){
            long x = writeToTable(new DataRow());
            sleep(r.nextInt(50));
            System.out.println(readFromTable(x).getId());
            sleep(r.nextInt(50));
        }
    }

    public void setTable(Table table){
        this.currentTable = table;
    }

    public long writeToTable(DataRow dataRow){
        return currentTable.write(dataRow);
    }

    public DataRow readFromTable(Long id){
        return currentTable.read(id);
    }


    private void sleep(int len){
        try {
            Thread.sleep(len);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
