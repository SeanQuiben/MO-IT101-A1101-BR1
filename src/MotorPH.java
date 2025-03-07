import java.util.List;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

/**
 * MotorPH Payroll System - Main Program
 *
 * This program allows users to:
 * 1. View Employee List
 * 2. Display Total Hours Worked
 * 3. Compute Gross Weekly Salary
 * 4. Compute Net Weekly Salary
 * 5. Exit the program
 *
 * The program reads employee details and attendance from CSV files.
 */

public class MotorPH {
    public static void main(String[] args) {
        // File paths for employee and attendance records
        String employeeCsvPath = "src/EmployeeDetails.csv";
        String attendanceCsvPath = "src/AttendanceRecords.csv";

        // Load employee details into a list
        List<Employee> employees = Employee.loadEmployeesFromCSV(employeeCsvPath);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            // Display main menu
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
            // User selection
            switch (choice) {
                case 1:
                    System.out.println("\n--- Employee List ---");
                    System.out.println("\nEmployee # | Employee Name | Birthday");
                    for (Employee emp : employees) {
                        System.out.println(emp);
                    }
                    break;

                case 2:
                    //Display total hours worked for each employee
                    TotalHours.displayTotalHours(attendanceCsvPath);
                    break;

                case 3:
                    //Compute and display gross weekly salary
                    SalaryCalculation.displayWeeklySalary(attendanceCsvPath,employeeCsvPath);
                    break;

                case 4:
                    // **Compute Net Salary After Deductions**

                    //Step 1: Get the monthly salaries from SalaryCalculation
                    Map<String, Double> monthlySalaries = SalaryCalculation.getMonthlySalaries(attendanceCsvPath,employeeCsvPath);

                    //Step 2: Create DeductionCalculator and pass employeeCsvPath
                    Mandatories deductionCalculator = new Mandatories(employeeCsvPath);

                    //Step 3: Compute and display net salary with employee names
                    deductionCalculator.displayNetSalary(monthlySalaries);
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