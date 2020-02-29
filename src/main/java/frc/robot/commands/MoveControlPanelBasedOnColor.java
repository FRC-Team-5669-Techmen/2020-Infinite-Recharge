/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ControlPanelRotatorSubsystem;

public class MoveControlPanelBasedOnColor extends CommandBase {
  
  private final ControlPanelRotatorSubsystem m_controlPanelSubsystem;
  /**
   * Creates a new MoveControlPaneBasedOnColor.
   */
  public MoveControlPanelBasedOnColor(ControlPanelRotatorSubsystem subsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_controlPanelSubsystem = subsystem;
    addRequirements(m_controlPanelSubsystem);
  }


  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_controlPanelSubsystem.startControlPanelRotator();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_controlPanelSubsystem.moveControlPanelRotator();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_controlPanelSubsystem.stopControlPanelRotator();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_controlPanelSubsystem.checkColor();
  }
}