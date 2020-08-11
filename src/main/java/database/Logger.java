package database;

public class Logger implements TableListener{
    @Override
    public void callback(String msg) {
        System.out.println(msg);
    }
}
