/**
 *
 * WitholdingTaxDeduction Class
 * This is to compute withholding tax based on employee monthly salary
 */

public class WithholdingTaxDeduction implements Deduction{

    @Override
    public double compute(double monthlyComp) {
        if (monthlyComp <= 20832) {
            return 0.0;
        } else if (monthlyComp < 33333) {
            return (monthlyComp - 20833) * 0.20; // 20% of excess over 20,833
        } else if (monthlyComp < 66667) {
            return 2500 + (monthlyComp - 33333) * 0.25; // ₱2,500 + 25% of excess over 33,333
        } else if (monthlyComp < 166667) {
            return 10833 + (monthlyComp - 66667) * 0.30; // ₱10,833 + 30% of excess over 66,667
        } else if (monthlyComp < 666667) {
            return 40833.33 + (monthlyComp - 166667) * 0.32; // ₱40,833.33 + 32% of excess over 166,667
        } else {
            return 200833.33 + (monthlyComp - 666667) * 0.35; // ₱200,833.33 + 35% of excess over 666,667
        }
    }
}
