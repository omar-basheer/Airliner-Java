import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;

public class ReadWrite {

    public static ArrayList<String> getAirportIATA(String[] location){

        Set<ArrayList<String>> all_keys = Airport.AirportMap.keySet();
        ArrayList<String> all_IATAs = new ArrayList<String>();

        for(ArrayList<String> key : all_keys){
            if(location[0].equals(key.get(1)) && location[1].equals(key.get(2))){
                String iata = key.get(0);
                all_IATAs.add(iata);
            }
        }
        return all_IATAs;
    }


     public static void InputFileReader(String filename){

         Scanner inputStream;
         try {
             inputStream = new Scanner(new FileReader(filename));

             String streamline;
             String[] start_location;
             String[] dest_location;
             ArrayList<String> all_starts;
             ArrayList<String> all_goals;

             streamline = inputStream.nextLine();
             start_location = streamline.split(", ");
             all_starts = getAirportIATA(start_location);
             System.out.println("   >> all starts: " + all_starts);

             streamline = inputStream.nextLine();
             dest_location = streamline.split(", ");
             all_goals = getAirportIATA(dest_location);
             System.out.println("   >> all goals: " + all_goals);

//             System.out.println("   >> all starts: " + all_starts);
//             System.out.println("   >> all goals: " + all_goals);
//             System.out.println("               *path " + path_count + "*");
//             System.out.println("     >>> start airport: " + start);
//             System.out.println("     >>> goal airport: " + goal);
//             System.out.println("       >>>> searching... ");
//             System.out.println("           - DeadEnd: no routes from current node " + node);
//             System.out.println("         >>>> found goal: " + child);
//             System.out.println("     >>> solution path ~ " + solution_path + " ~");

             int path_count = 1;
             for(int i = 0; i < all_starts.size(); i++){
                 for(int j = 0; j < all_goals.size(); j++){
                     System.out.println(" ");
                     System.out.println("               *path " + path_count + "*");
                     Route.findRoute(all_starts.get(i), all_goals.get(j));
                     path_count++;
                 }
             }


         }catch (FileNotFoundException e){
             System.out.println("error opening file: check that file is in the given directory and file name mathces");
         }

     }
}
