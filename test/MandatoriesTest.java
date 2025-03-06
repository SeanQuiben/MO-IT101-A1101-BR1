import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MandatoriesTest {

    @Test
    void testComputeWithholdingTaxAfterDeductions() {
        double grossSalary = 25000; // Monthly salary
        double sss = 1125; // SSS deduction
        double philHealth = 375; // PhilHealth deduction
        double pagIbig = 100; // Pag-IBIG deduction

        Deduction withholdingTax = new WithholdingTaxDeduction();

        double taxableIncome = grossSalary - (sss + philHealth + pagIbig); // Net taxable income
        double expectedTax = 513.40;

        // **Use case of polymorphism**
        assertEquals(expectedTax, withholdingTax.compute(taxableIncome), 0.01);
    }
}





