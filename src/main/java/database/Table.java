package database;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Table implements Serializable {

    private final ReadWriteLock mapLock = new ReentrantReadWriteLock();
    private final AtomicLong indexer = new AtomicLong(1);
    private final String tableId;
    private final Map<Long, DataRow> dbCache = new ConcurrentSkipListMap<>();

    public Table(String tableId) {
        this.tableId = tableId;
    }

    public DataRow read(Long id){
        mapLock.readLock().lock();
        DataRow dbRow = dbCache.get(id);
        mapLock.readLock().unlock();
        return dbRow;
    }

    public void write(DataRow data){
        mapLock.readLock().lock();
        dbCache.put(indexer.getAndIncrement(), data);
        mapLock.readLock().unlock();
    }

    public synchronized void dump(String path){
        mapLock.writeLock().lock();
        try {
            FileOutputStream fileOut = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
            System.out.println("Table saved in: " + path);
        } catch (IOException i) {
            System.out.println(i.getMessage());
        }
        mapLock.writeLock().unlock();
    }

    public static synchronized Table loadTable(String path){
        Table newTable = null;
        try {
            FileInputStream fileOut = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(fileOut);
            newTable = (Table) in.readObject();
            in.close();
            fileOut.close();
            System.out.println("Table saved in: " + path);
            newTable.mapLock.writeLock().unlock();
        } catch (IOException i) {
            System.out.println(i.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return newTable;
    }
}
