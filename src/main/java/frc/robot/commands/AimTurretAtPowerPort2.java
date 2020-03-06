/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.TurretSubsystemConstants;
import frc.robot.subsystems.LimelightSubsystem;
import frc.robot.subsystems.TurretSubsystem;

public class AimTurretAtPowerPort2 extends CommandBase {
  /**
   * Creates a new AimTurretAtPowerPort2.
   * https://docs.limelightvision.io/en/latest/cs_aiming.html
   */

  private final LimelightSubsystem limelight;
  private final TurretSubsystem turret;

  private double headingXError;
  private double speedAdjust = 0.0f;

  public AimTurretAtPowerPort2(LimelightSubsystem limelight, TurretSubsystem turret) {
    this.limelight = limelight;
    this.turret = turret;
    addRequirements(limelight, turret);
    // Use addRequirements() here to declare subsystem dependencies.

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    headingXError = -limelight.getXTargetAngleOffset();
    if (headingXError > 1.0)
    {
      speedAdjust = TurretSubsystemConstants.kP*headingXError - TurretSubsystemConstants.min_command;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
