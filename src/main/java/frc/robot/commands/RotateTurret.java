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

public class RotateTurret extends CommandBase {
  /**
   * Creates a new ShootPowerCell.
   */

   private final TurretSubsystem fuelTurret;
   private final boolean rotateLeft;
   
  /**
   * If set to true, rotates left. If set to false rotates right
   */
  public RotateTurret(TurretSubsystem fuelTurret, boolean rotateLeft) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.fuelTurret = fuelTurret;
    addRequirements(this.fuelTurret);
    this.rotateLeft = rotateLeft;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    //Using Math.Abs to lock the direction of the buttons
    if(rotateLeft)
      fuelTurret.setTurretRotatorMotorSpeed(-Math.abs(SmartDashboard.getNumber("Turret Rotator Speed", 0.0)));

    else
      fuelTurret.setTurretRotatorMotorSpeed(Math.abs(SmartDashboard.getNumber("Turret Rotator Speed", 0.0)));
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    fuelTurret.setTurretRotatorMotorSpeed(0.0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
