package database;

import java.util.Random;
import java.util.UUID;

public class DataRow {
    private final String id;
    private final int count;

    public DataRow(){
        this.id = UUID.randomUUID().toString();
        this.count = new Random().nextInt(100);
    }

    public DataRow(String id){
        this.id = id;
        this.count = new Random().nextInt(100);
    }

    public String getId() {
        return id;
    }

    public int getCount() {
        return count;
    }
}
