import java.util.*;

enum SpotStatus { EMPTY, OCCUPIED, DELETED }

class ParkingSpot {

    String plate;
    long entryTime;
    SpotStatus status;

    ParkingSpot(){
        status = SpotStatus.EMPTY;
    }
}

public class ParkingLot {

    ParkingSpot[] table = new ParkingSpot[500];

    public ParkingLot(){

        for(int i=0;i<500;i++)
            table[i] = new ParkingSpot();

    }

    int hash(String plate){

        return Math.abs(plate.hashCode()) % table.length;

    }

    public int parkVehicle(String plate){

        int index = hash(plate);

        int probes = 0;

        while(table[index].status == SpotStatus.OCCUPIED){

            index = (index + 1) % table.length;
            probes++;

        }

        table[index].plate = plate;
        table[index].entryTime = System.currentTimeMillis();
        table[index].status = SpotStatus.OCCUPIED;

        System.out.println("Assigned spot #" + index + " ("+probes+" probes)");

        return index;
    }

    public void exitVehicle(String plate){

        for(int i=0;i<table.length;i++){

            if(table[i].status == SpotStatus.OCCUPIED &&
                    table[i].plate.equals(plate)){

                long duration =
                        (System.currentTimeMillis()-table[i].entryTime)/60000;

                double fee = duration * 0.1;

                table[i].status = SpotStatus.DELETED;

                System.out.println("Spot #"+i+" freed, Fee: $"+fee);

                return;
            }
        }

        System.out.println("Vehicle not found");
    }

    public void getStatistics(){

        int occupied = 0;

        for(ParkingSpot s : table){

            if(s.status == SpotStatus.OCCUPIED)
                occupied++;

        }

        double occupancy = (occupied*100.0)/table.length;

        System.out.println("Occupancy: "+occupancy+"%");
    }

    public static void main(String[] args){

        ParkingLot lot = new ParkingLot();

        lot.parkVehicle("ABC-1234");
        lot.parkVehicle("ABC-1235");
        lot.parkVehicle("XYZ-9999");

        lot.exitVehicle("ABC-1234");

        lot.getStatistics();
    }
}