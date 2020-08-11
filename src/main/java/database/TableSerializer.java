package database;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class TableSerializer implements Runnable {
    private final Table scheduledTable;

    public TableSerializer(Table firstTable) {
        this.scheduledTable = firstTable;
    }

    public void run() {
        try {
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ creating dump");
            scheduledTable.dump(File.createTempFile("temp", "test").getAbsolutePath());
            System.out.println("############################################ dump created");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
