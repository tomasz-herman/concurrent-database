package database;

import java.util.ArrayList;
import java.util.List;

public class TableObserver {
    private final List<TableListener> listeners = new ArrayList<>();

    public synchronized void informListeners(String msg){
        listeners.forEach(l -> l.callback(msg));
    }

    public void registerListener(TableListener tl){
        listeners.add(tl);
    }

    public void unregisterListener(TableListener tl){
        listeners.remove(tl);
    }
}
