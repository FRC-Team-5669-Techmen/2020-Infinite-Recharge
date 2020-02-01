/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeSubsystemConstants;

public class IntakeSubsystem extends SubsystemBase {
  /**
   * Creates a new IntakeSubsystem.
   */

  private final int INTAKE_DEPLOYER_PISTON_FORWARD_CHANNEL = IntakeSubsystemConstants.INTAKE_DEPLOYER_PISTON_FORWARD_CHANNEL;
  private final int INTAKE_DEPLOYER_PISTON_REVERSE_CHANNEL = IntakeSubsystemConstants.INTAKE_DEPLOYER_PISTON_REVERSE_CHANNEL;
  
  private final WPI_TalonFX intakeWheelMotor = new WPI_TalonFX(0);  //not sure if should use WPI_TalonFX or TalonFX

  private final DoubleSolenoid intakeDeployerPistons = new DoubleSolenoid(INTAKE_DEPLOYER_PISTON_FORWARD_CHANNEL, INTAKE_DEPLOYER_PISTON_REVERSE_CHANNEL);
  
  //Pneumatics: not sure double or single 
  //likely double? If double, would do this

  public IntakeSubsystem() {

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
