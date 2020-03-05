/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.MecanumDriveSubsystem;

public class DriveForward extends CommandBase {
  /**
   * Creates a new DriveForward.
   */

   private final MecanumDriveSubsystem m_drive;
   private double startTime;
   private final double duration;
   private final double speed;

   /**
   * Drive Forward.
   *
   * <p>Angles are measured clockwise from the positive X axis. The robot's 
   * speed is independent from its angle or rotation rate.
   *
   * @param duration  How long to run the command
   * @param speed     The robot's speed
   * @param mecnumDrive A MecnnumDriveSubsystem
   */

  public DriveForward (double duration, double speed, MecanumDriveSubsystem mecanumDrive) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.m_drive = mecanumDrive;
    this.duration = duration;
    this.speed = speed;
    addRequirements(m_drive);

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    startTime = Timer.getFPGATimestamp();


  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_drive.driveCartesian(speed, 0, 0);

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_drive.brake();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return Timer.getFPGATimestamp() - startTime > duration;
  }
}
