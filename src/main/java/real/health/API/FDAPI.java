package real.health.API;

import com.fasterxml.jackson.databind.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import com.fasterxml.jackson.core.*;


public class FDAPI {
    
    // public static void main(String[] args) throws IOException {
    //     // Open the input and output files
    //     BufferedReader input = new BufferedReader(new FileReader("src/main/java/real/health/API/drugs.txt"));
    //     Writer output = new FileWriter("output.csv");

    //     // Create a CSV printer with the desired format
    //     CSVPrinter csvPrinter = new CSVPrinter(output, CSVFormat.DEFAULT);

    //     // Read each line of the input file and extract the second column
    //     String line;
    //     ArrayList<String> inArr = new ArrayList<>();
    //     while ((line = input.readLine()) != null) {
    //         String[] fields = line.split("\t");
    //         String column2 = fields[5];
    //         if (inArr.contains(column2) == false && column2.length() < 30) {
    //             inArr.add(column2);
    //             csvPrinter.printRecord(column2);
    //         }
            


    //         // Write the second column to the CSV file
            
    //     }

    //     // Close the input and output files
    //     input.close();
    //     output.close();
    // }

}
