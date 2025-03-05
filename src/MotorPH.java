import java.util.List;
import java.util.Scanner;

public class MotorPH {
    public static void main(String[] args) {
        String employeeCsvPath = "src/EmployeeDetails.csv";
        String attendanceCsvPath = "src/AttendanceRecords.csv";

        List<Employee> employees = Employee.loadEmployeesFromCSV(employeeCsvPath);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\nWelcome to MotorPH Payroll System!");
            System.out.println("Please choose an option:");
            System.out.println("1. Employee List");
            System.out.println("2. Total Hours Worked");
            System.out.println("3. Gross Weekly Salary");
            System.out.println("4. Net Weekly Salary");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                choice = -1;
            }

            switch (choice) {
                case 1:
                    System.out.println("\n--- Employee List ---");
                    System.out.println("\nEmployee # | Employee Name | Birthday");
                    for (Employee emp : employees) {
                        System.out.println(emp);
                    }
                    break;

                case 2:
                    TotalHours.displayTotalHours(attendanceCsvPath);
                    break;

                case 3:
                    SalaryCalculation.displayWeeklySalary(attendanceCsvPath,employeeCsvPath);
                    break;

                case 4:
                    Mandatories.displayNetWeeklySalary(attendanceCsvPath,employeeCsvPath);
                    break;

                case 5:
                    System.out.println("Exiting the program...");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }

        scanner.close();
    }
}
