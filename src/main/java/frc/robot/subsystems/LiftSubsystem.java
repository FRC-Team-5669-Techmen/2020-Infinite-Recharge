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

public class LiftSubsystem extends SubsystemBase {
  // Joystick Intlialization
  Joystick bStick = new Joystick(0);

  // Motor Intialization
  WPI_TalonSRX pulleyMotor = new WPI_TalonSRX(0);
  WPI_TalonSRX liftMotor = new WPI_TalonSRX(0);
  WPI_TalonSRX carriageMotor = new WPI_TalonSRX(0);

  // Button Ports
  int pulleyUpButton = 1;
  int pulleyDownButton = 2;
  int liftUpButton = 3;
  int liftDownButton = 4;
  int carriageRightButton = 5;
  int carriageLeftButton = 6;

  /**
   * Creates a new LiftSubsystem.
   */
  public LiftSubsystem() {

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    if(bStick.getRawButton(pulleyUpButton)) {
      pulleyMotor.set(ControlMode.PercentOutput, 1); // 1 == 100% ??
    }
    if(bStick.getRawButton(pulleyDownButton)) {

    }
    if(bStick.getRawButton(liftUpButton)) {

    }
    if(bStick.getRawButton(liftDownButton)) {

    }
    if(bStick.getRawButton(carriageRightButton)) {

    }
    if(bStick.getRawButton(carriageLeftButton)) {

    }
  }
}
