import HaversineFormula.HaversineDistance;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class Route {

    private String Source_AirportCode;
    private String Destination_AirportCode;
    private String AirlineCode;
    private String Stops;

    public String getSource_AirportCode() {
        return Source_AirportCode;
    }

    public String getDestination_AirportCode() {
        return Destination_AirportCode;
    }

    public String getAirlineCode() {
        return AirlineCode;
    }

    public String getStops() {
        return Stops;
    }

    public static HashMap<String, ArrayList<String>> AirRoutesMap = new HashMap<>();
    public static HashMap<ArrayList<String>, ArrayList<String>> AirlineRoutesMap = new HashMap<>();


    public static HashMap<String, ArrayList<String>> AirRouteFileReader(String filename){

        Scanner inputStream;
        // check if file exists
        try{
            inputStream = new Scanner(new FileInputStream(filename));
            String streamline;
            String []splitStream;

            while(inputStream.hasNext()){
                streamline = inputStream.nextLine();
                // split streamline by commas into array
                splitStream = streamline.split(",");

                String routeKey = splitStream[2];  //source airport code

                ArrayList<String> routeList;
                if(AirRoutesMap.containsKey(routeKey)){
                    // key already in map
                    routeList = AirRoutesMap.get(routeKey);
//                    System.out.println("key: " + routeKey);
//                    System.out.println("list: "+routeList);
                    AirRoutesMap.remove(routeKey, AirRoutesMap.get(routeKey));
                }
                else{
                    // key not in the map
                    routeList = new ArrayList<>();
                }
                routeList.add(splitStream[4]);  // destination airport code
                AirRoutesMap.put(routeKey, routeList);

            }
            inputStream.close();
            System.out.println("> Airport-Route map created...");

        }catch (FileNotFoundException e){
            System.out.println("> error opening file: check that the input file is in the right directory and the " +
                    "given file name matches");
        }

        return AirRoutesMap;
    }



    public static HashMap<ArrayList<String>, ArrayList<String>> AirlineRoutesFileReader(String filename){
        Scanner inputStream;
        // check if file exists
        try{
            inputStream = new Scanner(new FileInputStream(filename));
            String streamline;
            String []splitStream;

            while(inputStream.hasNext()){
                streamline = inputStream.nextLine();
                // split streamline by commas into array
                splitStream = streamline.split(",");

                ArrayList<String> routeKey = new ArrayList<>();
                routeKey.add(splitStream[2]);  // source airport code
                routeKey.add(splitStream[4]);  // destination airport code
                routeKey.add(splitStream[7]);  // stops


                ArrayList<String> routeList;
                if(AirlineRoutesMap.containsKey(routeKey)){
                    // key already in map
                    routeList = AirlineRoutesMap.get(routeKey);
//                    System.out.println("key: " + routeKey);
//                    System.out.println("list: "+routeList);
                    AirlineRoutesMap.remove(routeKey, AirlineRoutesMap.get(routeKey));
                }
                else{
                    // key not in the map
                    routeList = new ArrayList<>();
                }
                routeList.add(splitStream[0]);   //airline code
                AirlineRoutesMap.put(routeKey, routeList);

            }
            inputStream.close();
            System.out.println("> Airline-Route map created...");

        }catch (FileNotFoundException e){
            System.out.println("> error opening file: check that the input file is in the right directory and the " +
                    "given file name matches");
        }

        return AirlineRoutesMap;
    }


    /*
    * @Overload
    * */
    public static void printMap2(HashMap<String, ArrayList<String>> thisMap){
        int i = 0;
        for(String key : thisMap.keySet()){
            System.out.println(i + " " + key + " >> " + thisMap.get(key));
//            System.out.println(" ");
            i++;
        }
    }


    public static void printMap(HashMap<ArrayList<String>, ArrayList<String>> thisMap){
        int i = 0;
        for(ArrayList<String> key : thisMap.keySet()){
            System.out.println(i + ":  " + key + " >> " + thisMap.get(key).toString());
//            System.out.println(" ");
            i++;
        }
    }

    public static void findRoute(String start, String goal){

        Queue<String> frontier = new ArrayDeque<>();  // frontier
        ArrayList<String> explored_set = new ArrayList<>();  // explored set
        ArrayList<String> successors;  // successor states
        ArrayList<String> solution_path;  // solution path
        HashMap<String, String> ancestors = new HashMap<>();  // child-parent map

        System.out.println("     >>> start airport: " + start);
        System.out.println("     >>> goal airport: " + goal);
        System.out.println("       >>>> searching... ");

        // add root/start to frontier
        frontier.add(start);

        while(!(frontier.isEmpty())){
            // remove node from frontier
            String node = frontier.remove();
//            System.out.println("current frontier: " + frontier);
            // add node to explored set
            explored_set.add(node);
//            System.out.println("explored set: " + explored_set);

            // goal test  *goal test at node selection, before expansion*

            // generate successors
            successors = AirRoutesMap.get(node);
//            System.out.println(node + " >> " + successors + "\n");
            if(successors != null){
                for(int i = 0; i < successors.size(); i++){
                    if(!frontier.contains(successors.get(i)) && (!explored_set.contains(successors.get(i)))){
                        String child = successors.get(i);
                        ancestors.putIfAbsent(child, node);
                        // goal test  *goal test at child generation, after expansion*
                        if(child.equals(goal)){
                            System.out.println("         >>>> found goal: " + child);
                            solution_path = solutionPath(ancestors, child, start);
                            System.out.println("     >>> solution path ~ " + solution_path + " ~");
                            System.out.println("     >>> total distance ~ " + haversineHelper(solution_path) + " km ~");

                            return;
                        }
                        frontier.add(child);
                    }
                }
            }
            else{
                System.out.println("           - DeadEnd: no routes from current node " + node);
            }
        }
    }
    
    public static ArrayList<String> solutionPath(HashMap<String, String> thisMap, String goal, String start){

        ArrayList<String> path_from_goal = new ArrayList<>();
        ArrayList<String> solution_path = new ArrayList<>();
//        System.out.println(thisMap);
        path_from_goal.add(goal);
        while(thisMap.get(goal) != null){
            String node = thisMap.get(goal);
            path_from_goal.add(node);
            goal = node;
        }
//        System.out.println(path_from_goal);
        for(int i = path_from_goal.size()-1; i < path_from_goal.size() && i != -1; i--){
            solution_path.add(path_from_goal.get(i));
        }
        return solution_path;
    }


    public static double haversineHelper(ArrayList<String> this_solution_path){

        double latitude_A;
        double longitude_A;
        double latitude_B;
        double longitude_B;

        Airport airport_A = new Airport();
        Airport airport_B = new Airport();

        double haversine_distance = 0;
        double distance = 0;
        Set<ArrayList<String>> all_keys = Airport.AirportMap.keySet();

        for(int i = 0; i < this_solution_path.size()-1; i++){

            String pointA = this_solution_path.get(i);
            String pointB = this_solution_path.get(i+1);

            for(ArrayList<String> key : all_keys){
                if(key.get(0).equals(pointA))
                    airport_A = Airport.AirportMap.get(key);

                if(key.get(0).equals(pointB))
                    airport_B = Airport.AirportMap.get(key);
            }

            latitude_A = Double.parseDouble(airport_A.getLatitude());
            longitude_A = Double.parseDouble(airport_A.getLongitude());
            latitude_B = Double.parseDouble(airport_B.getLatitude());
            longitude_B = Double.parseDouble(airport_B.getLongitude());

            distance = HaversineDistance.haversine(latitude_A, longitude_A, latitude_B, longitude_B);
            haversine_distance = haversine_distance + distance;
        }
        return haversine_distance;
    }


    public static void main(String [] args){

        System.out.println(" ");

        AirRoutesMap = AirRouteFileReader("/Users/admin/Library/CloudStorage/OneDrive-AshesiUniversity/project_code/projects/Airliner Apps/Airliner Java/src/routes copy.csv");
//        printMap2(AirRoutesMap);

        AirlineRoutesMap = AirlineRoutesFileReader("/Users/admin/Library/CloudStorage/OneDrive-AshesiUniversity/project_code/projects/Airliner Apps/Airliner Java/src/routes copy.csv");
//        printMap(AirlineRoutesMap);

        findRoute("ACC", "GKA");



    }
}
