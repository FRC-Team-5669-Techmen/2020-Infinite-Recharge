/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.TurretSubsystemConstants;
import frc.robot.subsystems.MagazineSubsystem;
import frc.robot.subsystems.TurretSubsystem;

public class StartShooter extends CommandBase {
  /**
   * Creates a new PowerOnShooter.
   * In future, maybe add parameter for target RPM?) Need to see API for PID method or Phoenix API method
   */

  private final TurretSubsystem fuelTurret;
  private final MagazineSubsystem magazine;

  public StartShooter(TurretSubsystem fuelTurret, MagazineSubsystem magazine) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.fuelTurret = fuelTurret;
    this.magazine = magazine;
    addRequirements(fuelTurret);
    addRequirements(magazine);
    setName("Rev Up Shooter Wheel");
  }


  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    fuelTurret.setShooterMotorSpeed(TurretSubsystemConstants.SHOOTER_DEFAULT_SPEED);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    if (interrupted)
    {
      fuelTurret.setShooterMotorSpeed(0.0);
      magazine.turnOffRotator();
    }
    
    else{
      Timer.delay(1); //TODO see if this is a patch dweird physics stuff need to research. Talk to Mr. Aldrich.  So manual delay...
      fuelTurret.turnOnMagazineFeederMotor();
      magazine.feedTurret();

    }
    
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return fuelTurret.shooterAtOperatingRPM();
  }
}
