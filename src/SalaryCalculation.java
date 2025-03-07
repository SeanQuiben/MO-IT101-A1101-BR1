import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.*;

/**
 * SalaryCalculation Class
 *
 * This class reads attendance records and employee details from both CSV files
 * to compute the gross weekly salary for each employee based on total hours worked.
 */

public class SalaryCalculation {

    /**
     * EmployeeSalaryData Class
     *
     * Stores employee name, total hours worked, hourly rate, and unique weeks worked.
     */

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

    /**
     * Computes and displays the gross weekly salary for employees.
     * @param attendanceCsvPath to the AttendanceRecords.csv file
     * @param employeeCsvPath  to the EmployeeDetails.csv file
     */

    public static void displayWeeklySalary(String attendanceCsvPath, String employeeCsvPath) {
        // **Load Employee Hourly Rates First**
        Map<String, Double> employeeHourlyRates = loadEmployeeHourlyRates(employeeCsvPath);

        Map<String, EmployeeSalaryData> salaryMap = new HashMap<>();

        // Date and time format for parsing CSV records
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        try (BufferedReader br = new BufferedReader(new FileReader(attendanceCsvPath))) {
            String header = br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String[] cols = line.split(",");
                if (cols.length >= 6) {
                    // Extract employee and work data from AttendanceRecords.csv file
                    String empNo     = cols[0].trim().replace("\"", "");
                    String lastName  = cols[1].trim().replace("\"", "");
                    String firstName = cols[2].trim().replace("\"", "");
                    String dateStr   = cols[3].trim().replace("\"", "");
                    String logInStr  = cols[4].trim().replace("\"", "");
                    String logOutStr = cols[5].trim().replace("\"", "");

                    // Convert date and time strings to objects
                    LocalDate date = LocalDate.parse(dateStr, dateFormatter);
                    LocalTime in   = LocalTime.parse(logInStr, timeFormatter);
                    LocalTime out  = LocalTime.parse(logOutStr, timeFormatter);

                    // This method is to calculate total minutes worked
                    long minutesWorked = ChronoUnit.MINUTES.between(in, out);
                    double hoursWorked = minutesWorked / 60.0;

                    // This is to determine the week number
                    int weekNumber = date.get(weekFields.weekOfYear());

                    String fullName = lastName + " " + firstName;
                    double hourlyRate = employeeHourlyRates.getOrDefault(empNo, 0.0); // Used correct rate, else defaults to 0.0

                    // Store or update employee salary data
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

        // Print formatted employee salary details
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

    /**
     * Loads employee hourly rates from the EmployeeDetails.csv file
     * @param employeeCsvPath path to the employee CSV file
     * @return a map of employee numbers to hourly rates
     */
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

    /**
     * Computes and returns a map of Employee Number → Monthly Salary
     * @param attendanceCsvPath Path to AttendanceRecords.csv file
     * @param employeeCsvPath Path to EmployeeDetails.csv file
     * @return Map of employee numbers to their computed monthly salary
     */
    public static Map<String, Double> getMonthlySalaries(String attendanceCsvPath, String employeeCsvPath) {
        // Load Employee Hourly Rates First
        Map<String, Double> employeeHourlyRates = loadEmployeeHourlyRates(employeeCsvPath);
        Map<String, Double> monthlySalaries = new HashMap<>();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        try (BufferedReader br = new BufferedReader(new FileReader(attendanceCsvPath))) {
            String header = br.readLine(); // Skip header row

            String line;
            while ((line = br.readLine()) != null) {
                String[] cols = line.split(",");
                if (cols.length >= 6) {
                    // Extract employee work data
                    String empNo = cols[0].trim().replace("\"", "");
                    String dateStr = cols[3].trim().replace("\"", "");
                    String logInStr = cols[4].trim().replace("\"", "");
                    String logOutStr = cols[5].trim().replace("\"", "");

                    // Convert date and time
                    LocalDate date = LocalDate.parse(dateStr, dateFormatter);
                    LocalTime in = LocalTime.parse(logInStr, timeFormatter);
                    LocalTime out = LocalTime.parse(logOutStr, timeFormatter);

                    // Calculate total minutes worked
                    long minutesWorked = ChronoUnit.MINUTES.between(in, out);
                    double hoursWorked = minutesWorked / 60.0;

                    // Get hourly rate
                    double hourlyRate = employeeHourlyRates.getOrDefault(empNo, 0.0);
                    double weeklySalary = hoursWorked * hourlyRate;

                    // Convert to monthly salary (4 weeks)
                    double monthlySalary = weeklySalary * 4;

                    // Store employee's monthly salary
                    monthlySalaries.put(empNo, monthlySalary);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading attendance records: " + e.getMessage());
        }

        return monthlySalaries;
    }
}