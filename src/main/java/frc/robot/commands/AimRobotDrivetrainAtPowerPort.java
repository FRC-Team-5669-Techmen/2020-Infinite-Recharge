/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.subsystems.LimelightSubsystem;
import frc.robot.subsystems.MecanumDriveSubsystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html



public class AimRobotDrivetrainAtPowerPort extends PIDCommand {
  /**
   * Creates a new AimRobotAtPowerPort.
   */

  private final MecanumDriveSubsystem m_mecanumDriveSubsystem;
  private final LimelightSubsystem m_limelight;

 
  public AimRobotDrivetrainAtPowerPort(MecanumDriveSubsystem mecanumDriveSubsystem, LimelightSubsystem limelight) {
    super(
        // The controller that the command will use
        new PIDController(0, 0, 0),
        // This should return the measurement
        limelight::getXTargetAngleOffset,
        // This should return the setpoint (can also be a constant)
        () -> 0,
        // This uses the output
        output -> {
          // Use the output here: Rotate 
          mecanumDriveSubsystem.driveCartesian(0, 0, output);
        });
        this.m_mecanumDriveSubsystem = mecanumDriveSubsystem;
        this.m_limelight = limelight;


        addRequirements(m_mecanumDriveSubsystem, m_limelight);
    // Use addRequirements() here to declare subsystem dependencies.
    // Configure additional PID options by calling `getController` here.
    getController().enableContinuousInput(-180, 180);


  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return getController().atSetpoint() || !m_limelight.targetInView(); //if the target gets blocked by say, another robot, stop the command
  }

  @Override
  public void end(boolean interrupted) {
    // TODO Auto-generated method stub
    if (!m_limelight.targetInView())
      interrupted = true;
    
    super.end(interrupted);
    m_mecanumDriveSubsystem.driveCartesian(0, 0, 0);
  }
}
