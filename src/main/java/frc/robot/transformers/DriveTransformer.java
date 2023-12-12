package frc.robot.transformers;
import frc.robot.subsystems.Drivetrain;

public class DriveTransformer {
    public static double clamp(double n, double min, double max){
        return Math.max(Math.min(max, n), min);
    }
    static double b = Math.log(0.4) / Math.log(0.75);

    // Works for negatives!
    public static double exponentialScale(double input) {
    if (input < 0){
        return -Math.pow(-input, b);
    }
        return Math.pow(input, b);
    }

    public static DriveInstructions transformInputs(double x, double y, Drivetrain dt) {
        double xAxis = -exponentialScale(x);
        double yAxis = exponentialScale(y);
        double leftWheel = yAxis + xAxis;
        double rightWheel = yAxis - xAxis;
        leftWheel = clamp(leftWheel, -1, 1);
        rightWheel = clamp(rightWheel, -1, 1);
        return new DriveInstructions(leftWheel, rightWheel);
    }

    public static class DriveInstructions {
        final double left;
        final double right;

        public DriveInstructions(double left, double right) {
            this.left = left;
            this.right = right;
        }

        public double getLeft() {
            return left;
        }

        public double getRight() {
            return right;
        }
    }
}
