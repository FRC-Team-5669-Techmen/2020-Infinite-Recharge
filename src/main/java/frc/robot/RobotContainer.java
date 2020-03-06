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
import frc.robot.Constants.ControllerConstants;
import frc.robot.Constants.TurretSubsystemConstants;
import frc.robot.commands.DeployLift;

import frc.robot.commands.Autonomous;
import frc.robot.commands.AimTurretAtPowerPort;
import frc.robot.commands.ExampleCommand;
import frc.robot.commands.FeedPowerCellToTurret;
import frc.robot.commands.Intake;
import frc.robot.commands.ManualMecanumDrive;
import frc.robot.commands.MecanumDriveBrake;
import frc.robot.commands.RotateTurret;
import frc.robot.commands.ShootPowerCell;
import frc.robot.commands.StartShooter;
import frc.robot.commands.StopShooter;
import frc.robot.commands.RotateMagazine.MagazineDirection;
import frc.robot.commands.MoveControlPaneBasedOnColor;
import frc.robot.commands.RetrackLift;
import frc.robot.commands.RotateMagazine;
import frc.robot.commands.RotateTurret.Direction;
import frc.robot.commands.MoveControlPanelBasedOnColor;
import frc.robot.subsystems.ControlPanelRotatorSubsystem;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.LiftSubsystem;
import frc.robot.subsystems.LimelightSubsystem;
import frc.robot.subsystems.MecanumDriveSubsystem;
import frc.robot.subsystems.MagazineSubsystem;
import frc.robot.subsystems.TurretSubsystem;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...


  private final IntakeSubsystem m_intakeSubsystem = new IntakeSubsystem();
  private final TurretSubsystem fuelTurret = new TurretSubsystem();
  //private final ControlPanelRotatorSubsystem m_controlPanelSubsystem = new ControlPanelRotatorSubsystem(); //not installed
  private final MagazineSubsystem magazine = new MagazineSubsystem();
  private final MecanumDriveSubsystem mecanumDriveSubsystem = new MecanumDriveSubsystem();

  private final LiftSubsystem lift = new LiftSubsystem();

  private final LimelightSubsystem limelight = new LimelightSubsystem();


  //private final ControlPanelRotatorSubsystem m_controlPanelSubsystem = new ControlPanelRotatorSubsystem();

  
  private final Joystick buttonBox = new Joystick(ControllerConstants.BUTTON_BOX_CONTROLLER_PORT);
  private final Joystick m_joystick = new Joystick(ControllerConstants.JOYSTICK_CONTROLLER_PORT);

  //private final TurretSubsystem fuelTurret = new TurretSubsystem();

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
    
    // Add commands to SmartDashboard
    //SmartDashboard.putData("Shoot Power Cells", new ShootPowerCell(fuelTurret, () -> {return 1.00;} ));
    //SmartDashboard.putData("Rotate Turret Left", new RotateTurret(fuelTurret, Direction.COUNTERCLOCKWISE));
    //SmartDashboard.putData("Roate Turret Right", new RotateTurret(fuelTurret, Direction.CLOCKWISE));
    SmartDashboard.putData("Rotate Magazine Clockwise", new RotateMagazine(magazine, MagazineDirection.CLOCKWISE));
    SmartDashboard.putData("Rotate Magazine CounterClockwise", new RotateMagazine(magazine, MagazineDirection.COUNTERCLOCKWISE));

    //m_chooser.addOption("Test Turret", testShooter);
    fuelTurret.setName("Fuel Turret");
    
    // Put the chooser on the dashboard
    Shuffleboard.getTab("Autonomous").add(m_chooser);

    //Show which commands are running
    SmartDashboard.putData(fuelTurret);
    SmartDashboard.putData(limelight);

    //Needed in order to not return a null command.
    m_chooser.setDefaultOption("Test (Does nothing)", new ExampleCommand(new ExampleSubsystem())); //For good measure if no methods added to chooser

    // Asign default commands

    double deadband = 0.05;
     
    /*
    mecanumDriveSubsystem.setDefaultCommand(
      //y drives robot right
      //x drives is front
      
     
      new ManualMecanumDrive(() -> -m_joystick.getRawAxis(1)*0.65, 
      () -> m_joystick.getRawAxis(0)*0.65, 
      () -> m_joystick.getRawAxis(4)*0.65, mecanumDriveSubsystem));
    
    */
  }
 
  /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {


    new JoystickButton(buttonBox, 1).whileActiveOnce(new Intake(m_intakeSubsystem));
    new JoystickButton(buttonBox, 2).whileActiveOnce(new RotateMagazine(magazine, MagazineDirection.CLOCKWISE));
    new JoystickButton(buttonBox, 3).whileActiveOnce(new RotateMagazine(magazine, MagazineDirection.COUNTERCLOCKWISE));    
    //new JoystickButton(buttonBox, 4).whileActiveOnce(new FeedPowerCellToTurret(fuelTurret));
    new JoystickButton(buttonBox, 5).whileActiveOnce(new RotateTurret(fuelTurret, Direction.CLOCKWISE));
    new JoystickButton(buttonBox, 6).whileActiveOnce(new RotateTurret(fuelTurret, Direction.COUNTERCLOCKWISE));
   // new JoystickButton(buttonBox, 7).whileActiveOnce(new ShootPowerCell(fuelTurret, () -> {return 1.00;} )); //shoot while pressed.
   // new JoystickButton(buttonBox, 7).whenPressed(new StartShooter(fuelTurret)).whenReleased(new StopShooter(fuelTurret));
   
    new JoystickButton(buttonBox, 8).whenPressed(new InstantCommand(() -> fuelTurret.moveHoodForward(), 
    fuelTurret)).whenReleased(new InstantCommand(() -> fuelTurret.stopHood(), fuelTurret));
    new JoystickButton(buttonBox, 9).whenPressed(new InstantCommand(() -> fuelTurret.moveHoodBack(), 
    fuelTurret)).whenReleased(new InstantCommand(() -> fuelTurret.stopHood(), fuelTurret));
    
    new JoystickButton(buttonBox, 8).whenPressed(new AimTurretAtPowerPort(fuelTurret, limelight));
  


  //  new JoystickButton(buttonBox, 9).whenPressed(new StartEndCommand(onInit, onEnd, requirements))
   
   // new JoystickButton(buttonBox, 8).whenPressed(new MoveControlPanelBasedOnColor(m_controlPanelSubsystem));
    //SmartDashboard.putData("Turn Turret Clockwise" , new RotateTurret(fuelTurret, Direction.CLOCKWISE));
    //SmartDashboard.putData("Turen Turret CounterClockwise", new RotateTurret(fuelTurret, Direction.COUNTERCLOCKWISE));
    //SmartDashboard.putData("Shoot Power Cell", new ShootPowerCell(fuelTurret, () -> {return 0.70;} ));
    
    //new JoystickButton(buttonBox, 7).toggleWhenPressed(new RotateMagazine(magazine, MagazineDirection.CLOCKWISE).andThen(new WaitCommand(1))); //should make this its own command eventually
    //new JoystickButton(buttonBox, 8).toggleWhenPressed(new RotateMagazine(magazine, MagazineDirection.COUNTERCLOCKWISE).andThen(new WaitCommand(1))); //should make this its own command eventually

    new JoystickButton(buttonBox, 8).whileActiveOnce(new DeployLift(lift));
    new JoystickButton(buttonBox, 9).whileActiveOnce(new RetrackLift(lift));


    new JoystickButton(m_joystick, 1).whileHeld(new MecanumDriveBrake(mecanumDriveSubsystem));

    //new JoystickButton(buttonBox, 3).whileActiveOnce(new ShootPowerCell(fuelTurret)); //shoot while pressed.
    //new JoystickButton(buttonBox, 1).whileActiveOnce(new MoveControlPanelBasedOnColor(m_controlPanelSubsystem)); //shoot while pressed.

  }


  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
      // An ExampleCommand will run in autonomous
      return new Autonomous(mecanumDriveSubsystem, fuelTurret, limelight, m_intakeSubsystem);
  }
}