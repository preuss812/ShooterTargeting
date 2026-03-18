package shootertargeting;

import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;

public class ShooterEmpiricalTargeting {
    public final WeightedObservedPoints distanceToRpm = new WeightedObservedPoints();
    public  double[] distanceArray = new double[20];

    private final double inchesToMeters = 2.54/100.0;

    public ShooterEmpiricalTargeting() {
        /*
         * Prior values:
        distanceToRpm.add(48*inchesToMeters, 2500.0);
        distanceToRpm.add(72*inchesToMeters, 2455.0);
        distanceToRpm.add(120*inchesToMeters, 2905.0);
        distanceToRpm.add(14*12*inchesToMeters, 3389.0);
        distanceToRpm.add(17*12*inchesToMeters+1, 3640.0);
        */
        distanceToRpm.add(4*12*inchesToMeters, 2500.0);
        distanceArray[4] = 2500;
        distanceToRpm.add(6*12*inchesToMeters, 2479.0); // ~0
        distanceArray[6] = 2500;
        distanceToRpm.add(7*2*inchesToMeters, 2550.0);  // ~60 // fake
        distanceArray[7] = 2550;
        distanceToRpm.add(8*2*inchesToMeters, 2600.0);  // ~60
        distanceArray[8] = 2600;
        distanceToRpm.add(9*12*inchesToMeters, 2695.0); // ~95 // Fake
        distanceArray[9] = 2695;
        distanceToRpm.add(10*2*inchesToMeters, 2790.0); // ~95
        distanceArray[10] = 2790;
        distanceToRpm.add(11*12*inchesToMeters, 2875.0); // ~85
        distanceArray[11] = 2875;
        distanceToRpm.add(12*12*inchesToMeters, 2970.0); // ~95
        distanceArray[12] = 2970;
        distanceToRpm.add(13*12*inchesToMeters, 3090.0); // ~120
        distanceArray[13] = 3090;
        distanceToRpm.add(14*12*inchesToMeters+1, 3200); // (3200+3140)/2.0); // ~90
        distanceArray[14] = 3200;
        /*
        distanceToRpm.add(15*12*inchesToMeters+1, 3200+120); // (3200+3140)/2.0); // ~90
        distanceArray[15] = 3200+120;
        distanceToRpm.add(16*12*inchesToMeters+1, 3200+120+140); // (3200+3140)/2.0); // ~90
        distanceArray[16] = 3200+120+140;
        */
    }
    
     public double[] fitCurve() {
        // This is where we would fit a curve to the data collected from the simulations.
        // We would use a library like Apache Commons Math to perform polynomial regression on the data points.
        // The resulting curve would allow us to predict the required RPM for any given distance to the target.
        PolynomialCurveFitter fitter = PolynomialCurveFitter.create(3);
        double[] coeff = fitter.fit(distanceToRpm.toList());
        System.out.println("Fitted polynomial coefficients: ax^3 + bx^2 + cx + d where x = distance from shooter to target in meters");
        System.out.printf("a =%10.5f\n", coeff[3]);
        System.out.printf("b =%10.5f\n", coeff[2]);
        System.out.printf("c =%10.5f\n", coeff[1]);
        System.out.printf("d =%10.5f\n", coeff[0]);
        return coeff;
    }
    private static final double pwl[] = { // array or rpm vs feet from hub
            2500.0, // 0
            2500.0, // 1
            2500.0, // 2
            2500.0, // 3
            2500.0, // 4
            2500.0, // 5
            2500.0, // 6
            2550.0, // 7
            2600.0, // 8
            2695.0, // 9
            2790.0, // 10
            2875.0, // 11
            2970.0, // 12
            3090.0, // 13
            3200.0, // 14
            3300.0, // 15
            3400.0, // 16
            3500.0, // 17
            3600.0, // 18
            3700.0, // 19
            3800.0, // 20
        };

    static public double distanceToRPMPWL(double x) {
        double feetToMeters = 12*2.54/100.0;

        double feet = Math.abs(x)/feetToMeters;
        int wholeFeet = (int) feet;
        double remainder = feet - wholeFeet;
        if (wholeFeet > pwl.length - 2)
            wholeFeet = pwl.length - 2;
        return pwl[wholeFeet] + (pwl[wholeFeet+1] - pwl[wholeFeet]) * remainder;
    }
     public static void main(String... args) {
        ShooterEmpiricalTargeting  empirical = new ShooterEmpiricalTargeting();
        double feetToMeters = 12*2.54/100.0;
        double [] coeffs = empirical.fitCurve();
        double last = 0.0;
        double lastMeasuredRpm = 0.0;
        
        for (int distanceInFeet = 4; distanceInFeet <= 16; distanceInFeet++) {
            double rpm = coeffs[0] + coeffs[1]*Math.pow(distanceInFeet*feetToMeters, 1) +  coeffs[2] * Math.pow(distanceInFeet*feetToMeters, 2) + coeffs[3] * Math.pow(distanceInFeet*feetToMeters, 3);
            double measuredRpm = empirical.distanceArray[distanceInFeet];
            double err = rpm - measuredRpm;
            double linearRpm = 2600  + (distanceInFeet - 8) * 97;
            double pwlRPM = distanceToRPMPWL(distanceInFeet * feetToMeters);
            System.out.printf("%2d: %4.0f %4.0f %4.0f %4.0f %4.0f %4.0f %4.0f %4.0f\n", 
                distanceInFeet, rpm, err, rpm - last, measuredRpm - lastMeasuredRpm, linearRpm, linearRpm - measuredRpm,
                pwlRPM, pwlRPM - measuredRpm );
            last = rpm;
            lastMeasuredRpm = measuredRpm;
        }
     }

}
