/**
 * SSSDeduction Class
 * This class to compute SSS deductions based on employee monthly salary
 */

public class SSSDeduction implements Deduction{

    @Override
    public double compute(double monthlyComp){
        if (monthlyComp < 3250) return 135.00;
        else if (monthlyComp < 3750) return 157.50;
        else if (monthlyComp < 4250) return 180.00;
        else if (monthlyComp < 4750) return 202.50;
        else if (monthlyComp < 5250) return 225.00;
        else if (monthlyComp < 5750) return 247.50;
        else if (monthlyComp < 6250) return 270.00;
        else if (monthlyComp < 6750) return 292.50;
        else if (monthlyComp < 7250) return 315.00;
        else if (monthlyComp < 7750) return 337.50;
        else if (monthlyComp < 8250) return 360.00;
        else if (monthlyComp < 8750) return 382.50;
        else if (monthlyComp < 9250) return 405.00;
        else if (monthlyComp < 9750) return 427.50;
        else if (monthlyComp < 10250) return 450.00;
        else if (monthlyComp < 10750) return 472.50;
        else if (monthlyComp < 11250) return 495.00;
        else if (monthlyComp < 11750) return 517.50;
        else if (monthlyComp < 12250) return 540.00;
        else if (monthlyComp < 12750) return 562.50;
        else if (monthlyComp < 13250) return 585.00;
        else if (monthlyComp < 13750) return 607.50;
        else if (monthlyComp < 14250) return 630.00;
        else if (monthlyComp < 14750) return 652.50;
        else if (monthlyComp < 15250) return 675.00;
        else if (monthlyComp < 15750) return 697.50;
        else if (monthlyComp < 16250) return 720.00;
        else if (monthlyComp < 16750) return 742.50;
        else if (monthlyComp < 17250) return 765.00;
        else if (monthlyComp < 17750) return 787.50;
        else if (monthlyComp < 18250) return 810.00;
        else if (monthlyComp < 18750) return 832.50;
        else if (monthlyComp < 19250) return 855.00;
        else if (monthlyComp < 19750) return 877.50;
        else if (monthlyComp < 20250) return 900.00;
        else if (monthlyComp < 20750) return 922.50;
        else if (monthlyComp < 21250) return 945.00;
        else if (monthlyComp < 21750) return 967.50;
        else if (monthlyComp < 22250) return 990.00;
        else if (monthlyComp < 22750) return 1012.50;
        else if (monthlyComp < 23250) return 1035.00;
        else if (monthlyComp < 23750) return 1057.50;
        else if (monthlyComp < 24250) return 1080.00;
        else if (monthlyComp < 24750) return 1102.50;
        else return 1125.00; // Max SSS Deduction
    }
}
