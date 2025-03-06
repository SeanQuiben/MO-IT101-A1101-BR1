import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Mandatories Class
 * This class to Compute net weekly salary after deductions.
 */
public class Mandatories {

    // Use case of polymorphism
    private Deduction sssDeduction;
    private Deduction philHealthDeduction;
    private Deduction pagIbigDeduction;
    private Deduction withholdingTaxDeduction;

    // Map to store Employee Numbers to Names
    private Map<String, String> employeeNames = new HashMap<>();

    /**
     * Constructor that loads employee names from EmployeeDetails.csv
     * @param employeeCsvPath Path to EmployeeDetails.csv
     */
    public Mandatories(String employeeCsvPath) {
        this.sssDeduction = new SSSDeduction();
        this.philHealthDeduction = new PhilHealthDeduction();
        this.pagIbigDeduction = new PagIbigDeduction();
        this.withholdingTaxDeduction = new WithholdingTaxDeduction();
        loadEmployeeNames(employeeCsvPath); // Load employee names from CSV
    }

    /**
     * Loads employee names from EmployeeDetails.csv
     * @param employeeCsvPath Path to EmployeeDetails.csv
     */
    private void loadEmployeeNames(String employeeCsvPath) {
        try (BufferedReader br = new BufferedReader(new FileReader(employeeCsvPath))) {
            String header = br.readLine(); // Skip header row

            String line;
            while ((line = br.readLine()) != null) {
                String[] cols = line.split(",");
                if (cols.length >= 3) { // Assuming first column = ID, second = Last Name, third = First Name
                    String empNo = cols[0].trim().replace("\"", "");
                    String lastName = cols[1].trim().replace("\"", "");
                    String firstName = cols[2].trim().replace("\"", "");
                    String fullName = lastName + " " + firstName; // Format: LastName FirstName
                    employeeNames.put(empNo, fullName);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading employee details: " + e.getMessage());
        }
    }

    /**
     * Computes and displays net weekly salary.
     * @param totalMonthlySalaries Map of employee numbers and their monthly salaries.
     */
    public void displayNetSalary(Map<String, Double> totalMonthlySalaries) {
        System.out.println("\nEmployee # | Name                  | Gross Salary | Deductions | Net Salary");

        for (String empNo : totalMonthlySalaries.keySet()) {
            double grossSalary = totalMonthlySalaries.get(empNo);

            // Compute deductions
            double sss = sssDeduction.compute(grossSalary);
            double philHealth = philHealthDeduction.compute(grossSalary);
            double pagIbig = pagIbigDeduction.compute(grossSalary);
            double tax = withholdingTaxDeduction.compute(grossSalary);

            double totalDeductions = sss + philHealth + pagIbig + tax;
            double netSalary = grossSalary - totalDeductions;

            // Retrieve the employee name
            String employeeName = employeeNames.getOrDefault(empNo, "Unknown Employee");

            // Print the employee name
            System.out.printf("%-10s | %-20s | %-12.2f | %-10.2f | %.2f\n",
                    empNo, employeeName, grossSalary, totalDeductions, netSalary);
        }
    }
}
