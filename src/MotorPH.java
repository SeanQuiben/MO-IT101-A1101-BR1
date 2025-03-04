import java.util.*;

public class MotorPH {

    private static EmployeeModel employeeModel;
    private static final Scanner scanner = new Scanner(System.in); // Use a single Scanner object

    public static void main(String[] args) {
        displayMainMenu();
        scanner.close(); // Close Scanner when the program ends
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
        System.out.println("     Data will be read from employees.csv");
        System.out.println("*********************************************");

        // Always use the CSV file, so no need for user input
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

    private static void calculateNetSalary() {
        System.out.println("Net salary calculation feature is under development.");
    }

    private static void salaryOnHoursWorked() {
        System.out.println("Salary calculation based on hours worked is under development.");
    }
}




