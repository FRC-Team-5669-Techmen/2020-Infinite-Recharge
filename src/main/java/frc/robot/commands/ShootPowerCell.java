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

public class ShootPowerCell extends CommandBase {
  /**
   * Creates a new ShootPowerCell.
   */

   private final TurretSubsystem fuelTurret;
   private final DoubleSupplier speed; //not making final.
   
    /*
  Use a set speed
  */
  public ShootPowerCell(TurretSubsystem fuelTurret, DoubleSupplier speed) {
     /*
  Use to set speed
  */
    // Use addRequirements() here to declare subsystem dependencies.
       //Use Math.Abs since we want positive values only from dashboard.
    this.fuelTurret = fuelTurret;
    addRequirements(this.fuelTurret);
    this.speed = speed;
  }

  /*
  Uses SmartDashboard numbers
  */
  public ShootPowerCell(TurretSubsystem fuelTurret) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.fuelTurret = fuelTurret;
    addRequirements(this.fuelTurret);
    speed = () -> {return TurretSubsystemConstants.SHOOTER_DEFAULT_SPEED;};
  }



  // Called when the command is initially scheduled.
  //TODO Implement this stragetfy for rotate turret
  @Override
  public void initialize() {
    fuelTurret.setShooterMotorSpeed(this.speed.getAsDouble());
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
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
