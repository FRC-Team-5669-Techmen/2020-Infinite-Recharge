/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.MagazineSubsystem;

public class RotateAndThenStopMagazine extends CommandBase {
  /**
   * Creates a new RotateMagazine.
   */

  private final MagazineSubsystem magazine;
  private final MagazineDirection direction;

  public enum MagazineDirection{
    CLOCKWISE, COUNTERCLOCKWISE;
  }

  public RotateAndThenStopMagazine(MagazineSubsystem magazine, MagazineDirection direction) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.magazine = magazine;
    addRequirements(this.magazine);

    this.direction = direction;

    setName("Rotate Magazine");
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(direction == MagazineDirection.CLOCKWISE)
      magazine.turnOnRotatorClockwise();
    else
      magazine.turnOnRotatorCounterClockwise();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    magazine.turnOffRotator();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
