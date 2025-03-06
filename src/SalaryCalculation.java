import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.*;

public class SalaryCalculation {

    static class EmployeeSalaryData {
        String employeeName;
        double totalHours;
        Set<Integer> distinctWeeks;
        double hourlyRate;

        public EmployeeSalaryData(String employeeName, double hourlyRate) {
            this.employeeName = employeeName;
            this.hourlyRate = hourlyRate;
            this.totalHours = 0.0;
            this.distinctWeeks = new HashSet<>();
        }
    }

    public static void displayWeeklySalary(String attendanceCsvPath, String employeeCsvPath) {
        // **Load Employee Hourly Rates First**
        Map<String, Double> employeeHourlyRates = loadEmployeeHourlyRates(employeeCsvPath);

        Map<String, EmployeeSalaryData> salaryMap = new HashMap<>();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        try (BufferedReader br = new BufferedReader(new FileReader(attendanceCsvPath))) {
            String header = br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String[] cols = line.split(",");
                if (cols.length >= 6) {
                    String empNo     = cols[0].trim().replace("\"", "");
                    String lastName  = cols[1].trim().replace("\"", "");
                    String firstName = cols[2].trim().replace("\"", "");
                    String dateStr   = cols[3].trim().replace("\"", "");
                    String logInStr  = cols[4].trim().replace("\"", "");
                    String logOutStr = cols[5].trim().replace("\"", "");

                    LocalDate date = LocalDate.parse(dateStr, dateFormatter);
                    LocalTime in   = LocalTime.parse(logInStr, timeFormatter);
                    LocalTime out  = LocalTime.parse(logOutStr, timeFormatter);

                    long minutesWorked = ChronoUnit.MINUTES.between(in, out);
                    double hoursWorked = minutesWorked / 60.0;

                    int weekNumber = date.get(weekFields.weekOfYear());

                    String fullName = lastName + " " + firstName;
                    double hourlyRate = employeeHourlyRates.getOrDefault(empNo, 0.0); // Used correct rate, else defaults to 0.0

                    salaryMap.putIfAbsent(empNo, new EmployeeSalaryData(fullName, hourlyRate));
                    EmployeeSalaryData esd = salaryMap.get(empNo);

                    esd.totalHours += hoursWorked;
                    esd.distinctWeeks.add(weekNumber);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading attendance records: " + e.getMessage());
        }

        List<String> sortedEmpNos = new ArrayList<>(salaryMap.keySet());
        Collections.sort(sortedEmpNos);

        System.out.println("\nEmployee # | Name                  | Weekly Salary");

        for (String empNo : sortedEmpNos) {
            EmployeeSalaryData esd = salaryMap.get(empNo);
            if (esd == null) continue;

            double totalHours = esd.totalHours;
            int distinctWeeks = esd.distinctWeeks.size();
            double weeklyHours = (distinctWeeks > 0) ? (totalHours / distinctWeeks) : 0.0;

            double weeklySalary = weeklyHours * esd.hourlyRate; // hourly rate

            System.out.printf("%-10s | %-20s | %.2f\n", empNo, esd.employeeName, weeklySalary);
        }
    }

    private static Map<String, Double> loadEmployeeHourlyRates(String employeeCsvPath) {
        Map<String, Double> hourlyRates = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(employeeCsvPath))) {
            String header = br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String[] cols = line.split(",");
                if (cols.length >= 19) {
                    String empNo = cols[0].trim().replace("\"", "");
                    double hourlyRate = Double.parseDouble(cols[cols.length - 1].trim()); // Last column
                    hourlyRates.put(empNo, hourlyRate);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading employee details: " + e.getMessage());
        }
        return hourlyRates;
    }
}