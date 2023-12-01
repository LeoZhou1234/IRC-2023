// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.motorcontrol.VictorSP;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.*;
import frc.robot.recorder.RecorderRegistry;
import frc.robot.recorder.RecorderService;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.transformers.DriveTransformer;
import org.reflections.Reflections;

import java.util.HashSet;
import java.util.Set;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    // The robot's subsystems and commands are defined here...
//  private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();
//
//  private final ExampleCommand m_autoCommand = new ExampleCommand(m_exampleSubsystem);

    Joystick leftJoystick = new Joystick(0);
    //  Joystick rightJoystick = new Joystick(1);
    Drivetrain drivetrain = new Drivetrain();
    Conveyor conveyor = new Conveyor(new VictorSP(2));
    Drive drive = new Drive(drivetrain, leftJoystick);
    String recording = null;

    JoystickButton joystickButton = new JoystickButton(leftJoystick, 3);
    JoystickButton joystickButton2 = new JoystickButton(leftJoystick, 2);
    JoystickButton joystickButton3 = new JoystickButton(leftJoystick, 6);
    JoystickButton joystickButton4 = new JoystickButton(leftJoystick, 7);
    JoystickButton joystickButton5 = new JoystickButton(leftJoystick, 11);
    static RobotContainer instance = null;

    public static RobotContainer getInstance() {
        return instance;
    }

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        // Configure the button binding
        registerAutoMappings();
        RecorderService.init(leftJoystick);
        drivetrain.setDefaultCommand(drive);
        configureButtonBindings();
        instance = this;
    }

    public String getRecording() {
        return recording;
    }

    public void registerAutoMappings() {
        RecorderRegistry.register(frame -> {
            if (frame.getX() != 0 || frame.getY() != 0) {
                DriveTransformer.DriveInstructions instructions = DriveTransformer.transformInputs(frame.getX(), frame.getY());
                if (instructions.getLeft() != 0D || instructions.getRight() != 0D) {
                    return new AutoDrive(RobotContainer.getInstance().getDrivetrain(), instructions);
                }
            } else {
                RobotContainer.getInstance().getDrivetrain().stop();
            }
            return null;
        });
        RecorderRegistry.register(frame -> {
            if (frame.getPressedButtons().contains(3)) {
                return new SetConveyorSpeed(RobotContainer.getInstance().getConveyor(), Constants.CONVEYOR_SPEED);
            } else if (frame.getPressedButtons().contains(2)) {
                return new SetConveyorSpeed(RobotContainer.getInstance().getConveyor(), -Constants.CONVEYOR_SPEED);
            }
            return new SetConveyorSpeed(RobotContainer.getInstance().getConveyor(), 0D);
        });
    }

    public Drivetrain getDrivetrain() {
        return drivetrain;
    }

    public Conveyor getConveyor() {
        return conveyor;
    }

    /**
     * Use this method to define your button->command mappings. Buttons can be created by
     * instantiating a {@link GenericHID} or one of its subclasses ({@link
     * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
     * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */
    private void configureButtonBindings() {
        joystickButton2
                .whileTrue(new SetConveyorSpeed(conveyor, Constants.CONVEYOR_SPEED))
                .whileFalse(() -> {
                    if (joystickButton2.getAsBoolean()) {
                        new SetConveyorSpeed(conveyor, -Constants.CONVEYOR_SPEED).execute();
                    } else {
                        new SetConveyorSpeed(conveyor, 0D).execute();
                    }
                    HashSet<Subsystem> ret = new HashSet<>();
                    ret.add(conveyor);
                    return ret;
                });
        joystickButton
                .whileTrue(new SetConveyorSpeed(conveyor, -Constants.CONVEYOR_SPEED))
                .whileFalse(() -> {
                    if (joystickButton.getAsBoolean()) {
                        new SetConveyorSpeed(conveyor, Constants.CONVEYOR_SPEED).execute();
                    } else {
                        new SetConveyorSpeed(conveyor, 0D).execute();
                    }
                    HashSet<Subsystem> ret = new HashSet<>();
                    ret.add(conveyor);
                    return ret;
                });
        joystickButton3
                .whileTrue(() -> {
                    if (!RecorderService.getInstance().isRecording() && !Constants.IS_PRODUCTION) {
                        RecorderService.getInstance().beginRecording();
                        System.out.println("Began recording");
                    } else if (Constants.IS_PRODUCTION) {
                        System.out.println("Cannot begin recording - robot is in production mode! Change this in Constants.java if you want to make and export recordings.");
                    }

                    HashSet<Subsystem> ret = new HashSet<>();
                    ret.add(conveyor);
                    return ret;
                });
        joystickButton4
                .whileTrue(() -> {
                    if (RecorderService.getInstance().isRecording()) {
                        RecorderService.getInstance().stopRecording();
                        System.out.println("Stopped recording! Temp-saved. Press 11 to see encoded recording.");
                        recording = RecorderService.getInstance().getFrames().serializeToString();
                        RecorderService.getInstance().clear();
                    }

                    HashSet<Subsystem> ret = new HashSet<>();
                    ret.add(conveyor);
                    return ret;
                });
        joystickButton5
                .whenPressed(() -> {
                    if (recording != null && !Constants.IS_PRODUCTION) {
                        System.out.println("Encoded base64 instructions:");
                        System.out.println(recording);
                    } else if (Constants.IS_PRODUCTION) {
                        System.out.println("Cannot export recording - robot is in production mode! Change this in Constants.java if you want to make and export recordings.");
                    } else {
                        System.out.println("You must record before you can print out the encoded recording!");
                    }
                });
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        // An ExampleCommand will run in autonomous
        if (Constants.USE_FALLBACK_AUTO) {
            if (Constants.IS_PRODUCTION) {
                return new AutoPlaybackCommand(drivetrain, conveyor);
            } else {
                if (recording != null) {
                    return new AutoPlaybackCommand(drivetrain, conveyor, recording);
                } else {
                    return new AutoPlaybackCommand(drivetrain, conveyor);
                }
            }
        } else {
            return new PreprogrammedAuto(drivetrain, conveyor);
        }
    }
}
