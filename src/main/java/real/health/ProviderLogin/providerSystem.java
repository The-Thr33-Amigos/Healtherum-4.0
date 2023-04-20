package real.health.ProviderLogin;

import real.health.ProviderLogin.DateLabelFormatter;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.JFormattedTextField;
import java.text.ParseException;
import java.util.Calendar;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import real.health.GUI.UserRole;
import real.health.PatientLogin.patientInformationSystem;
import real.health.SQL.HealthConn;

public class providerSystem {

    private static JList<Appointment> appointmentsList;
    private static JTable appointmentsTable;

    public static class Appointment {
        private String date;
        private String time;
        private String type;
        private String provider;
        private String status;
        private String patientId;

        public Appointment(String date, String time, String type, String provider, String status, String patientId) {
            this.date = date;
            this.time = time;
            this.type = type;
            this.provider = provider;
            this.status = status;
            this.patientId = patientId;
        }

        public String getDate() {
            return date;
        }

        public String getTime() {
            return time;
        }

        public String getType() {
            return type;
        }

        public String getProvider() {
            return provider;
        }

        public String getStatus() {
            return status;
        }

        public String getPatientId() {
            return patientId;
        }

    }

    public static class User {
        private String id;
        private static String firstName;
        private String lastName;
        private String bDate;
        private String phone;
        private String address;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getBdate() {
            return bDate;
        }

        public void setBdate(String bDate) {
            this.bDate = bDate;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }

    public static void initialize(String id) {
        // Initialize the home screen
        JFrame homeScreen = new JFrame("Provider Home");
        homeScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        homeScreen.setSize(800, 600);
        homeScreen.setLocationRelativeTo(null);

        // Create a panel for the home screen
        JPanel mainPanel = new JPanel(new BorderLayout());
        homeScreen.add(mainPanel);

        // Create a panel for the navigation buttons and add it to the main panel
        JPanel navigationPanel = new JPanel();
        mainPanel.add(navigationPanel, BorderLayout.PAGE_END);
        createNavigationButtons(navigationPanel);

        // Create a panel for the appointments list and add it to the main panel
        JPanel appointmentsPanel = new JPanel(new BorderLayout());
        mainPanel.add(appointmentsPanel, BorderLayout.WEST);
        createAppointmentsList(appointmentsPanel, id, "123");

        // Create a panel for the calendar and add it to the main panel
        JPanel calendarPanel = new JPanel(new BorderLayout());
        mainPanel.add(calendarPanel, BorderLayout.EAST);
        createCalendar(calendarPanel, id, getAppointments(id, "123"), "123");

        // Create a panel for the patient search and add it to the main panel
        JPanel searchPanel = new JPanel(new BorderLayout());
        mainPanel.add(searchPanel, BorderLayout.PAGE_START);
        createPatientSearch(searchPanel);

        // Display the home screen
        homeScreen.setVisible(true);
    }

    private static void createAppointmentsList(JPanel panel, String id, String providerName) {
        // Add components for the daily appointments list
        JLabel appointmentsLabel = new JLabel("Today's Appointments");
        appointmentsLabel.setFont(new Font("Serif", Font.BOLD, 18));
        panel.add(appointmentsLabel);

        // Create a table model for the appointments list
        DefaultTableModel tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableModel.addColumn("Time");
        tableModel.addColumn("Patient");
        tableModel.addColumn("Reason");

        // Set the current date as the selected date
        Date selectedDate = new Date();

        // Fetches appointments from the database
        List<Appointment> allAppointments = getAppointments(id, providerName);
        List<Appointment> appointments = filterAppointmentsByDate(allAppointments, selectedDate);

        for (Appointment appointment : appointments) {
            User patient = getUserById(appointment.getPatientId());
            String patientName = patient.getFirstName() + " " + patient.getLastName();
            tableModel.addRow(new Object[] { appointment.getTime(), patientName, appointment.getStatus() });
        }

        appointmentsTable = new JTable(tableModel);
        JScrollPane appointmentsScrollPane = new JScrollPane(appointmentsTable);
        appointmentsScrollPane.setPreferredSize(new Dimension(600, 300));
        panel.add(appointmentsScrollPane);
    }

    // Add this method to fetch appointments from the database
    private static List<Appointment> getAppointments(String id, String providerName) {
        List<Appointment> appointments = new ArrayList<>();

        try {
            HealthConn newConnection = new HealthConn();
            Connection con = newConnection.connect();
            
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedSelectedDate = dtf.format(LocalDate.now());

            String sql = "SELECT appointment_date, appointment_time, appointment_type, provider, status, id FROM appointments WHERE provider = ? AND appointment_date = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, providerName);
            statement.setString(2, formattedSelectedDate);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String date = resultSet.getString("appointment_date");
                String time = resultSet.getString("appointment_time");
                String type = resultSet.getString("appointment_type");
                String provider = resultSet.getString("provider");
                String status = resultSet.getString("status");
                String patientId = resultSet.getString("id"); 


                appointments.add(new Appointment(date, time, type, provider, status, patientId));
            }

            resultSet.close();
            statement.close();
            con.close();

        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Error: unable to connect to MySQL database");
            ex.printStackTrace();
        }

        return appointments;
    }

    private static void createCalendar(JPanel panel, String id, List<Appointment> initialAppointments, String providerName) {
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

        datePicker.getModel().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("value")) {
                    Date selectedDate = (Date) datePicker.getModel().getValue();
                    if (selectedDate != null) {
                        // Fetch appointments for the selected date and provider
                        List<Appointment> appointmentsForSelectedDate = getAppointmentsForSelectedDateAndProvider(providerName, selectedDate);
                        updateAppointmentsList(appointmentsForSelectedDate);
                    }
                }
            }
        });

        panel.add(datePicker);
    }

    // Add a method to fetch appointments from the database for the selected date
    // and provider
    private static List<Appointment> getAppointmentsForSelectedDateAndProvider(String providerName, Date selectedDate) {
        List<Appointment> appointments = new ArrayList<>();
    
        try {
            HealthConn newConnection = new HealthConn();
            Connection con = newConnection.connect();
    
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String formattedSelectedDate = sdf.format(selectedDate);
        
            String sql = "SELECT appointment_date, appointment_time, appointment_type, provider, status, id FROM appointments WHERE provider = ? AND appointment_date = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, providerName);
            statement.setString(2, formattedSelectedDate);
            ResultSet resultSet = statement.executeQuery();
    
            while (resultSet.next()) {
                String date = resultSet.getString("appointment_date");
                String time = resultSet.getString("appointment_time");
                String type = resultSet.getString("appointment_type");
                String provider = resultSet.getString("provider");
                String status = resultSet.getString("status");
                String patientId = resultSet.getString("id"); 
    
                appointments.add(new Appointment(date, time, type, provider, status, patientId));
            }
    
            resultSet.close();
            statement.close();
            con.close();
    
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Error: unable to connect to MySQL database");
            ex.printStackTrace();
        }
    
        return appointments;
    }

    // Add a method to update the appointments list based on the selected date
    private static void updateAppointmentsList(List<Appointment> appointments) {
        DefaultTableModel tableModel = (DefaultTableModel) appointmentsTable.getModel();
    
        // 1. Clear the current appointments table
        tableModel.setRowCount(0);

        // 2. Update the appointments table with the new data
        for (Appointment appointment : appointments) {
            User patient = getUserById(appointment.getPatientId());
            String patientName = patient.getFirstName() + " " + patient.getLastName();
            tableModel.addRow(new Object[]{appointment.getTime(), patientName, appointment.getStatus()});
        }
    }

    private static List<Appointment> filterAppointmentsByDate(List<Appointment> allAppointments, Date selectedDate) {
        if (selectedDate == null) {
            return new ArrayList<>();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedSelectedDate = sdf.format(selectedDate);

        List<Appointment> filteredAppointments = new ArrayList<>();

        for (Appointment appointment : allAppointments) {
            if (formattedSelectedDate.equals(appointment.getDate())) {
                filteredAppointments.add(appointment);
            }
        }

        return filteredAppointments;
    }

    private static User getUserById(String userId) {
        User user = new User();
    
        try {
            HealthConn newConnection = new HealthConn();
            Connection con = newConnection.connect();
    
            String sql = "SELECT * FROM basic WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, userId);
            ResultSet resultSet = statement.executeQuery();
    
            if (resultSet.next()) {
                user.setId(resultSet.getString("id"));
                user.setFirstName(resultSet.getString("firstName"));
                user.setLastName(resultSet.getString("lastName"));
                user.setBdate(resultSet.getString("bdate"));
                user.setPhone(resultSet.getString("phone"));
                user.setAddress(resultSet.getString("mailing"));
            }
    
            resultSet.close();
            statement.close();
            con.close();
    
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Error: unable to connect to MySQL database");
            ex.printStackTrace();
        }
    
        return user;
    }

    private static void createNavigationButtons(JPanel panel) {
        // Add quick navigation buttons for prescription management, referral
        // management, messaging, and analytics/reporting
        JButton prescriptionsButton = new JButton("Prescriptions");
        JButton referralsButton = new JButton("Referrals");
        JButton messagingButton = new JButton("Messaging");
        JButton analyticsButton = new JButton("Analytics");

        panel.add(prescriptionsButton);
        panel.add(referralsButton);
        panel.add(messagingButton);
        panel.add(analyticsButton);
    }

    private static void createPatientSearch(JPanel panel) {
        // Create a new panel with FlowLayout for the search components
        JPanel searchComponentsPanel = new JPanel(new FlowLayout());
        panel.add(searchComponentsPanel, BorderLayout.NORTH);

        // Create a search label
        JLabel searchLabel = new JLabel("Search Patient:");
        searchComponentsPanel.add(searchLabel);

        // Create a search text field
        JTextField searchField = new JTextField(20);
        searchComponentsPanel.add(searchField);

        // Create a search button
        JButton searchButton = new JButton("Search");
        searchComponentsPanel.add(searchButton);

        // Set up action listener for the search button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = searchField.getText();
                if (!searchText.isEmpty()) {
                    try {
                        // Search for the patient in the database
                        List<User> users = searchPatients(searchText);

                        if (!users.isEmpty()) {
                            // Display a window with the search results
                            displaySearchResults(users);
                        } else {
                            JOptionPane.showMessageDialog(panel, "No patients found with the given search criteria.",
                                    "Search Result", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        // Handle database errors
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(panel, "An error occurred while searching for patients.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    } catch (ClassNotFoundException ex) {
                        // Handle ClassNotFoundException
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(panel, "An error occurred while connecting to the database.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    // Prepare a patient search query
    private static List<User> searchPatients(String searchText) throws SQLException, ClassNotFoundException {
        List<User> users = new ArrayList<>();

        // Use the HealthConn class to establish a connection
        HealthConn newConnection = new HealthConn();
        Connection connection = newConnection.connect();

        // Prepare a patient search query
        String query = "SELECT id, firstName, lastName, bdate, phone, mailing FROM basic WHERE firstName LIKE ? OR lastName LIKE ? OR bdate LIKE ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            String searchPattern = "%" + searchText + "%";
            statement.setString(1, searchPattern);
            statement.setString(2, searchPattern);
            statement.setString(3, searchPattern);

            // Execute the query and process the results
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getString("id"));
                    user.setFirstName(resultSet.getString("firstName"));
                    user.setLastName(resultSet.getString("lastName"));
                    user.setBdate(resultSet.getString("bdate"));
                    user.setPhone(resultSet.getString("phone"));
                    user.setAddress(resultSet.getString("mailing"));
                    users.add(user);
                }
            }
        }

        // Close the connection
        connection.close();

        return users;
    }

    private static void displaySearchResults(List<User> users) {
        // Create a new JFrame for displaying the search results
        JFrame searchResultsFrame = new JFrame("Search Results");
        searchResultsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        searchResultsFrame.setSize(400, 300);
        searchResultsFrame.setLocationRelativeTo(null);

        // Create a table model for the search results
        // Create a table model for the search results
        DefaultTableModel tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableModel.addColumn("First Name");
        tableModel.addColumn("Last Name");
        tableModel.addColumn("Birthdate");
        tableModel.addColumn("Phone Number");
        tableModel.addColumn("Address");

        for (User user : users) {
            tableModel.addRow(new Object[] { user.getFirstName(), user.getLastName(), user.getBdate(), user.getPhone(),
                    user.getAddress() });
        }

        // Create a table for displaying the search results
        JTable resultsTable = new JTable(tableModel);
        resultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add the search results table to the search results JFrame
        searchResultsFrame.add(new JScrollPane(resultsTable), BorderLayout.CENTER);

        // Create a "View" button for viewing the selected patient's info
        JButton viewButton = new JButton("View");
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = resultsTable.getSelectedRow();
                if (selectedRow != -1) {
                    User selectedUser = users.get(selectedRow);
                    viewPatientInfo(selectedUser, UserRole.PROVIDER);
                }
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchResultsFrame.dispose();
            }
        });

        // Add the "View" button to the search results JFrame
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(viewButton);
        buttonPanel.add(cancelButton);
        searchResultsFrame.add(buttonPanel, BorderLayout.SOUTH);

        // Display the search results JFrame
        searchResultsFrame.setVisible(true);
    }

    private static void viewPatientInfo(User user, UserRole role) {
        try {
            // Show the loading screen here
            JFrame loadingFrame = new JFrame("Loading...");
            loadingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            loadingFrame.setSize(400, 100);
            JPanel panel = new JPanel(new BorderLayout());
            JProgressBar progressBar = new JProgressBar(0, 100);
            progressBar.setValue(0);
            progressBar.setStringPainted(true);
            panel.add(progressBar, BorderLayout.CENTER);
            loadingFrame.add(panel);
            loadingFrame.setLocationRelativeTo(null);
            loadingFrame.setVisible(true);

            // Create a SwingWorker object to execute patientInformationSystem on a separate
            // thread
            SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
                JFrame patientInfoFrame;

                @Override
                protected Void doInBackground() throws Exception {
                    int progress = 0;
                    while (progress < 100) {
                        progress = progressBar.getValue();
                        Thread.sleep(5);
                        progress++;
                        progressBar.setValue(progress);
                        if (progress == 50) {
                            patientInfoFrame = (JFrame) patientInformationSystem
                                    .patientInformationSystem(String.valueOf(user.getId()), progressBar, role);
                            patientInfoFrame
                                    .setTitle("Patient Info - " + user.getFirstName() + " " + user.getLastName());
                            patientInfoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            patientInfoFrame.setSize(1000, 600);
                            patientInfoFrame.setVisible(false);
                        } else {
                            progressBar.setValue(progressBar.getValue());
                        }
                    }
                    return null;
                }

                @Override
                protected void done() {
                    // Once patientInformationSystem has finished loading, dispose of the loading
                    // screen and show the patient's medical records JFrame
                    loadingFrame.dispose();
                    patientInfoFrame.setVisible(true);
                }
            };

            // Schedule the SwingWorker to be executed on the Event Dispatch Thread
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    worker.execute();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred while opening the Patient Information System.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

}
