/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants.  This class should not be used for any other purpose.  All constants should be
 * declared globally (i.e. public static).  Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    public static final class IntakeSubsystemConstants {
        public static final int INTAKE_MOTOR_PORT = 0;
    }

    public static final class ControlPanelRotatorSubsystemConstansts {

    }

    public static final class LiftSubsystemConstants {

    }

    public static final class TankDrivetrainSubsystemConstants {

    }

    public static final class TurretSubsystemConstants {
        public static final int SHOOTER_MOTOR_CAN_ID = 1;
        public static final int FOLLOWER_SHOOTER_MOTOR_CAN_ID = 2;
        public static final int TURRET_ROTATOR_MOTOR_CAN_ID = 3;
        public static final double SHOOTER_MAX_SPEED = 1.0;
        public static final double ROTATOR_MAX_SPEED = 1.0;
    }

    public static final class ContollerConstants {
        public static final int BUTTON_BOX_CONTROLLER_PORT = 0;

    }
}
