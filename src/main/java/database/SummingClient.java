package database;

public class SummingClient extends Client {

    public SummingClient(Table currentTable) {
        super(currentTable);
    }

    @Override
    public void run() {
        while(true){
            sleep(1000);
            int sum = currentTable.readAll(stream -> stream.mapToInt(e -> e.getValue().getCount()).sum());
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% The sum is: " + sum);
        }
    }
}
