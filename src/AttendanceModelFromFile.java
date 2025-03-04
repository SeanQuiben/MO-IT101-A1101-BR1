import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AttendanceModelFromFile {
    private List<Attendance> attendanceList = new ArrayList<>();

    public AttendanceModelFromFile() {
        loadAttendanceData();
    }

    private void loadAttendanceData() {
        String filePath = "attendance.csv";
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("Error: Attendance file not found at " + filePath);
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstLine = true;
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip the header
                }

                if (data.length < 6) {
                    System.out.println("Warning: Skipping invalid entry -> " + line);
                    continue;
                }

                try {
                    String empNo = data[0].trim(); // "Employee #"
                    String lastName = data[1].trim(); // "Last Name"
                    String firstName = data[2].trim(); // "First Name"
                    LocalDate date = LocalDate.parse(data[3].trim(), dateFormatter); // "Date"
                    LocalTime loginTime = LocalTime.parse(data[4].trim(), timeFormatter); // "Log In"
                    LocalTime logoutTime = LocalTime.parse(data[5].trim(), timeFormatter); // "Log Out"

                    attendanceList.add(new Attendance(empNo, lastName, firstName, date, loginTime, logoutTime));
                } catch (Exception e) {
                    System.out.println("Error parsing attendance entry: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading attendance file: " + e.getMessage());
        }
    }

    public List<Attendance> getAttendanceList() {
        return attendanceList;
    }

    public double getTotalHoursWorked(String empNo) {
        double totalHours = 0;

        for (Attendance record : attendanceList) {
            if (record.getEmpNo().equals(empNo)) {
                long minutesWorked = java.time.Duration.between(record.getLoginTime(), record.getLogoutTime()).toMinutes();
                totalHours += minutesWorked / 60.0; // Convert minutes to hours
            }
        }

        return totalHours;
    }
}

