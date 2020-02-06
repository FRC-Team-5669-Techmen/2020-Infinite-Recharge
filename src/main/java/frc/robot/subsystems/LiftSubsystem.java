/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.LiftSubsystemConstants;

public class LiftSubsystem extends SubsystemBase {

  // Joystick Intlialization
  Joystick bStick = new Joystick(0);

  // Motor Intialization
  WPI_TalonSRX pulleyMotor = new WPI_TalonSRX(LiftSubsystemConstants.PULLEY_MOTOR_CAN_ID);
  WPI_TalonSRX liftMotor = new WPI_TalonSRX(LiftSubsystemConstants.LIFT_MOTOR_CAN_ID);
  WPI_TalonSRX carriageMotor = new WPI_TalonSRX(LiftSubsystemConstants.CARRIAGE_MOTOR_CAN_ID);

  // Button Ports
  int pulleyUpButton = LiftSubsystemConstants.PULLEY_UP_BUTTON;
  int pulleyDownButton = LiftSubsystemConstants.PULLEY_DOWN_BUTTON;
  int liftUpButton = LiftSubsystemConstants.LIFT_UP_BUTTON;
  int liftDownButton = LiftSubsystemConstants.LIFT_DOWN_BUTTON;
  int carriageRightButton = LiftSubsystemConstants.CARRIAGE_RIGHT_BUTTON;
  int carriageLeftButton = LiftSubsystemConstants.CARRIAGE_LEFT_BUTTON;

  private static boolean pulleyPosition = false; // True = up, False = down
  private static boolean liftPosition = false; // True = up, False = down

  /**
   * Creates a new LiftSubsystem.
   */
  public LiftSubsystem() {
    pulleyMotor.set(0);
    liftMotor.set(0);
    carriageMotor.set(0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    if(bStick.getRawButton(pulleyUpButton)) {
      // This will pull the robot up
      if(pulleyPosition == false) {
        pulleyMotor.set(ControlMode.PercentOutput, 0.5); // 1 = 100% 
        pulleyPosition = !pulleyPosition;
      } else {
        // Pulley is already up!
      }
    }
    if(bStick.getRawButton(pulleyDownButton)) {
      // This will bring the robot back down
      if(pulleyPosition == true) {
        pulleyMotor.set(ControlMode.PercentOutput, 0.5); // 1 = 100% 
        pulleyPosition = !pulleyPosition;
      } else {
        // Pulley is already down!
      }
    }

    if(bStick.getRawButton(liftUpButton)) {
      // This will extend the lift up
      if(liftPosition == false) {
        pulleyMotor.set(ControlMode.PercentOutput, 0.5); // 1 = 100% 
        liftPosition = !liftPosition;
      } else {
        // Lift is already up!\
      }
    }
    if(bStick.getRawButton(liftDownButton)) {
      // This will bring the lift down 
      if(liftPosition == true) {
        pulleyMotor.set(ControlMode.PercentOutput, 0.5); // 1 = 100% 
        liftPosition = !liftPosition;
      } else {
        // Lift is already down!
      }
    }

    if(bStick.getRawButton(carriageRightButton)) {
      // This will shift the robot to the right
      pulleyMotor.set(ControlMode.PercentOutput, 0.5); // 1 = 100% 
    }
    if(bStick.getRawButton(carriageLeftButton)) {
      // This will shift the robot to the left
      pulleyMotor.set(ControlMode.PercentOutput, 0.2); // 1 = 100% 
    }
  }
}
