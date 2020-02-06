/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeSubsystemConstants;

public class IntakeSubsystem extends SubsystemBase {
  /**
   * Creates a new IntakeSubsystem.
   */
  
  private final WPI_TalonFX intakeWheels = new WPI_TalonFX(0);
  private final double MAX = IntakeSubsystemConstants.MAX_INTAKE_MOTOR_SPEED;
  
  //Pneumatics: double

  public IntakeSubsystem() {
    intakeWheels.set(IntakeSubsystemConstants.INTAKE_MOTOR_CAN_ID);
    
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void setMotorOn(double speed){
    if(speed <= -MAX|| speed >= MAX)
      intakeWheels.set(speed);
  }
}
