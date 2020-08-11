package database;

import java.util.Random;
import java.util.UUID;

public class DataRow {
    private final String id = UUID.randomUUID().toString();
    private final int count = new Random().nextInt(100);

    public String getId() {
        return id;
    }

    public int getCount() {
        return count;
    }
}
