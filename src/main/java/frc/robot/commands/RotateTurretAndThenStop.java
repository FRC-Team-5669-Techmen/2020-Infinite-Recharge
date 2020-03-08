/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

///SHould consider if PID necessary.

package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.TurretSubsystemConstants;
import frc.robot.subsystems.TurretSubsystem;

public class RotateTurretAndThenStop extends CommandBase {
  /**
   * Creates a new ShootPowerCell. This is basicaly a Start End Command 
   */

  public enum Direction{
    CLOCKWISE, COUNTERCLOCKWISE;
  }

   private final TurretSubsystem fuelTurret;
   private final Direction direction;
   private final DoubleSupplier speed;
   
  /**
   * If set to true, rotates left. If set to false rotates right
   */
  public RotateTurretAndThenStop(TurretSubsystem fuelTurret, Direction direction, DoubleSupplier speed) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.fuelTurret = fuelTurret;
    addRequirements(this.fuelTurret);
    this.direction = direction;
    this.speed = speed;
    setName("Rotate " + this.direction.toString().toLowerCase());
  }

  public RotateTurretAndThenStop(TurretSubsystem fuelTurret, Direction direction) {
    // Use addRequirements() here to declare subsystem dependencies.
       //Use Math.Abs since we want positive values only from dashboard.
    this.fuelTurret = fuelTurret;
    addRequirements(this.fuelTurret);
    this.direction = direction;
    speed = () -> {return TurretSubsystemConstants.TURRET_ROTATOR_DEFAULT_SPEED;};
    setName("Rotate " + this.direction.toString().toLowerCase().replace('_', ' '));
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    //Check if -speed is clockwise or counterclockwise.
    if(direction == Direction.CLOCKWISE)
      fuelTurret.setTurretRotatorMotorSpeed(-speed.getAsDouble());

    else
      fuelTurret.setTurretRotatorMotorSpeed(speed.getAsDouble());
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    fuelTurret.stopTurretRotator();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
