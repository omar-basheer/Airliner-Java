import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class Airport {

    private String AirportName;  // name of an airport
    private String AirportCity;   // city in which airport is located
    private String Country;   // country in which airport is located
    private String IATA_Code;   // iata code of airport
    private String Latitude;   // airport latitude
    private String Longitude;  // airport longitude


    /*
    * Default airport constructor
    * */
    public Airport(){
        this.AirportName = "";
        this.AirportCity = "";
        this.Country = "";
        this.IATA_Code = "";
        this.Latitude = "";
        this.Longitude = "";
    }

    /**
     * Constructor for an airport object
     * */
    public Airport(String airportName,
                   String airportCity,
                   String country,
                   String iataCode,
                   String latitude,
                   String longitude){
        this.AirportName = airportName;
        this.AirportCity = airportCity;
        this.Country = country;
        this.IATA_Code = iataCode;
        this.Latitude = latitude;
        this.Longitude = longitude;
    }

    public String getAirportName() {
        return AirportName;
    }

    public String getAirportCity() {
        return AirportCity;
    }

    public String getCountry() {
        return Country;
    }

    public String getIATA_Code() {
        return IATA_Code;
    }

    public String getLatitude() {
        return Latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    @Override
    public boolean equals(Object o){
        if(o == null)
            return false;
        else if(getClass() != o.getClass())
            return false;
        Airport airport = (Airport) o;
        return AirportName.equals(airport.getAirportName())
                && AirportCity.equals(airport.getAirportCity())
                && Country.equals(airport.getCountry())
                && IATA_Code.equals(airport.getIATA_Code())
                && Latitude.equals(airport.getLatitude())
                && Longitude.equals(airport.getLongitude());


    }

    @Override
    public String toString(){
        return "Airport - [" + getAirportName() + ", " + getAirportCity() + ", " + getCountry() + ", " + getIATA_Code() + ", " + getLatitude() + ", " + getLongitude() + "]";
    }


    public static HashMap<ArrayList<String>, Airport> AirportMap = new HashMap<>();

    public static HashMap<ArrayList<String>, Airport> AirportFileReader(String filename){

        Scanner inputStream;
        // check if file exists
        try{
            inputStream = new Scanner(new FileInputStream(filename));
            String streamline;
            String [] splitStream;

            while(inputStream.hasNext()){
                streamline = inputStream.nextLine();
                // split streamline by commas into array
                splitStream = streamline.split(",");

                if(!(splitStream[4].equals("\\N"))){
                    ArrayList<String> airportKey = new ArrayList<>();
                    airportKey.add(splitStream[4]);  // iata code
                    airportKey.add(splitStream[2]);  // city
                    airportKey.add(splitStream[3]);  // country

                    Airport thisAirport = new Airport(splitStream[1], splitStream[2], splitStream[3], splitStream[4], splitStream[6], splitStream[7]);

                    AirportMap.putIfAbsent(airportKey, thisAirport);
                }
            }
            inputStream.close();
            System.out.println("> Airports map created...");

        }catch (FileNotFoundException e){
            System.out.println("error opening file: check that the input file is in the right directory and the given file name matches");
        }

        return AirportMap;
    }

    public static void printMap(HashMap<ArrayList<String>, Airport> thisMap){
        int i = 0;
        for(ArrayList<String> key : thisMap.keySet()){
            System.out.println(i + " " + key + " >> " + thisMap.get(key).toString());
            System.out.println(" ");
            i++;
        }
    }


    public static void main(String [] args){

        AirportMap = AirportFileReader("/Users/admin/Library/CloudStorage/OneDrive-AshesiUniversity/project_code/projects/Airliner Apps/Airliner Java/src/airports copy.csv");
        printMap(AirportMap);

    }


}
