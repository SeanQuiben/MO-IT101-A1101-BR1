import java.io.*;
import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class AttendanceModel {
    private Map<String, Double> employeeHours = new HashMap<>(); // Stores total hours per employee
    private static final String FILE_PATH = "attendance.csv";

    public AttendanceModel() {
        loadAttendance();
    }

    private void loadAttendance() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("Error: Attendance file not found at " + FILE_PATH);
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            boolean isFirstLine = true;
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Skip header
                    continue;
                }

                String[] data = line.split(",");
                if (data.length < 5) continue; // Ensure valid data

                String empNo = data[0].trim();
                String date = data[3].trim();
                String loginTime = data[4].trim();
                String logoutTime = data[5].trim();

                try {
                    LocalTime login = LocalTime.parse(loginTime, timeFormatter);
                    LocalTime logout = LocalTime.parse(logoutTime, timeFormatter);

                    long minutesWorked = Duration.between(login, logout).toMinutes();
                    double hoursWorked = minutesWorked / 60.0;

                    employeeHours.put(empNo, employeeHours.getOrDefault(empNo, 0.0) + hoursWorked);
                } catch (Exception e) {
                    System.out.println("Error parsing time for Employee " + empNo + " on " + date);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading attendance file: " + e.getMessage());
        }
    }

    public double getTotalHoursWorked(String empNo) {
        return employeeHours.getOrDefault(empNo, 0.0);
    }
}
