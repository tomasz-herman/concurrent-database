package database;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        Table firstTable = new Table("first");
        firstTable.getTableObserver().registerListener(new Logger());
        for (int i = 0; i < 50; i++) {
            new Thread(new Client(firstTable)).start();
        }
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new TableSerializer(firstTable), 10, 10, TimeUnit.SECONDS);
        new Thread(new SummingClient(firstTable)).start();
        WindowBuilder builder = new WindowBuilder();
        Viewer viewer = new Viewer(firstTable);
        builder.setContentPane(viewer.getMainPane())
                .setSize(1280, 720)
                .setPreferredSize(1280, 720)
                .buildFrame();
    }
}
