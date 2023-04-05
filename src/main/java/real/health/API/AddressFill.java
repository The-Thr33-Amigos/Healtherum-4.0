package real.health.API;

import javax.swing.JTextField;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class AddressFill extends JTextField {
    private final AutoCompleteListener listener;
    private final JPopupMenu suggestionsPop;
    private String selectedSuggestion;
    private Timer searchTimer;

    public AddressFill(int columns, AutoCompleteListener listener) {
        super(columns);
        this.listener = listener;
        this.suggestionsPop = new JPopupMenu();

        this.searchTimer = new Timer(250 ,new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSuggestions();
            }
        });
        searchTimer.setRepeats(false);

        this.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchTimer.restart();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchTimer.restart();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchTimer.restart();
            }
        });
    }

    public String getSelectedAddress() {
        return selectedSuggestion;
    }

    private void updateSuggestions() {
        String query = this.getText();

        if (query.length() > 4) {
            String jsonResponse = searchGoog(query);

            JSONObject json = new JSONObject(jsonResponse);
            JSONArray results = json.getJSONArray("results");
            List<String> suggestions = new ArrayList<>();

            for (int i = 0; i < results.length(); i++) {
                JSONObject result = results.getJSONObject(i);
                String displayName = result.getString("formatted_address");

                suggestions.add(displayName);
            }

            if (!suggestionsPop.isVisible() && results.length() > 0) {
                showSuggestions(suggestions);
                this.requestFocusInWindow();
            } else if (results.length() == 0) {
                suggestionsPop.setVisible(false);
            }
        } else {
            suggestionsPop.setVisible(false);
        }
    }
    private String searchGoog(String que) {
        String response = "";
        List<String> suggestions = new ArrayList<>();

        try {
            String APIKEY = "AIzaSyAylPDAIokS7YWx8Fu4SVP4-PLi6v7IuIQ";
            String encodedQuery = URLEncoder.encode(que, StandardCharsets.UTF_8);
            String apiUrl = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + encodedQuery + "&key=" + APIKEY + "&maxresults=5&region=us";

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;

                while ((line = reader.readLine()) != null) {
                    response += line;
                }
                
                reader.close();
            } else {
                System.out.println("Error: API Request Failed with code " + responseCode);
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;   
    }

    private void showSuggestions(List<String> suggestions) {
        suggestionsPop.removeAll();

        for (String sug : suggestions) {
            JMenuItem menuItem = new JMenuItem(sug);
            menuItem.addActionListener(e -> {
                setText(sug);
                suggestionsPop.setVisible(false);
            });
            suggestionsPop.add(menuItem);
        }
        if (suggestionsPop.getComponentCount() > 0) {
            suggestionsPop.show(this, 0, getHeight());
        } else {
            suggestionsPop.setVisible(false);
        }

        
    }

    
}
