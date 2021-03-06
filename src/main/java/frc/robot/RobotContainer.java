/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;
import edu.wpi.first.wpilibj.Compressor;
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
import frc.robot.commands.HomeTurretRotator;
import frc.robot.commands.Intake;
import frc.robot.commands.ManualMecanumDrive;
import frc.robot.commands.MecanumDriveBrake;
import frc.robot.commands.RotateTurretAndThenStop;
import frc.robot.commands.ShootPowerCell;
import frc.robot.commands.StartShooter;
import frc.robot.commands.StopShooter;
import frc.robot.commands.RotateAndThenStopMagazine.MagazineDirection;
import frc.robot.commands.RetrackLift;
import frc.robot.commands.RotateAndThenStopMagazine;
import frc.robot.commands.RotateTurretAndThenStop.Direction;
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
  private final MagazineSubsystem magazine = new MagazineSubsystem();
  private final MecanumDriveSubsystem mecanumDriveSubsystem = new MecanumDriveSubsystem();

  private final ControlPanelRotatorSubsystem m_controlPanelSubsystem = new ControlPanelRotatorSubsystem();
  //private final Compressor c = new Compressor(0);

  private final LiftSubsystem lift = new LiftSubsystem();

  private final LimelightSubsystem limelight = new LimelightSubsystem();

  //Commands for TeleOP

  private final StartEndCommand startAndThenStopIntake = new StartEndCommand(m_intakeSubsystem::setIntakeMotorOn, 
  m_intakeSubsystem::setIntakeMotorOff, m_intakeSubsystem);

  private final StartEndCommand rotateMagazineClockwiseAndThenStop = new StartEndCommand(magazine::turnOnRotatorClockwise,
   magazine::turnOffRotator, magazine);
   private final StartEndCommand rotateMagazineCounterClockwiseAndThenStop = new StartEndCommand(magazine::turnOnRotatorCounterClockwise,
   magazine::turnOffRotator, magazine);

   private final StartEndCommand feedMagazinefromIntakeAndThenStop = new StartEndCommand(magazine::feedMagazine,
   magazine::turnOffRotator, magazine);

   private final StartEndCommand unjamMagazineWithButton =  new StartEndCommand(magazine::revereseMagazine,
    magazine::turnOffRotator, magazine); 

   private final MoveControlPanelBasedOnColor moveControlPanelBasedOnColor = new MoveControlPanelBasedOnColor(m_controlPanelSubsystem);

   //private final StartEndComman deployContr


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
    configureTeleOpCommands();
    configureButtonBindings();
    
    
    // Add commands to SmartDashboard
    //SmartDashboard.putData("Shoot Power Cells", new ShootPowerCell(fuelTurret, () -> {return 1.00;} ));
    //SmartDashboard.putData("Rotate Turret Left", new RotateTurret(fuelTurret, Direction.COUNTERCLOCKWISE));
    //SmartDashboard.putData("Roate Turret Right", new RotateTurret(fuelTurret, Direction.CLOCKWISE));
    SmartDashboard.putData("Rotate Magazine Clockwise", new RotateAndThenStopMagazine(magazine, MagazineDirection.CLOCKWISE));
    SmartDashboard.putData("Rotate Magazine CounterClockwise", new RotateAndThenStopMagazine(magazine, MagazineDirection.COUNTERCLOCKWISE));
    SmartDashboard.putData("Hone Turret", new HomeTurretRotator(fuelTurret));
    SmartDashboard.putBoolean("Forward turet rotator limit hit", fuelTurret.turretRotatorFwdLimitSwitchHit());
    SmartDashboard.putBoolean("Reverse turet rotator limit hit", fuelTurret.turretRotatorReverseLimitSwitchHit());
    //m_chooser.addOption("Test Turret", testShooter);
    fuelTurret.setName("Fuel Turret");
    
    // Put the chooser on the dashboard
    Shuffleboard.getTab("Autonomous").add(m_chooser);

    //Show which commands are running
    SmartDashboard.putData(fuelTurret);
    SmartDashboard.putData(limelight);

     //Compressser Data
     /*
     SmartDashboard.putData(c);
     SmartDashboard.putBoolean("Compresser On?" , c.enabled());
     SmartDashboard.putBoolean("Pressure Low (so switch on?)", c.getPressureSwitchValue());
     SmartDashboard.putNumber("Pressure Switch Current", c.getCompressorCurrent());
     SmartDashboard.putBoolean("PCM Running Closed Loop Control?", c.getClosedLoopControl());
     
     */

     

    //Needed in order to not return a null command.
    m_chooser.setDefaultOption("Test (Does nothing)", new ExampleCommand(new ExampleSubsystem())); //For good measure if no methods added to chooser

    // Asign default commands

    double deadband = 0.05;
     

    
    
    mecanumDriveSubsystem.setDefaultCommand(
      //y drives robot right
      //x drives is front
      
     
      new ManualMecanumDrive(() -> -m_joystick.getRawAxis(1)*0.65, 
      () -> m_joystick.getRawAxis(0)*0.65, 
      () -> m_joystick.getRawAxis(4)*0.65, mecanumDriveSubsystem));
      
    
  }

  private void configureTeleOpCommands(){
    startAndThenStopIntake.setName("Intaking");
    rotateMagazineClockwiseAndThenStop.setName("Rotating Clockwise");
    rotateMagazineCounterClockwiseAndThenStop.setName("Rotating CounterClockwise");

  }


 
  /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    
    new JoystickButton(buttonBox, 1).whenPressed(new StartShooter(fuelTurret, magazine).withInterrupt(fuelTurret::hoodRetracted)).whenReleased(new StopShooter(fuelTurret, magazine));//add interupt to check status of piston //E button //Interrupt added if hood is retrated
    //engage pistons when flywheel is started
    new JoystickButton(buttonBox, 2).whenPressed(new AimTurretAtPowerPort(fuelTurret, limelight).withInterrupt(fuelTurret::hoodRetracted));
   
    new JoystickButton(buttonBox, 4).whileActiveOnce(new RotateTurretAndThenStop(fuelTurret,  Direction.CLOCKWISE)); //knob left button
    new JoystickButton(buttonBox, 5).whileActiveOnce(new RotateTurretAndThenStop(fuelTurret,  Direction.COUNTERCLOCKWISE)); //knob right nutton

    new JoystickButton(buttonBox, 6).whenPressed(new InstantCommand(m_intakeSubsystem::deployIntake, m_intakeSubsystem)); //SW 1
    new JoystickButton(buttonBox, 7).whenPressed(new InstantCommand(m_intakeSubsystem::retractIntake, m_intakeSubsystem)); //SW 2

  //*new JoystickButton(buttonBox, 8).whenPressed(new InstantCommand(m_controlPanelSubsystem::retractControlPanelRotator, m_controlPanelSubsystem)); //sw3
  // *new JoystickButton(buttonBox, 9).whenPressed(new InstantCommand(m_controlPanelSubsystem::deployControlPanelRotator, m_controlPanelSubsystem)); //sw4
     new JoystickButton(buttonBox, 8).whenPressed(moveControlPanelBasedOnColor); //sw3 does not sop 
     new JoystickButton(buttonBox, 9).cancelWhenPressed(moveControlPanelBasedOnColor);//sw4
    
  new JoystickButton(buttonBox, 10).whenHeld(new InstantCommand(lift::deployLift, lift)); //sw5
  new JoystickButton(buttonBox, 11).whenHeld(new InstantCommand(lift::retrackLift, lift)); //sw6
   // new JoystickButton(buttonBox, 5).whileActiveOnce(new RotateAndThenStopMagazine(magazine, MagazineDirection.CLOCKWISE));
  new JoystickButton(buttonBox, 12).whenHeld(new InstantCommand(fuelTurret::deployHood, fuelTurret)); //tgl1 up
  new JoystickButton(buttonBox, 13).whenHeld(new InstantCommand(fuelTurret::retractHood, fuelTurret)); //tg;1 down
       //new JoystickButton(buttonBox, 1).whileActiveOnce();
   new JoystickButton(buttonBox, 14).whenHeld(startAndThenStopIntake.withInterrupt(m_intakeSubsystem::isIntakeRetracted)
   .alongWith(feedMagazinefromIntakeAndThenStop.withInterrupt(m_intakeSubsystem::isIntakeRetracted))); //TGl 2 up

   new JoystickButton(buttonBox, 15).whenHeld(unjamMagazineWithButton.withInterrupt(m_intakeSubsystem::isIntakeRetracted)); //TODO check that the button is right //TGL2 down
 

  
  

    
   // new JoystickButton(buttonBox, 6).whileActiveOnce(new RotateAndThenStopMagazine(magazine, MagazineDirection.COUNTERCLOCKWISE));    
    //new JoystickButton(buttonBox, 4).whileActiveOnce(new FeedPowerCellToTurret(fuelTurret));


   
    //new JoystickButton(buttonBox, 8).whileActiveOnce(new RotateTurret(fuelTurret, Direction.COUNTERCLOCKWISE));
   // new JoystickButton(buttonBox, 7).whileActiveOnce(new ShootPowerCell(fuelTurret, () -> {return 1.00;} )); //shoot while pressed.


   
    //new JoystickButton(buttonBox, 9).whenPressed(new MoveControlPanelBasedOnColor(m_controlPanelSubsystem));

   // new JoystickButton(buttonBox, 2).whileActiveOnce(new RotateMagazine(magazine, MagazineDirection.CLOCKWISE));
    //new JoystickButton(buttonBox, 3).whileActiveOnce(new RotateMagazine(magazine, MagazineDirection.COUNTERCLOCKWISE));    
    //new JoystickButton(buttonBox, 4).whileActiveOnce(new FeedPowerCellToTurret(fuelTurret));
   // new JoystickButton(buttonBox, 5).whileActiveOnce(new RotateTurret(fuelTurret, Direction.CLOCKWISE));
    //new JoystickButton(buttonBox, 6).whileActiveOnce(new RotateTurret(fuelTurret, Direction.COUNTERCLOCKWISE));
   //// new JoystickButton(buttonBox, 7).whileActiveOnce(new ShootPowerCell(fuelTurret, () -> {return 1.00;} )); //shoot while pressed.
   // new JoystickButton(buttonBox, 7).whenPressed(new StartShooter(fuelTurret)).whenReleased(new StopShooter(fuelTurret));
   /*
    new JoystickButton(buttonBox, 8).whenPressed(new InstantCommand(() -> fuelTurret.moveHoodForward(), 
    fuelTurret)).whenReleased(new InstantCommand(() -> fuelTurret.stopHood(), fuelTurret));
    new JoystickButton(buttonBox, 9).whenPressed(new InstantCommand(() -> fuelTurret.moveHoodBack(), 
    fuelTurret)).whenReleased(new InstantCommand(() -> fuelTurret.stopHood(), fuelTurret));
    
   
  
*/

  //  new JoystickButton(buttonBox, 9).whenPressed(new StartEndCommand(onInit, onEnd, requirements))
   
   // new JoystickButton(buttonBox, 8).whenPressed(new MoveControlPanelBasedOnColor(m_controlPanelSubsystem));

    //SmartDashboard.putData("Turn Turret Clockwise" , new RotateTurret(fuelTurret, Direction.CLOCKWISE));
    //SmartDashboard.putData("Turen Turret CounterClockwise", new RotateTurret(fuelTurret, Direction.COUNTERCLOCKWISE));
    //SmartDashboard.putData("Shoot Power Cell", new ShootPowerCell(fuelTurret, () -> {return 0.70;} ));
    
    //new JoystickButton(buttonBox, 7).toggleWhenPressed(new RotateMagazine(magazine, MagazineDirection.CLOCKWISE).andThen(new WaitCommand(1))); //should make this its own command eventually
    //new JoystickButton(buttonBox, 8).toggleWhenPressed(new RotateMagazine(magazine, MagazineDirection.COUNTERCLOCKWISE).andThen(new WaitCommand(1))); //should make this its own command eventually

    //new JoystickButton(buttonBox, 2).whenPressed(new InstantCommand(m_intakeSubsystem::deployIntake, m_intakeSubsystem));
    //new JoystickButton(buttonBox, 3).whenPressed(new InstantCommand(m_intakeSubsystem::retractIntake, m_intakeSubsystem));


   
    //new JoystickButton(buttonBox, 7).whenPressed(new DeployLift(lift));
   // new JoystickButton(buttonBox, 8).whenPressed(new RetrackLift(lift));
   // new JoystickButton(buttonBox, 9).whenPressed(new InstantCommand(lift::turnOffPistons));
    
   


   // new JoystickButton(m_joystick, 1).whileHeld(new MecanumDriveBrake(mecanumDriveSubsystem));

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
      return new Autonomous(mecanumDriveSubsystem, fuelTurret, limelight, m_intakeSubsystem, magazine);
  }
}