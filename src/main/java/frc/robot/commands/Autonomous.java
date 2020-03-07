/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.RotateAndThenStopMagazine.MagazineDirection;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.LimelightSubsystem;
import frc.robot.subsystems.MagazineSubsystem;
import frc.robot.subsystems.MecanumDriveSubsystem;
import frc.robot.subsystems.TurretSubsystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class Autonomous extends SequentialCommandGroup {
  /**
   * Creates a new Autonomous.
   */
  public Autonomous(MecanumDriveSubsystem m_drive, TurretSubsystem m_turret, 
  LimelightSubsystem m_limelight, IntakeSubsystem intake, MagazineSubsystem magazine) {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    super();
    addCommands(
      
      new DriveForward(2.0, 0.2, m_drive) /*,
      new WaitCommand(2.0),
      new InstantCommand(intake::deployIntake, intake), //
      new WaitCommand(2.0),
      new AimTurretAtPowerPort(m_turret, m_limelight),
      new StartShooter(m_turret),
      with the new startshooter stuff, it is no longer necesaary to set magazine direction and stop magazine
      new WaitCommand(2.0),
      new InstantCommand(magazine::turnOnRotatorClockwise, magazine),
      new WaitCommand(2.0),
      new InstantCommand(magazine::turnOffRotator, magazine),
      new StopShooter(m_turret)
      */
    );
  }
}
