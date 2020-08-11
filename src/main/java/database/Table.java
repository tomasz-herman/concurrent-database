package database;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.stream.Stream;

public class Table implements Serializable {

    private final ReadWriteLock writeDumpLock = new ReentrantReadWriteLock();
    private final AtomicLong indexer = new AtomicLong(1);
    private final String tableId;
    private final Map<Long, DataRow> dbCache = new ConcurrentSkipListMap<>();
    private final TableObserver tableObserver = new TableObserver();

    public TableObserver getTableObserver() {
        return tableObserver;
    }

    public Table(String tableId) {
        this.tableId = tableId;
    }

    public DataRow read(Long id){
        DataRow dbRow = dbCache.get(id);
        tableObserver.informListeners("Read data:" + dbRow.getId() + " from row: " + id);
        return dbRow;
    }

    public long write(DataRow data){
        writeDumpLock.readLock().lock();
        long i = indexer.getAndIncrement();
        tableObserver.informListeners("Writing to db: " + data.getId() + " from row: " + i);
        dbCache.put(i, data);
        writeDumpLock.readLock().unlock();
        return i;
    }

    public <T> T readAll(Function<Stream<Map.Entry<Long, DataRow>>, T> callback) {
        writeDumpLock.writeLock().lock();
        T result = callback.apply(dbCache.entrySet().parallelStream());
        writeDumpLock.writeLock().unlock();
        return result;
    }

    public void dump(String path){
        writeDumpLock.writeLock().lock();
        try {
            FileOutputStream fileOut = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
            tableObserver.informListeners("Table saved in: " + path);
        } catch (IOException i) {
            tableObserver.informListeners(i.getMessage());
        }
        writeDumpLock.writeLock().unlock();
    }

    public static Table loadTable(String path){
        Table newTable = null;
        try {
            FileInputStream fileOut = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(fileOut);
            newTable = (Table) in.readObject();
            in.close();
            fileOut.close();
            System.out.println("Table saved in: " + path);
            newTable.writeDumpLock.writeLock().unlock();
        } catch (IOException i) {
            System.out.println(i.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return newTable;
    }
}
