/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.LiftSubsystemConstants;

public class LiftSubsystem extends SubsystemBase {
  /**
   * Creates a new LiftSubsystem.
   */

  private final int INTAKE_DEPLOYER_PISTON_FORWARD_CHANNEL = LiftSubsystemConstants.SOLENIOD_FORWARD_CHANNEL;
  private final int INTAKE_DEPLOYER_PISTON_REVERSE_CHANNEL = LiftSubsystemConstants.SOLENIOD_REVERSE_CHANNEL;

  private final DoubleSolenoid doubleSolenoid = 
  new DoubleSolenoid(INTAKE_DEPLOYER_PISTON_FORWARD_CHANNEL, INTAKE_DEPLOYER_PISTON_REVERSE_CHANNEL);


  public LiftSubsystem() {
    setName("Pnuematic Lift Subsystem");
  }

  public void deployLift() {
    doubleSolenoid.set(Value.kForward);
  }

  public void retrackLift() {
    doubleSolenoid.set(Value.kReverse);
  }

  public void turnOffPistons() {
    doubleSolenoid.set(Value.kOff);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
