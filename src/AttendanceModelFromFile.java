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
                    String empNo = data[0].trim();
                    String lastName = data[1].trim();
                    String firstName = data[2].trim();
                    LocalDate date = LocalDate.parse(data[3].trim(), dateFormatter);
                    LocalTime loginTime = LocalTime.parse(data[4].trim(), timeFormatter);
                    LocalTime logoutTime = LocalTime.parse(data[5].trim(), timeFormatter);

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
                totalHours += minutesWorked / 60.0;
            }
        }
        return totalHours;
    }
}

abstract class DeductionModel {
    public abstract double calculateDeduction(double salary);
}

class SSSDeduction extends DeductionModel {
    @Override
    public double calculateDeduction(double salary) {
        if (salary <= 3250) return 135.00;
        else if (salary <= 3750) return 157.50;
        else if (salary <= 4250) return 180.00;
        else if (salary <= 4750) return 202.50;
        else if (salary <= 5250) return 225.00;
        else if (salary <= 5750) return 247.50;
        else if (salary <= 6250) return 270.00;
        else if (salary <= 6750) return 292.50;
        else if (salary <= 7250) return 315.00;
        else if (salary <= 7750) return 337.50;
        else if (salary <= 8250) return 360.00;
        else if (salary <= 8750) return 382.50;
        else if (salary <= 9250) return 405.00;
        else if (salary <= 9750) return 427.50;
        else if (salary <= 10250) return 450.00;
        else if (salary <= 10750) return 472.50;
        else if (salary <= 11250) return 495.00;
        else if (salary <= 11750) return 517.50;
        else if (salary <= 12250) return 540.00;
        else if (salary <= 12750) return 562.50;
        else if (salary <= 13250) return 585.00;
        else if (salary <= 13750) return 607.50;
        else if (salary <= 14250) return 630.00;
        else if (salary <= 14750) return 652.50;
        else if (salary <= 15250) return 675.00;
        else if (salary <= 15750) return 697.50;
        else if (salary <= 16250) return 720.00;
        else if (salary <= 16750) return 742.50;
        else if (salary <= 17250) return 765.00;
        else if (salary <= 17750) return 787.50;
        else if (salary <= 18250) return 810.00;
        else if (salary <= 18750) return 832.50;
        else if (salary <= 19250) return 855.00;
        else if (salary <= 19750) return 877.50;
        else if (salary <= 20250) return 900.00;
        else if (salary <= 20750) return 922.50;
        else if (salary <= 21250) return 945.00;
        else if (salary <= 21750) return 967.50;
        else if (salary <= 22250) return 990.00;
        else if (salary <= 22750) return 1012.50;
        else if (salary <= 23250) return 1035.00;
        else if (salary <= 23750) return 1057.50;
        else if (salary <= 24250) return 1080.00;
        else if (salary <= 24750) return 1102.50;
        else return 1125.00;
    }
}

class PhilhealthDeduction extends DeductionModel {
    @Override
    public double calculateDeduction(double salary) {
        return salary * 0.03 / 2; // Premium Rate (3%)
    }
}

class PagibigDeduction extends DeductionModel {
    @Override
    public double calculateDeduction(double salary) {
        return Math.min(salary * 0.02, 100); // Max contribution cap
    }
}

class TaxDeduction extends DeductionModel {
    @Override
    public double calculateDeduction(double salary) {
        if (salary <= 20832) return 0;
        else if (salary <= 33333) return (salary - 20833) * 0.20;
        else if (salary <= 66667) return 2500 + (salary - 33333) * 0.25;
        else if (salary <= 166667) return 10833 + (salary - 66667) * 0.30;
        else if (salary <= 666667) return 40833.33 + (salary - 166667) * 0.32;
        else return 200833.33 + (salary - 666667) * 0.35;
    }
}

class DeductionCalculator {
    public static DeductionModel getDeductionModel(String type) {
        switch (type.toLowerCase()) {
            case "sss": return new SSSDeduction();
            case "philhealth": return new PhilhealthDeduction();
            case "pagibig": return new PagibigDeduction();
            case "tax": return new TaxDeduction();
            default: throw new IllegalArgumentException("Invalid deduction type");
        }
    }
}