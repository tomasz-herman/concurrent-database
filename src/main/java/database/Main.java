package database;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        Table firstTable = new Table("first");
        for (int i = 0; i < 50; i++) {
            new Thread(new Client(firstTable)).start();
        }
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new TableSerializer(firstTable), 10, 10, TimeUnit.SECONDS);
    }
}
