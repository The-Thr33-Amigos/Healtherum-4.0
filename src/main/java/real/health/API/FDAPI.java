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
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import com.fasterxml.jackson.core.*;


public class FDAPI {
    
    private static final String BASE_URL = "https://api.fda.gov/drug/label.json?search=openfda.brand_name:";
    private static final String API_KEY = "&api_key=oAXwjLBrf8zqmPXKh5LhSBBIsvqqzjfQ3NZDZS7Z";
    String test = "http://api.fda.gov/drug/label.json?search=openfda.brand_name:XANAX&api_key=oAXwjLBrf8zqmPXKh5LhSBBIsvqqzjfQ3NZDZS7Z";
    
    private ObjectMapper objectMapper;
    private dosageParse doseParse;
    public Boolean valid = false;

    public FDAPI() {
        objectMapper = new ObjectMapper();
        doseParse = new dosageParse();
    }

    public List<String> getDrugDosages(String drugName) throws IOException {
        
        if (drugName == null || drugName.trim().isEmpty()) {
            return Collections.emptyList();
        }
        if (drugName.trim().length() < 5) {
            return Collections.emptyList();
        }
        String encodedDrugName = URLEncoder.encode(drugName, StandardCharsets.UTF_8.toString());
        String url = BASE_URL + encodedDrugName + API_KEY;
        
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            JsonNode jsonResponse = objectMapper.readTree(reader);

            JsonNode results = jsonResponse.get("results");
            List<String> dosages = new ArrayList<>();
            
            
            
            for (JsonNode result : results) {
                if (result.has("dosage_and_administration")) {
                    JsonNode dosageArray = result.get("dosage_and_administration");
                    for (JsonNode dosageNode : dosageArray) {
                        String dosageText = dosageNode.asText();
                        System.out.println("Dosage Text: " + dosageText);
            
                        if (doseParse.isValidMG(dosageText)) {
                            dosages.addAll(doseParse.drugDose);
                            valid = true;
                        }
                    }
                }
            }

            return dosages;
        } else {
            throw new IOException("Failed to fetch data from FDA API");
        }
    }

}
