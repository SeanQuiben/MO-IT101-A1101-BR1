import java.time.LocalDate;
import java.time.LocalTime;

public class Attendance {
    private String empNo;
    private String lastName;
    private String firstName;
    private LocalDate date;
    private LocalTime loginTime;
    private LocalTime logoutTime;

    public Attendance(String empNo, String lastName, String firstName, LocalDate date, LocalTime loginTime, LocalTime logoutTime) {
        this.empNo = empNo;
        this.lastName = lastName;
        this.firstName = firstName;
        this.date = date;
        this.loginTime = loginTime;
        this.logoutTime = logoutTime;
    }

    public String getEmpNo() {
        return empNo;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getLoginTime() {
        return loginTime;
    }

    public LocalTime getLogoutTime() {
        return logoutTime;
    }

    public long getHoursWorked() {
        return java.time.Duration.between(loginTime, logoutTime).toHours();
    }

    @Override
    public String toString() {
        return empNo + " | " + lastName + ", " + firstName + " | " + date + " | " + loginTime + " - " + logoutTime;
    }
}

