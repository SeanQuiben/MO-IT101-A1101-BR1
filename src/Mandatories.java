import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.*;

public class Mandatories {

    static class EmployeeData {
        String fullName;
        double totalHours;
        Set<Integer> weeksUsed;

        public EmployeeData(String fullName) {
            this.fullName = fullName;
            this.totalHours = 0.0;
            this.weeksUsed = new HashSet<>();
        }
    }

    public static void displayNetWeeklySalary(String attendanceCsvPath, String employeeCsvPath) {
        Map<String, Double> employeeHourlyRates = loadEmployeeHourlyRates(employeeCsvPath); // Load dynamic rates
        Map<String, EmployeeData> dataMap = new HashMap<>();

        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("H:mm");
        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        try (BufferedReader br = new BufferedReader(new FileReader(attendanceCsvPath))) {
            String header = br.readLine(); // Skip header row

            String line;
            while ((line = br.readLine()) != null) {
                String[] cols = line.split(",");
                if (cols.length >= 6) {
                    String empNo     = cols[0].trim().replace("\"", "");
                    String lastName  = cols[1].trim().replace("\"", "");
                    String firstName = cols[2].trim().replace("\"", "");
                    String dateStr   = cols[3].trim().replace("\"", "");
                    String inStr     = cols[4].trim().replace("\"", "");
                    String outStr    = cols[5].trim().replace("\"", "");

                    LocalDate date    = LocalDate.parse(dateStr, dateFmt);
                    LocalTime inTime  = LocalTime.parse(inStr, timeFmt);
                    LocalTime outTime = LocalTime.parse(outStr, timeFmt);

                    long minutesWorked = ChronoUnit.MINUTES.between(inTime, outTime);
                    double hoursWorked = minutesWorked / 60.0;

                    int weekNum = date.get(weekFields.weekOfYear());

                    String fullName = lastName + " " + firstName;
                    double hourlyRate = employeeHourlyRates.getOrDefault(empNo, 0.0); // Use employee-specific rate

                    dataMap.putIfAbsent(empNo, new EmployeeData(fullName));
                    EmployeeData ed = dataMap.get(empNo);

                    ed.totalHours += hoursWorked;
                    ed.weeksUsed.add(weekNum);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading attendance CSV: " + e.getMessage());
        }

        System.out.println("\nEmployee # | Name                  | Weekly Gross Salary | Weekly Mandatories | Net Salary");

        for (String empNo : dataMap.keySet()) {
            EmployeeData ed = dataMap.get(empNo);
            if (ed == null) continue;

            int distinctWeeks = ed.weeksUsed.size();
            double weeklyHours = (distinctWeeks > 0) ? (ed.totalHours / distinctWeeks) : 0.0;

            double hourlyRate = employeeHourlyRates.getOrDefault(empNo, 0.0); // Get the correct hourly rate, else defaults to 0.0
            double weeklyGrossSalary = weeklyHours * hourlyRate;

            double monthlyGross = weeklyGrossSalary * 4;

            double sss        = computeSSS(monthlyGross);
            double philHealth = computePhilHealth(monthlyGross);
            double pagibig    = computePagIbig(monthlyGross);
            double wtax       = computeWithholdingTax(monthlyGross);

            double totalMonthlyMandatories = sss + philHealth + pagibig + wtax;
            double weeklyMandatories = totalMonthlyMandatories / 4;
            double netSalary = weeklyGrossSalary - weeklyMandatories;

            System.out.printf("%-10s | %-20s | %-21.2f | %-18.2f | %.2f\n",
                    empNo, ed.fullName, weeklyGrossSalary, weeklyMandatories, netSalary);
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
                    double hourlyRate = Double.parseDouble(cols[cols.length - 1].trim()); // Last column contains hourly rate
                    hourlyRates.put(empNo, hourlyRate);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading employee details: " + e.getMessage());
        }
        return hourlyRates;
    }

    public static double computeSSS(double monthlyComp) {
        if (monthlyComp < 3250) return 135.00;
        else if (monthlyComp < 3750) return 157.50;
        else if (monthlyComp < 4250) return 180.00;
        else if (monthlyComp < 4750) return 202.50;
        else if (monthlyComp < 5250) return 225.00;
        else if (monthlyComp < 5750) return 247.50;
        else if (monthlyComp < 6250) return 270.00;
        else if (monthlyComp < 6750) return 292.50;
        else if (monthlyComp < 7250) return 315.00;
        else if (monthlyComp < 7750) return 337.50;
        else if (monthlyComp < 8250) return 360.00;
        else if (monthlyComp < 8750) return 382.50;
        else if (monthlyComp < 9250) return 405.00;
        else if (monthlyComp < 9750) return 427.50;
        else if (monthlyComp < 10250) return 450.00;
        else if (monthlyComp < 10750) return 472.50;
        else if (monthlyComp < 11250) return 495.00;
        else if (monthlyComp < 11750) return 517.50;
        else if (monthlyComp < 12250) return 540.00;
        else if (monthlyComp < 12750) return 562.50;
        else if (monthlyComp < 13250) return 585.00;
        else if (monthlyComp < 13750) return 607.50;
        else if (monthlyComp < 14250) return 630.00;
        else if (monthlyComp < 14750) return 652.50;
        else if (monthlyComp < 15250) return 675.00;
        else if (monthlyComp < 15750) return 697.50;
        else if (monthlyComp < 16250) return 720.00;
        else if (monthlyComp < 16750) return 742.50;
        else if (monthlyComp < 17250) return 765.00;
        else if (monthlyComp < 17750) return 787.50;
        else if (monthlyComp < 18250) return 810.00;
        else if (monthlyComp < 18750) return 832.50;
        else if (monthlyComp < 19250) return 855.00;
        else if (monthlyComp < 19750) return 877.50;
        else if (monthlyComp < 20250) return 900.00;
        else if (monthlyComp < 20750) return 922.50;
        else if (monthlyComp < 21250) return 945.00;
        else if (monthlyComp < 21750) return 967.50;
        else if (monthlyComp < 22250) return 990.00;
        else if (monthlyComp < 22750) return 1012.50;
        else if (monthlyComp < 23250) return 1035.00;
        else if (monthlyComp < 23750) return 1057.50;
        else if (monthlyComp < 24250) return 1080.00;
        else if (monthlyComp < 24750) return 1102.50;
        else return 1125.00;
    }

    public static double computePhilHealth(double monthlyComp) {
        if (monthlyComp <= 10000) return 300;
        else if (monthlyComp >= 60000) return 1800;
        else return Math.max(300, Math.min(1800, monthlyComp * 0.03));
    }

    public static double computePagIbig(double monthlyComp) {
        return (monthlyComp <= 1500) ? monthlyComp * 0.03 : monthlyComp * 0.04;
    }

    public static double computeWithholdingTax(double monthlyComp) {
        if (monthlyComp <= 20832) {
            return 0.0;
        } else if (monthlyComp < 33333) {
            return (monthlyComp - 20833) * 0.20; // 20% of excess over 20,833
        } else if (monthlyComp < 66667) {
            return 2500 + (monthlyComp - 33333) * 0.25; // ₱2,500 + 25% of excess over 33,333
        } else if (monthlyComp < 166667) {
            return 10833 + (monthlyComp - 66667) * 0.30; // ₱10,833 + 30% of excess over 66,667
        } else if (monthlyComp < 666667) {
            return 40833.33 + (monthlyComp - 166667) * 0.32; // ₱40,833.33 + 32% of excess over 166,667
        } else {
            return 200833.33 + (monthlyComp - 666667) * 0.35; // ₱200,833.33 + 35% of excess over 666,667
        }
    }
}
