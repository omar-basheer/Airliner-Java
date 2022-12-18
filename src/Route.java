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
            System.out.println("error opening file: check that the input file is in the right directory and the given file name matches");
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
            System.out.println("> Airport-Route map created...");

        }catch (FileNotFoundException e){
            System.out.println("error opening file: check that the input file is in the right directory and the given file name matches");
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

    public static HashMap<String, String> findRoute(String start, String goal){

        Queue<String> frontier = new ArrayDeque<>();  // frontier
        ArrayList<String> explored_set = new ArrayList<>();  // explored set
        ArrayList<String> successors;  // successor states
        HashMap<String, String> ancestors = new HashMap<>();  // child-parent map

        System.out.println("  >> start airport: " + start);
        System.out.println("  >> goal airport: " + goal);
        System.out.println("     >>> searching...");

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
            System.out.println(node + " >> " + successors + "\n");
            if(!successors.isEmpty()){
                for(int i = 0; i < successors.size(); i++){
                    if(!frontier.contains(successors.get(i)) && (!explored_set.contains(successors.get(i)))){
                        String child = successors.get(i);
                        ancestors.putIfAbsent(node, child);
                        // goal test  *goal test at child generation, after expansion*
                        if(child.equals(goal)){
                            System.out.println("found goal: " + child);
                            return ancestors;
                        }
                        frontier.add(child);
                    }
                }
            }
            else{
                System.out.println("DeadEnd: no routes from current node " + node);
            }
        }
        return null;
    }

    public static void main(String [] args){

        AirRoutesMap = AirRouteFileReader("/Users/admin/Library/CloudStorage/OneDrive-AshesiUniversity/project_code/projects/Airliner Apps/Airliner Java/src/routes copy.csv");
//        printMap2(AirRoutesMap);

        AirlineRoutesMap = AirlineRoutesFileReader("/Users/admin/Library/CloudStorage/OneDrive-AshesiUniversity/project_code/projects/Airliner Apps/Airliner Java/src/routes copy.csv");
//        printMap(AirlineRoutesMap);

        findRoute("YYZ", "ACC");



    }
}
