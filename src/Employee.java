import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Employee Class
 * This shows an employee with details such as employee number, name, birthday, and hourly rates
 * This also provides a method to load employee data from EmployeeDetails.csv file
 */

public class Employee {
    private String employeeNumber; //Employee ID
    private String name; // Full name (first and last name)
    private String birthday; //Birthday
    private double hourlyRate; //Hourly rate of the employee

    /**
     *
     *
     * @param employeeNumber Unique Employee ID
     * @param name Employee Full name
     * @param birthday Birthday
     * @param hourlyRate Hourly Rate
     */

    public Employee(String employeeNumber, String name, String birthday, double hourlyRate) {
        this.employeeNumber = employeeNumber;
        this.name = name;
        this.birthday = birthday;
        this.hourlyRate = hourlyRate;
    }

    // Getter method
    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public String getName() {
        return name;
    }

    public String getBirthday() {
        return birthday;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    /**
     *
     * @return Formatted string with Employee Number, Name, Birthday
     */

    @Override
    public String toString() {
        return String.format("%s | %s | %s", employeeNumber, name, birthday);
    }

    /**
     *
     * Reads employee details from CSV file then loads them into a list
     * @param filePath EmployeeDetails.csv file
     * @return list of Employees
     */

    public static List<Employee> loadEmployeesFromCSV(String filePath) {
        List<Employee> employees = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String header = br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length >= 19) {
                    String empNumber = columns[0].trim().replace("\"", "");
                    String lastName  = columns[1].trim().replace("\"", "");
                    String firstName = columns[2].trim().replace("\"", "");
                    String bday      = columns[3].trim().replace("\"", "");

                    // Employee Name
                    String fullName = firstName + " " + lastName;

                    // Parse hourly rate from the last column of EmployeeDetails.csv file
                    String rateStr = columns[18].trim().replace("\"", "");
                    double rate = Double.parseDouble(rateStr);

                    // Create employee object and add it to the list
                    Employee e = new Employee(empNumber, fullName, bday, rate);
                    employees.add(e);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading employee data from CSV: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid HourlyRate format: " + e.getMessage());
        }

        return employees; // Return the list of employees
    }
}