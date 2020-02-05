/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

///SHould consider if PID necessary.

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.TurretSubsystem;

public class ShootPowerCell extends CommandBase {
  /**
   * Creates a new ShootPowerCell.
   */

   private final TurretSubsystem fuelTurret;
   private final double speed;
   
  public ShootPowerCell(TurretSubsystem fuelTurret, double speed) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.fuelTurret = fuelTurret;
    addRequirements(this.fuelTurret);
    this.speed = speed;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    fuelTurret.setShooterMotorSpeed(speed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    fuelTurret.setShooterMotorSpeed(0.0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
