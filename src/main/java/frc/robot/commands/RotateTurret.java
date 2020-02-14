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

  public enum Direction{
    CLOCKWISE, COUNTERCLOCKWISE;
  }

   private final TurretSubsystem fuelTurret;
   private final Direction direction;
   private final double speed;
   
  /**
   * If set to true, rotates left. If set to false rotates right
   */
  public RotateTurret(TurretSubsystem fuelTurret, Direction direction, double speed) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.fuelTurret = fuelTurret;
    addRequirements(this.fuelTurret);
    this.direction = direction;
    this.speed = speed;
  }

  public RotateTurret(TurretSubsystem fuelTurret, Direction direction) {
    // Use addRequirements() here to declare subsystem dependencies.
       //Use Math.Abs since we want positive values only from dashboard.
    this.fuelTurret = fuelTurret;
    addRequirements(this.fuelTurret);
    this.direction = direction;
    this.speed = Math.abs(SmartDashboard.getNumber("Turret Rotator Speed", 0.0)); //not sure if positive is clockwise
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    //Check if -speed is clockwise or counterclockwise.
    if(direction == Direction.CLOCKWISE)
      fuelTurret.setTurretRotatorMotorSpeed(-speed);

    else
      fuelTurret.setTurretRotatorMotorSpeed(speed);
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
