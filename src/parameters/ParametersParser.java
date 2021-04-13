package parameters;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class ParametersParser {
    private static final Parameters defaultParameters = new Parameters(
            50,
            50,
            0.2f,
            1000,
            2,
            500,
            50,
            1000
    );

    public static Parameters getDefaultParameters(){
        return defaultParameters;
    }

    public static Parameters parse(String name) {
        JSONParser jsonParser = new JSONParser();
        FileReader reader;
        Object obj;

        try {
            reader = new FileReader(name);
            obj = jsonParser.parse(reader);
        } catch(IOException | ParseException e ){
            System.out.println("EXCEPTION: ");
            System.out.println(e.getMessage());
            return defaultParameters;
        }

        JSONObject file = (JSONObject)obj;

        long width = (long) file.get("width");
        long height = (long) file.get("height");
        double jungleRatio = (double) file.get("jungleRatio");
        long startEnergy = (long) file.get("startEnergy");
        long moveEnergy = (long) file.get("moveEnergy");
        long plantEnergy = (long) file.get("plantEnergy");
        long numOfAnimals = (long) file.get("numOfAnimals");
        long days = (long) file.get("days");

        Parameters parameters = new Parameters(width, height, jungleRatio, startEnergy, moveEnergy, plantEnergy, numOfAnimals,  days);

        return parameters;
    }

    public static void saveStats(HashMap<String, Double> toSave, String name){
        JSONObject stats = new JSONObject();
        toSave.forEach((key, value) -> {
            stats.put(key, value);
        });

        //Write JSON file
        try (FileWriter file = new FileWriter(name + ".json")) {

            file.write(toSave.toString());
            file.flush();

        } catch (IOException e) {
            System.out.println("EXCEPTION: ");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        System.out.println(stats);
    }


}
