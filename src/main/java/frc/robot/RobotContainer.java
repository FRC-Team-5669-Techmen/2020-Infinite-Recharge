/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.ContollerConstants;
import frc.robot.commands.ExampleCommand;
import frc.robot.commands.RotateTurret;
import frc.robot.commands.ShootPowerCell;
import frc.robot.subsystems.ControlPanelRotatorSubsystem;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.TurretSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...

  private final ControlPanelRotatorSubsystem m_controlPanelSubsystem = new ControlPanelRotatorSubsystem();
  private final TurretSubsystem fuelTurret = new TurretSubsystem();
  private final Joystick buttonBox = new Joystick(ContollerConstants.BUTTON_BOX_CONTROLLER_PORT);

  // A simple auto routine that drives forward a specified distance, and then stops.

  //no longer needed. Better to have in perodic
  //private final Command testShooter = new ShootPowerCell(fuelTurret); //rredendant


  // A chooser for autonomous commands. We will use this for testing individual subsystem too.
  SendableChooser<Command> m_chooser = new SendableChooser<>();
    




  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();
    
    // Add commands to the autonomous command chooser

    //m_chooser.addOption("Test Turret", testShooter);
    

    // Put the chooser on the dashboard
    Shuffleboard.getTab("Autonomous").add(m_chooser);

    //For debugging purposes, allow tester to set speed
    SmartDashboard.putNumber("Shooter Speed", 0.0);
    SmartDashboard.putNumber("Turret Rotator Speed", 0.0);

    //Needed in order to not return a null command.
    m_chooser.setDefaultOption("Test (Does nothing)", new ExampleCommand(new ExampleSubsystem())); //For good measure if no methods added to chooser
  }

  /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    new JoystickButton(buttonBox, 3).whileActiveOnce(new ShootPowerCell(fuelTurret)); //shoot while pressed.
    new JoystickButton(buttonBox, 4).whileActiveOnce(new RotateTurret(fuelTurret, false));
    new JoystickButton(buttonBox, 5).whileActiveOnce(new RotateTurret(fuelTurret, true));
  }


  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
      // An ExampleCommand will run in autonomous
      return m_chooser.getSelected();
  }
}
