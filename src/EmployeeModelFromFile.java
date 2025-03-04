import java.io.*;
import java.util.*;

public class EmployeeModelFromFile extends EmployeeModel {
    private List<Employee> employeeList = new ArrayList<>();

    public EmployeeModelFromFile() { // No parameters
        loadEmployees(); // Load employees directly from CSV
    }

    private void loadEmployees() {
        String filePath = "employees.csv";
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("Error: Employee file not found at " + filePath);
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true; // To skip the header

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip the header
                }

                String[] data = splitCSVLine(line);

                if (data.length < 19) {
                    System.out.println("Warning: Insufficient data in line. Skipping.");
                    continue;
                }

                try {
                    Employee emp = new Employee();
                    emp.setEmpNo(data[0].trim());
                    emp.setLastName(data[1].trim());
                    emp.setFirstName(data[2].trim());
                    emp.setBirthday(data[3].trim());
                    emp.setAddress(data[4].trim());
                    emp.setPhoneNumber(data[5].trim());
                    emp.setSssNo(data[6].trim());
                    emp.setPhilHealthNo(data[7].trim());
                    emp.setTinNo(data[8].trim());
                    emp.setPagibigNo(data[9].trim());
                    emp.setStatus(data[10].trim());
                    emp.setPosition(data[11].trim());
                    emp.setSupervisor(data[12].trim());

                    // Handle possible empty or invalid numeric fields
                    emp.setBasicSalary(parseDoubleSafe(data[13]));
                    emp.setRiceSubsidy(parseDoubleSafe(data[14]));
                    emp.setPhoneAllowance(parseDoubleSafe(data[15]));
                    emp.setClothingAllowance(parseDoubleSafe(data[16]));
                    emp.setSemiMonthlyRate(parseDoubleSafe(data[17]));
                    emp.setHourlyRate(parseDoubleSafe(data[18]));

                    employeeList.add(emp);
                } catch (Exception e) {
                    System.out.println("Error processing employee data: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading employee file: " + e.getMessage());
        }
    }

    // Helper method to safely parse doubles
    private double parseDoubleSafe(String value) {
        try {
            return value.isEmpty() ? 0.0 : Double.parseDouble(value);
        } catch (NumberFormatException e) {
            System.out.println("Warning: Invalid number format -> " + value);
            return 0.0; // Default to 0 if invalid
        }
    }

    // CSV Parser that correctly handles quoted values
    private String[] splitCSVLine(String line) {
        List<String> tokens = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder sb = new StringBuilder();

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes; // Toggle quote flag
            } else if (c == ',' && !inQuotes) {
                tokens.add(sb.toString().trim());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        tokens.add(sb.toString().trim()); // Add last field

        return tokens.toArray(new String[0]);
    }

    @Override
    public Employee[] getEmployeeModelList() {
        return employeeList.toArray(new Employee[0]);
    }
}







