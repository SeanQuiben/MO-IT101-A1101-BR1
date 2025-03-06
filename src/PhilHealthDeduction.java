/**
 * PhilHealthDeduction Class
 * This class to compute Philhealth deductions based on employee monthly salary
 *
 */
public class PhilHealthDeduction implements Deduction {

    @Override
    public double compute(double monthlyComp){
        if (monthlyComp <= 10000) return 300;
        else if (monthlyComp >= 60000) return 1800;
        else return Math.max(300, Math.min(1800, monthlyComp * 0.03));
    }
}
