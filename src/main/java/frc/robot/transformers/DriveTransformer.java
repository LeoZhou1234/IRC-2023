package frc.robot.transformers;

public class DriveTransformer {
    public static double clamp(double n, double min, double max){
        return Math.max(Math.min(max, n), min);
    }

    protected static double b_yAxis = Math.log(0.35) / Math.log(0.5);
    protected static double a_yAxis = 0.35 / Math.pow(0.5, b_yAxis);

    protected static double b_xAxis = Math.log(0.2) / Math.log(0.5);
    protected static double a_xAxis = 0.3 / Math.pow(0.75, b_xAxis);

    protected static double exponentialScaleY(double input) {
        return a_yAxis * Math.pow(input, b_yAxis);
    }

    protected static double exponentialScaleX(double input) {
        return a_xAxis * Math.pow(input, b_xAxis);
    }

    protected static double absExponentialScaleX(double input) {
        return input > 0 ? exponentialScaleX(input) : -exponentialScaleX(-input);
    }

    protected static double absExponentialScaleY(double input) {
        return input > 0 ? exponentialScaleY(input) : -exponentialScaleY(-input);
    }

    public static DriveInstructions transformInputs(double x, double y) {
        double xAxis = -absExponentialScaleX(Math.abs(x) < 0.05 ? 0 : x);
        double yAxis = 0;
        if (Math.abs(y) < 0.15 && Math.abs(y) > 0.9) {
            xAxis = y < 0 ? -1D : 1D;
        } else {
            yAxis = absExponentialScaleY(Math.abs(y) < 0.05 ? 0 : y);
        }
        return new DriveInstructions(clamp(yAxis + xAxis, -1, 1), clamp(yAxis - xAxis, -1, 1));
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
