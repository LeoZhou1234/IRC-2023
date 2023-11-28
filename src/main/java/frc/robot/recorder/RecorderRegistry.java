package frc.robot.recorder;

import edu.wpi.first.wpilibj2.command.Command;

import java.util.ArrayList;
import java.util.function.Function;

public class RecorderRegistry {
    private static final ArrayList<Function<RecordingFrame, Command>> constructors = new ArrayList<>();

    public static ArrayList<Function<RecordingFrame, Command>> getConstructors() {
        return constructors;
    }

    public static void register(Function<RecordingFrame, Command> constructor) {
        constructors.add(constructor);
    }
}
