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
    
  }

  public void raiseLift() {
    // Raises the lift
    if(liftPosition == false) {
      liftMotor.set(ControlMode.PercentOutput, 0.5); // 1 = 100% 
      liftPosition = !liftPosition;
    } else {
      // Lift is already up!\
    }
  }

  public void lowerLift() {
    // Lowers the lift
    if(liftPosition == true) {
      liftMotor.set(ControlMode.PercentOutput, -0.5); // 1 = 100% 
      liftPosition = !liftPosition;
    } else {
      // Lift is already down!
    }
  }
  
  public void raisePulley() {
    // Raises the pulley
    if(pulleyPosition == false) {
      pulleyMotor.set(ControlMode.PercentOutput, 0.5); // 1 = 100% 
      pulleyPosition = !pulleyPosition;
    } else {
      // Pulley is already up!
    }
  }

  public void lowerPulley() {
    // Lowers the pulley
    if(pulleyPosition == true) {
      pulleyMotor.set(ControlMode.PercentOutput, -0.5); // 1 = 100% 
      pulleyPosition = !pulleyPosition;
    } else {
      // Pulley is already down!
    }
  }

  public void moveCarriageRight() {
    // Moves the carriage to the right
    carriageMotor.set(ControlMode.PercentOutput, 0.2);
  }

  public void moveCarriageLeft() {
    // Moves the carriage to the left
    carriageMotor.set(ControlMode.PercentOutput, -0.2);
  }
}
