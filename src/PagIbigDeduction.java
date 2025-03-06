/**
 * PagIbigDeduction class
 * This class to compute PagIbig deductions based on employee monthly salary
 *
 */

public class PagIbigDeduction implements Deduction{

    @Override
    public double compute(double monthlyComp){
        return (monthlyComp <= 1500) ? monthlyComp * 0.03 : monthlyComp * 0.04;
    }
}
