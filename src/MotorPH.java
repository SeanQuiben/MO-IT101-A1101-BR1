import java.util.*;

public class MotorPH {

    private static EmployeeModel employeeModel;
    private static AttendanceModelFromFile attendanceModel;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        displayMainMenu();
        scanner.close();
    }

    private static void displayMainMenu() {
        System.out.println("*********************************************");
        System.out.println("           Motor PH Main Menu                ");
        System.out.println("*********************************************");
        System.out.println("1. Data to read from CSV File");
        System.out.println("2. List of Employee Records");
        System.out.println("3. Calculate Net Salary");
        System.out.println("4. Calculate Salary based on Hours Worked");

        System.out.print("Please select an option: ");
        String option = scanner.nextLine();

        processOption(option);
    }

    private static void processOption(String option) {
        switch (option) {
            case "1":
                choosePlatform();
                break;
            case "2":
                processListOfEmployees();
                break;
            case "3":
                calculateNetSalary();
                break;
            case "4":
                salaryOnHoursWorked();
                break;
            default:
                System.out.println("Invalid Option. Please enter a number from 1 to 4");
                displayMainMenu();
                break;
        }
    }

    private static void choosePlatform() {
        System.out.println("*********************************************");
        System.out.println("     You have chosen option #1");
        System.out.println("     Data will be read from employees.csv and attendance.csv");
        System.out.println("*********************************************");

        //Load Employee and Attendance data
        attendanceModel = new AttendanceModelFromFile();
        employeeModel = new EmployeeModelFromFile();
        System.out.println("Employee data successfully loaded.");

        // Return to main menu
        displayMainMenu();
    }


    private static void processListOfEmployees() {
        if (employeeModel == null) {
            System.out.println("Please choose a data source first (Option 1).");
            return;
        }

        Employee[] employees = employeeModel.getEmployeeModelList();

        if (employees == null || employees.length == 0) {
            System.out.println("No employee records found.");
        } else {
            System.out.println("**********************************************");
            System.out.println("         List of Employee Records            ");
            System.out.println("**********************************************");
            System.out.println("Employee ID | Employee Name | Employee Birthday\n");
            for (Employee emp : employees) {
                System.out.println(emp.getEmpNo() + " - " + emp.getFirstName() + " " + emp.getLastName() +"  "+ emp.getBirthday());
            }
        }
    }

    private static void salaryOnHoursWorked() {
        if (employeeModel == null) {
            System.out.println("Please load employee data first (Option 1).");
            return;
        }

        Employee[] employees = employeeModel.getEmployeeModelList();
        if (employees == null || employees.length == 0) {
            System.out.println("No employee records found.");
            return;
        }

        System.out.println("**********************************************");
        System.out.println("        Salary Based on Hours Worked          ");
        System.out.println("**********************************************");
        System.out.printf("%-10s %-20s %-15s %-15s\n", "Emp No", "Name", "Total Hours", "Salary");
        System.out.println("----------------------------------------------------------");

        for (Employee emp : employees) {
            double totalHours = attendanceModel.getTotalHoursWorked(emp.getEmpNo());
            double salary = totalHours * emp.getHourlyRate();

            System.out.printf("%-10s %-20s %-15.2f %-15.2f\n",
                    emp.getEmpNo(),
                    emp.getFirstName() + " " + emp.getLastName(),
                    totalHours,
                    salary);
        }
    }

    private static void calculateNetSalary() {
        if (employeeModel == null) {
            System.out.println("Please load employee data first (Option 1).");
            return;
        }

        Employee[] employees = employeeModel.getEmployeeModelList();
        if (employees == null || employees.length == 0) {
            System.out.println("No employee records found.");
            return;
        }

        System.out.println("**********************************************");
        System.out.println("                Net Salary Report            ");
        System.out.println("**********************************************");
        System.out.printf("%-10s %-20s %-15s\n", "Emp No", "Name", "Net Salary");
        System.out.println("----------------------------------------------------------");

        for (Employee emp : employees) {
            double grossSalary = emp.getBasicSalary() + emp.getRiceSubsidy() + emp.getPhoneAllowance() + emp.getClothingAllowance();
            double deductions = computeDeductions(emp);
            double netSalary = grossSalary - deductions;

            System.out.printf("%-10s %-20s %-15.2f\n",
                    emp.getEmpNo(),
                    emp.getFirstName() + " " + emp.getLastName(),
                    netSalary);
        }
    }

    private static double computeDeductions(Employee emp) {
        double sss = emp.getBasicSalary() * 0.045; // Example SSS deduction (4.5%)
        double philHealth = emp.getBasicSalary() * 0.035; // Example PhilHealth deduction (3.5%)
        double pagIbig = 100; // Fixed Pag-IBIG deduction
        return sss + philHealth + pagIbig;
    }
}




