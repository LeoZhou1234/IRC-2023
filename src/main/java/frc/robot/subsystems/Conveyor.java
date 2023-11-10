package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.VictorSP;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Conveyor extends SubsystemBase {
    VictorSP motor;

    public Conveyor(VictorSP motor) {
        this.motor = motor;
    }

    public void setSpeed(double speed) {
        motor.set(speed);
    }

    public double getSpeed() {
        return motor.get();
    }

    public void stop() {
        motor.set(0D);
    }
}
