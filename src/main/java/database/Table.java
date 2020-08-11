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
//        mapLock.readLock().lock();
        DataRow dbRow = dbCache.get(id);
        System.out.println("Read data:" + dbRow.getId() + " from row: " + id);
//        mapLock.readLock().unlock();
        return dbRow;
    }

    public long write(DataRow data){
        mapLock.readLock().lock();
        long i = indexer.getAndIncrement();
        System.out.println("Writing to db: " + data.getId() + " from row: " + i);
        dbCache.put(i, data);
        mapLock.readLock().unlock();
        return i;
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
