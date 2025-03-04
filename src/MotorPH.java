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
        while (true) {
            System.out.println("**********************************************");
            System.out.println("           MotorPH Employee System           ");
            System.out.println("**********************************************");
            System.out.println("1. Read data from CSV file");
            System.out.println("2. List employee records");
            System.out.println("3. Show total hours worked in a week");
            System.out.println("4. Compute gross weekly salary based on hours worked");
            System.out.println("5. Compute net weekly salary after applying generic deductions");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    loadEmployeeData();
                    break;
                case 2:
                    listEmployees();
                    break;
                case 3:
                    totalHoursWorked();
                    break;
                case 4:
                    salaryOnHoursWorked();
                    break;
                case 5:
                    calculateNetWeeklySalary();
                    break;
                case 6:
                    System.out.println("Exiting program...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

    }

    private static void loadEmployeeData() {
        System.out.println("*********************************************");
        System.out.println("     Loading employee data from CSV files...");
        System.out.println("*********************************************");

        attendanceModel = new AttendanceModelFromFile();
        employeeModel = new EmployeeModelFromFile();

        System.out.println("Employee data successfully loaded.");
    }

    private static void listEmployees() {
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
        System.out.println("         List of Employee Records             ");
        System.out.println("**********************************************");
        System.out.printf("%-10s %-20s %-15s\n", "Emp No", "Name", "Birthday");
        System.out.println("----------------------------------------------------------");

        for (Employee emp : employees) {
            System.out.printf("%-10s %-20s %-15s\n",
                    emp.getEmpNo(),
                    emp.getFirstName() + " " + emp.getLastName(),
                    emp.getBirthday());
        }
    }

    private static void totalHoursWorked() {
        if (employeeModel == null || attendanceModel == null) {
            System.out.println("Please load employee data first (Option 1).");
            return;
        }

        Employee[] employees = employeeModel.getEmployeeModelList();
        if (employees == null || employees.length == 0) {
            System.out.println("No employee records found.");
            return;
        }

        System.out.println("**********************************************");
        System.out.println("           Total Hours Worked                ");
        System.out.println("**********************************************");
        System.out.printf("%-10s %-20s %-15s\n", "Emp No", "Name", "Total Hours");
        System.out.println("----------------------------------------------------------");

        for (Employee emp : employees) {
            double totalHours = attendanceModel.getTotalHoursWorked(emp.getEmpNo());

            System.out.printf("%-10s %-20s %-15.2f\n", emp.getEmpNo(), emp.getFirstName() + " " + emp.getLastName(), totalHours);
        }
    }


    private static void salaryOnHoursWorked() {
        if (employeeModel == null || attendanceModel == null) {
            System.out.println("Please load employee data first (Option 1).");
            return;
        }

        Employee[] employees = employeeModel.getEmployeeModelList();
        if (employees == null || employees.length == 0) {
            System.out.println("No employee records found.");
            return;
        }

        System.out.println("**********************************************");
        System.out.println("            Weekly Salary                     ");
        System.out.println("**********************************************");
        System.out.printf("%-10s %-20s %-15s %-15s\n", "Emp No", "Name", "Total Hours", "Gross Salary");
        System.out.println("----------------------------------------------------------");

        for (Employee emp : employees) {
            double totalHours = attendanceModel.getTotalHoursWorked(emp.getEmpNo());
            double grossSalary = totalHours * emp.getHourlyRate();

            System.out.printf("%-10s %-20s %-15.2f %-15.2f\n", emp.getEmpNo(), emp.getFirstName() + " " + emp.getLastName(), totalHours, grossSalary);
        }
    }


    private static void calculateNetWeeklySalary() {
        if (employeeModel == null) {
            System.out.println("Please load employee data first (Option 1).");
            return;
        }

        Employee[] employees = employeeModel.getEmployeeModelList();
        if (employees == null || employees.length == 0) {
            System.out.println("No employee records found.");
            return;
        }

        DeductionModel sssModel = DeductionCalculator.getDeductionModel("sss");
        DeductionModel philhealthModel = DeductionCalculator.getDeductionModel("philhealth");
        DeductionModel pagibigModel = DeductionCalculator.getDeductionModel("pagibig");
        DeductionModel taxModel = DeductionCalculator.getDeductionModel("tax");

        System.out.println("**********************************************");
        System.out.println("           Net Salary                         ");
        System.out.println("**********************************************");
        System.out.printf("%-10s %-20s %-15s\n", "Emp No", "Name", "Net Salary");
        System.out.println("----------------------------------------------------------");

        for (Employee emp : employees) {
            double totalHours = attendanceModel.getTotalHoursWorked(emp.getEmpNo());
            double grossSalary = totalHours * emp.getHourlyRate();

            double sssDeduction = sssModel.calculateDeduction(emp.getBasicSalary());
            double philhealthDeduction = philhealthModel.calculateDeduction(emp.getBasicSalary());
            double pagibigDeduction = pagibigModel.calculateDeduction(emp.getBasicSalary());
            double taxDeduction = taxModel.calculateDeduction(emp.getBasicSalary());


            double totalDeductions = sssDeduction + philhealthDeduction + pagibigDeduction + taxDeduction;
            double netSalary = grossSalary - totalDeductions;

            System.out.printf("%-10s %-20s %-15.2f\n",
                    emp.getEmpNo(),
                    emp.getFirstName() + " " + emp.getLastName(),
                    netSalary);
        }
    }
}
