import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class TotalHours {

    static class EmployeeHoursData {
        String employeeName;
        double totalHours;
        Set<Integer> distinctWeeks;

        public EmployeeHoursData(String employeeName) {
            this.employeeName = employeeName;
            this.totalHours = 0.0;
            this.distinctWeeks = new HashSet<>();
        }
    }

    public static void displayTotalHours(String csvFilePath) {
        Map<String, EmployeeHoursData> dataMap = new HashMap<>();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String header = br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length >= 6) {
                    String empNo     = columns[0].trim().replace("\"", "");
                    String lastName  = columns[1].trim().replace("\"", "");
                    String firstName = columns[2].trim().replace("\"", "");
                    String dateStr   = columns[3].trim().replace("\"", "");
                    String logInStr  = columns[4].trim().replace("\"", "");
                    String logOutStr = columns[5].trim().replace("\"", "");

                    LocalDate date   = LocalDate.parse(dateStr, dateFormatter);
                    LocalTime logIn  = LocalTime.parse(logInStr, timeFormatter);
                    LocalTime logOut = LocalTime.parse(logOutStr, timeFormatter);

                    long minutesWorked = ChronoUnit.MINUTES.between(logIn, logOut);
                    double hoursWorked = minutesWorked / 60.0;

                    int weekNumber = date.get(weekFields.weekOfYear());

                    String fullName = lastName + " " + firstName;

                    dataMap.putIfAbsent(empNo, new EmployeeHoursData(fullName));
                    EmployeeHoursData eData = dataMap.get(empNo);

                    eData.totalHours += hoursWorked;
                    eData.distinctWeeks.add(weekNumber);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading AttendanceRecords.csv: " + e.getMessage());
        }

        ArrayList<String> sortedEmpNos = new ArrayList<>(dataMap.keySet());
        Collections.sort(sortedEmpNos);

        System.out.println("\nTotal Hours Worked");
        System.out.println("Employee # | Employee Name       | Total Hours Worked | Weekly Total Hours worked");

        for (String empNo : sortedEmpNos) {
            EmployeeHoursData eData = dataMap.get(empNo);
            double totalHrs = eData.totalHours;
            int weekCount   = eData.distinctWeeks.size();

            double weeklyHrs = (weekCount > 0) ? (totalHrs / weekCount) : 0.0;

            System.out.printf("%-10s | %-20s | %-19.2f | %.2f\n",
                    empNo, eData.employeeName, totalHrs, weeklyHrs);
        }
    }
}