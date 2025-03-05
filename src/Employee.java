import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Employee {
    private String employeeNumber;
    private String name;
    private String birthday;
    private double hourlyRate;

    public Employee(String employeeNumber, String name, String birthday, double hourlyRate) {
        this.employeeNumber = employeeNumber;
        this.name = name;
        this.birthday = birthday;
        this.hourlyRate = hourlyRate;
    }

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

    @Override
    public String toString() {
        return String.format("%s | %s | %s", employeeNumber, name, birthday);
    }

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

                    String fullName = lastName + ", " + firstName;

                    String rateStr = columns[18].trim().replace("\"", "");
                    double rate = Double.parseDouble(rateStr);

                    Employee e = new Employee(empNumber, fullName, bday, rate);
                    employees.add(e);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading employee data from CSV: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid HourlyRate format: " + e.getMessage());
        }

        return employees;
    }
}
