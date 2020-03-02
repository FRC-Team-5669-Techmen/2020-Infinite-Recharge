/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.FollowerType;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import static frc.robot.Constants.TurretSubsystemConstants;

public class TurretSubsystem extends SubsystemBase {
  /**
   * Creates a new TurretSubsystem.
   **/

  //https://phoenix-documentation.readthedocs.io/en/latest/ch13_MC.html#follower

 
  private static final double SHOOTER_MAX_SPEED = TurretSubsystemConstants.SHOOTER_MAX_SPEED;
  private static final double ROTATOR_MAX_SPEED = TurretSubsystemConstants.ROTATOR_MAX_SPEED;
  
  private static final int SHOOTER_MOTOR_CAN_ID = TurretSubsystemConstants.SHOOTER_MOTOR_CAN_ID;
  private static final int FOLLOWER_SHOOTER_MOTOR_CAN_ID = TurretSubsystemConstants.FOLLOWER_SHOOTER_MOTOR_CAN_ID;
  private static final int TURRET_ROTATOR_CAN_ID = TurretSubsystemConstants.TURRET_ROTATOR_MOTOR_CAN_ID;

  private final WPI_TalonFX shooterMotor = new WPI_TalonFX(SHOOTER_MOTOR_CAN_ID);
  private final WPI_TalonFX followerShooterMotor = new WPI_TalonFX(FOLLOWER_SHOOTER_MOTOR_CAN_ID);

  private final WPI_TalonFX turretRotatorMotor = new WPI_TalonFX(TURRET_ROTATOR_CAN_ID);

  private final WPI_VictorSPX turretFeederMotor = 
    new WPI_VictorSPX(TurretSubsystemConstants.TURRET_FEEDER_MOTOR_CAN_ID);

//servos are running in continous mode.
  private final Servo hoodAdjusterServo = new Servo(0);
  private final Servo hoodAdjusterFollowerServo = new Servo(1);

  //also need limit swtiches for the turret. Those will be digital inputs

  private final TalonFXInvertType shooterMotorInvert = TalonFXInvertType.Clockwise; //might be overkill, but just here for readability
  private final TalonFXInvertType followerShooterMotorInvert = TalonFXInvertType.OpposeMaster;

  private double appliedMotorOutput = 0;
	private int selSenPos = 0; /* position units */
  private int selSenVel = 0; /* position units per 100ms */

  private double followerappliedMotorOutput = 0;
	private int follwerselSenPos = 0; /* position units */
  private int follwerselSenVel = followerShooterMotor.getSelectedSensorVelocity(0); /* position units per 100ms */
  
  /* scaling depending on what user wants */
  private double pos_Rotations = 0;
  private double vel_RotPerSec = 0; /* scale per100ms to perSecond */
  private double vel_RotPerMin = 0;
  
  private double follower_pos_Rotations = 0;
  private double follower_vel_RotPerSec = 0; /* scale per100ms to perSecond */
  private double follower_vel_RotPerMin = 0;

  private boolean shooterAtOperatingRPM = false;
  
  public TurretSubsystem() {
    //For debugging purposes, allow tester to set speed
    super();
    

    //followerShooterMotor.follow(shooterMotor, FollowerType.PercentOutput); //TODO: Consider auxillary output?
    //followerShooterMotor.set(Motion, demand0, demand1Type, demand1);
    configShooterMotors();

    setShooterMotorSpeed(0.0);
    setTurretRotatorMotorSpeed(0.0);

    //add them to live window
    setName("Turret Rotator Subsysyem");//default name
    addChild("Shooter Motor", shooterMotor);
    addChild("Follower Shooter Motor", followerShooterMotor);
    addChild("Turret Rotator Motor", turretRotatorMotor);
  }

  @Override
  public void periodic() {
   
   
   updateValues();

    //followerShooterMotor.follow(shooterMotor);
    //followerShooterMotor.
    SmartDashboard.putNumber("Motor-out: %.2f | ", appliedMotorOutput);
    SmartDashboard.putNumber("Pos-units: %d | ", selSenPos);
    SmartDashboard.putNumber("Vel-unitsPer100ms: %d | ", selSenVel);
    SmartDashboard.putNumber("Pos-Rotations:%.3f | ", pos_Rotations);
    SmartDashboard.putNumber("Vel-RPS:%.1f | ", vel_RotPerSec);
    SmartDashboard.putNumber("Vel-RPM:%.1f | ", vel_RotPerMin);

    SmartDashboard.putNumber("Follwoer Motor-out: %.2f | ", followerappliedMotorOutput);
    SmartDashboard.putNumber("Follower Pos-units: %d | ", follwerselSenPos);
    SmartDashboard.putNumber("Follwoer Vel-unitsPer100ms: %d | ", follower_vel_RotPerMin);
    SmartDashboard.putNumber("Follower Pos-Rotations:%.3f | ", follower_pos_Rotations);
    SmartDashboard.putNumber("Follower Vel-RPS:%.1f | ", follower_vel_RotPerSec);
    SmartDashboard.putNumber("Follwoer Vel-RPM:%.1f | ", follower_vel_RotPerMin);
}

  

  public void setShooterMotorSpeed(double speed) {  //ball
    if (speed >= -SHOOTER_MAX_SPEED || speed <= SHOOTER_MAX_SPEED)
      shooterMotor.set(ControlMode.PercentOutput, speed);
  }

  public void setTurretRotatorMotorSpeed(double speed) {  //ball
    if (speed >= -ROTATOR_MAX_SPEED || speed <= ROTATOR_MAX_SPEED)
      turretRotatorMotor.set(speed);
  }

  public void turnOnMagazineFeederMotor(){
    turretFeederMotor.set(-TurretSubsystemConstants.TURRET_FEEDER_MOTOR_DEFAULT_SPEED);
  }

  public void turnOffMagazineFeederMotor(){
    turretFeederMotor.set(0.0);
  }

 
  private void configShooterMotors(){
    shooterMotor.configFactoryDefault();
    followerShooterMotor.configFactoryDefault();

    //
    turretRotatorMotor.configFactoryDefault();

    TalonFXConfiguration encoderConfigs = new TalonFXConfiguration();
    encoderConfigs.primaryPID.selectedFeedbackSensor = FeedbackDevice.IntegratedSensor;
    shooterMotor.configAllSettings(encoderConfigs);
    followerShooterMotor.configAllSettings(encoderConfigs);

    followerShooterMotor.follow(shooterMotor); //kind of dumb the Phoenix requires the follow call every time. Possible to set flag, Phoenix?  
    shooterMotor.setInverted(shooterMotorInvert);
    followerShooterMotor.setInverted(followerShooterMotorInvert);

    /////
    turretRotatorMotor.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);
  }

  public void adjustAngle(double angle){
    //this might evolve into its own command. Angle will also depend on speed
  }

  public void adjustTurretHood(double levelOfExtension){
   // hoodAdjusterFollowerServo.set
  }

  public boolean atLeftLimit(){
    return false; //needs implementation with limite switches
  }

  public boolean atRightLimit(){
    return false; //needs implemtaiotn with limit swtiches
  }

  public double getShooterFollowerRPM(){
    return vel_RotPerMin;
  }

  public boolean atOperatingRPM(){
    return vel_RotPerMin >= TurretSubsystemConstants.SHOOTER_OPERATING_RPM;

  }

  public void setServoSpeed(double speed){
    /*
      should be a speed between 0 and 1
    */
    hoodAdjusterServo.set(speed);
    hoodAdjusterFollowerServo.set(1.0-speed);
  }

  //TODO add other getter methods

  private void updateValues(){
     // This method will be called once per scheduler run
    		/* get the selected sensor for PID0 */
		appliedMotorOutput = shooterMotor.getMotorOutputPercent();
		selSenPos = shooterMotor.getSelectedSensorPosition(0); /* position units */
    selSenVel = shooterMotor.getSelectedSensorVelocity(0); /* position units per 100ms */
    
    followerappliedMotorOutput = followerShooterMotor.getMotorOutputPercent();
		follwerselSenPos = followerShooterMotor.getSelectedSensorPosition(0); /* position units */
	  follwerselSenVel = followerShooterMotor.getSelectedSensorVelocity(0); /* position units per 100ms */

		/* scaling depending on what user wants */
		pos_Rotations = (double) selSenPos / Constants.kFalconFXUnitsPerRevolution;
		vel_RotPerSec = (double) selSenVel / Constants.kFalconFXUnitsPerRevolution * 10; /* scale per100ms to perSecond */
    vel_RotPerMin = vel_RotPerSec * 60.0;
    
    follower_pos_Rotations = (double) follwerselSenPos / Constants.kFalconFXUnitsPerRevolution;
		follower_vel_RotPerSec = (double) follwerselSenVel / Constants.kFalconFXUnitsPerRevolution * 10; /* scale per100ms to perSecond */
		follower_vel_RotPerMin = vel_RotPerSec * 60.0;

  }





}
