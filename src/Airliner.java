public class Airliner {

    public static void main(String[]args){

        String route_file = "/Users/admin/Library/CloudStorage/OneDrive-AshesiUniversity" +
                "/project_code" +
                "/projects/Airliner Apps/Airliner Java/src/routes copy.csv";

        Airport.AirportMap = Airport.AirportFileReader("/Users/admin/Library/CloudStorage/OneDrive-AshesiUniversity" +
                "/project_code" +
                "/projects/Airliner Apps/Airliner Java/src/airports copy.csv");
//        Airport.printMap(Airport.AirportMap);

        Route.AirRoutesMap = Route.AirRouteFileReader(route_file);
//        printMap2(AirRoutesMap);

        Route.AirlineRoutesMap = Route.AirlineRoutesFileReader(route_file);
//        printMap(AirlineRoutesMap);

        ReadWrite.InputFileReader("/Users/admin/Library/CloudStorage/OneDrive-AshesiUniversity/project_code/projects/Airliner Apps/Airliner Java/src/accra_to_kazan.txt");
    }
}
