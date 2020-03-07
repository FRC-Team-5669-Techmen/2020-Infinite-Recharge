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
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;
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

 
  private static double shooterMaxSpeed = TurretSubsystemConstants.SHOOTER_MAX_SPEED;
  private static double rotatorMaxSpeed = TurretSubsystemConstants.TURRET_ROTATOR_MAX_SPEED;
  
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

  private double predictedHoodPostition = 0.0;
  private final double hoodSpeedRatio = 1.0; //need to get units

  private DigitalInput hoodLowerLimitSwitch = new DigitalInput(0); //TODO install this
  private boolean hoodMoving = false;
  private double hoodMovementDuration = 0.0; //in seconds
  private double lastHoodFPGAStartTime = 0.0;
  private boolean checkHoodSoftLimits = true;
  public boolean hoodUnsycned = false;
  private double hoodMaxExtension = TurretSubsystemConstants.HOOD_MAX_EXTENSION;// TODO find
  private double hoodMinExtension = TurretSubsystemConstants.HOOD_MIN_EXTENSION;
  private double hoodMinSpeed = TurretSubsystemConstants.HOOD_MIN_SPEED;
  private double hoodMaxSpeed = TurretSubsystemConstants.HOOD_MAX_SPEED;

  private boolean shooterAtOperatingRPM = false;
  
  public TurretSubsystem() {
    //For debugging purposes, allow tester to set speed
    super();
    

    //followerShooterMotor.follow(shooterMotor, FollowerType.PercentOutput); //TODO: Consider auxillary output?
    //followerShooterMotor.set(Motion, demand0, demand1Type, demand1);
    configShooterMotors();
    configRotatorMotor();
    configMagazineFeederMotor();

    setShooterMotorSpeed(0.0);
    setTurretRotatorMotorSpeed(0.0);
    //turretRotatorMotor.softl

    //add them to live window
    setName("Turret Subsyste");
    addChild("Shooter Motor", shooterMotor);
    addChild("Follower Shooter Motor", followerShooterMotor);
    addChild("Turret Rotator Motor", turretRotatorMotor);
    addChild("Turret Feeder Motor", turretFeederMotor);
  }

  @Override
  public void periodic() {
   
   
   updateValues();
   updateHoodPosition(); //wathcdog/position finder for turret

    //followerShooterMotor.follow(shooterMotor);
    //followerShooterMotor.
    SmartDashboard.putNumber("Turret Rotator Speed", turretRotatorMotor.get());
    SmartDashboard.putNumber("Turret Rotator Encoder Counts", turretRotatorMotor.getSelectedSensorPosition());
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

//--------------------------------------------Magazine Feeder Code-------------------------------------------------
  public void turnOnMagazineFeederMotor(){
  turretFeederMotor.set(-TurretSubsystemConstants.TURRET_FEEDER_MOTOR_DEFAULT_SPEED);
}

public void turnOffMagazineFeederMotor(){
  turretFeederMotor.set(0.0);
}

public void configMagazineFeederMotor(){
  turretFeederMotor.configFactoryDefault();
}

//------------------------------------------------Shooter Code-------------------------------------------------

  public void setShooterMotorSpeed(double speed) {  //ball
    if (speed >= 0 && speed <= shooterMaxSpeed)
      shooterMotor.set(speed);
    else if (speed < 0)
      shooterMotor.set(0);
    else if (speed > shooterMaxSpeed )
      shooterMotor.set( shooterMaxSpeed);
    else
      shooterMotor.set(0.0);
  }

  public void stopShooterMotor(){
    setShooterMotorSpeed(0.0);
  }

  private void configShooterMotors(){
    shooterMotor.configFactoryDefault();
    followerShooterMotor.configFactoryDefault();

    //
   
    TalonFXConfiguration encoderConfigs = new TalonFXConfiguration();
    encoderConfigs.primaryPID.selectedFeedbackSensor = FeedbackDevice.IntegratedSensor;
    shooterMotor.configAllSettings(encoderConfigs);
    followerShooterMotor.configAllSettings(encoderConfigs);

    followerShooterMotor.follow(shooterMotor); //kind of dumb the Phoenix requires the follow call every time. Possible to set flag, Phoenix?  
    shooterMotor.setInverted(shooterMotorInvert);
    followerShooterMotor.setInverted(followerShooterMotorInvert);

    /////
   
  }

  public double getShooterFollowerRPM(){
    return vel_RotPerMin;
  }

  public boolean shooterAtOperatingRPM(){
    return vel_RotPerMin >= TurretSubsystemConstants.SHOOTER_OPERATING_RPM;
  }


  //---------------------------------------------Turret Rotator Code-----------------------------------------------------

  public void setTurretRotatorMotorSpeed(double speed) {  //ball
    if (speed >= -rotatorMaxSpeed && speed <= rotatorMaxSpeed)
      turretRotatorMotor.set(speed);
    else if (speed < -rotatorMaxSpeed)
      turretRotatorMotor.set( -rotatorMaxSpeed);
    else if (speed > rotatorMaxSpeed )
      turretRotatorMotor.set( rotatorMaxSpeed);
  }

  public void stopTurretRotator(){
    turretRotatorMotor.set(0.0);
  }

  public void configRotatorMotor(){
    turretRotatorMotor.configFactoryDefault();

    enableTurretRotatorSoftLimits();

    turretRotatorMotor.configClearPositionOnLimitF(true, 1000);

    //turretRotatorMotor.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);

    /*
    Figure this out in OC Not working out RN. ALso should set the orientation clockwise and counterclowise
    turretRotatorMotor.configPeakOutputForward(-0.20, 500);
    turretRotatorMotor.configPeakOutputReverse(0.20, 500); //the 500 is an arbituary guess
    */
  }

  private void enableTurretRotatorSoftLimits(){

    TalonFXConfiguration turretRotatorEncoderConfigs = new TalonFXConfiguration();
    turretRotatorEncoderConfigs.primaryPID.selectedFeedbackSensor = FeedbackDevice.IntegratedSensor;
    //encoderConfigs.forwardSoftLimitThreshold = 4;
    turretRotatorEncoderConfigs.reverseSoftLimitThreshold = -86000;
    turretRotatorEncoderConfigs.reverseSoftLimitEnable = true;
    turretRotatorEncoderConfigs.forwardSoftLimitThreshold = -7000;
    turretRotatorEncoderConfigs.forwardSoftLimitEnable = true;
    turretRotatorMotor.configAllSettings(turretRotatorEncoderConfigs);    
  } 

  private void disableTurretRotatorSoftLimits(){

    TalonFXConfiguration turretRotatorEncoderConfigs = new TalonFXConfiguration();
    turretRotatorEncoderConfigs.primaryPID.selectedFeedbackSensor = FeedbackDevice.IntegratedSensor;
    //encoderConfigs.forwardSoftLimitThreshold = 4;
    turretRotatorEncoderConfigs.reverseSoftLimitEnable = false;
    turretRotatorEncoderConfigs.forwardSoftLimitEnable = false;
    turretRotatorMotor.configAllSettings(turretRotatorEncoderConfigs);
  }

  public void initRotatorHomingMode() {
    disableTurretRotatorSoftLimits();
    rotatorMaxSpeed = 0.10;
  }

  public void closeRotatorHomeingMode() {
    enableTurretRotatorSoftLimits();
    rotatorMaxSpeed = TurretSubsystemConstants.TURRET_ROTATOR_MAX_SPEED;

  }

  public boolean turretRotatorFwdLimitSwitchHit(){
    return turretRotatorMotor.getSensorCollection().isFwdLimitSwitchClosed() == 1;
  }

  public boolean turretRotatorReverseLimitSwitchHit(){
    return turretRotatorMotor.getSensorCollection().isRevLimitSwitchClosed() == 1;
  }

  //--------------------------------------------Turret Hood Code--------------------------------------------------
  

  public void adjustAngle(double angle){
    //this might evolve into its own command. Angle will also depend on speed
    if(hoodUnsycned)
      System.out.println("Hood not synced");
    
  }

  public void adjustTurretHood(double levelOfExtension){
   // hoodAdjusterFollowerServo.set
  }

  public void updateHoodPosition(){
    if (hoodAdjusterServo.get() != 0.5 && !hoodMoving) //hood started
    {
      lastHoodFPGAStartTime = Timer.getFPGATimestamp();
      hoodMoving = true;
    }

    else if (hoodAtLimitSwitch())
    {
      hoodAdjusterServo.set(0.5);
      predictedHoodPostition = 0.0;
      hoodUnsycned = true;
    }

    else if (checkHoodSoftLimits)
    {
      if ((predictedHoodPostition <= hoodMinExtension) && hoodAdjusterServo.get() < 0.5) //at a limit. Stop servo if moving against it
      {
         hoodAdjusterServo.set(0.5);
      }

      else if (predictedHoodPostition >= hoodMaxExtension && hoodAdjusterServo.get() > 0.5) //at max extension (calcualted)
      {
        hoodAdjusterServo.set(0.5);
      }

    }

    else if (hoodAdjusterServo.get() == 0.5 && hoodMoving) //hood stopped
    {
      double hoodTimeDifference = Timer.getFPGATimestamp() - lastHoodFPGAStartTime;
      double changeInDistance = Math.copySign (hoodSpeedRatio, hoodAdjusterServo.get() < 0.5 ? 1.0 : -1.0)*hoodTimeDifference;
      predictedHoodPostition += changeInDistance;
      hoodMoving = false;
    }
    
  }

  public void initHoodHomingMode(){
    hoodMinExtension = -99.00; //some small number
    checkHoodSoftLimits = false;
  }

  public void closeHoodHomingMode(){
    hoodMinExtension = TurretSubsystemConstants.HOOD_MIN_EXTENSION; //some small number
    checkHoodSoftLimits = true;
    hoodMaxSpeed = TurretSubsystemConstants.HOOD_MAX_SPEED;
  }

  public void closeHoodAtHomingSpeed(){
    setServoSpeed(0.7);
    hoodUnsycned = true;
  }

  public boolean hoodAtLimitSwitch(){
    return hoodLowerLimitSwitch.get();
  }


  private void setServoSpeed(double speed){
    /*
      should be a speed between 0 and 1
    */
    if (speed >= hoodMinSpeed && speed <=hoodMaxSpeed)
    {
      hoodAdjusterServo.set(speed);
      hoodAdjusterFollowerServo.set(1.0-speed);
    }

    else if (speed < hoodMinSpeed)
      {
        hoodAdjusterServo.set(hoodMinSpeed);
        hoodAdjusterFollowerServo.set(1.0-hoodMinSpeed);

      }
      else if (speed > hoodMaxSpeed)
      {
        hoodAdjusterServo.set(hoodMaxSpeed);
        hoodAdjusterFollowerServo.set(1.0-hoodMaxSpeed);

      }
  }

  public void moveHoodForward(){
    setServoSpeed(TurretSubsystemConstants.HOOD_DEFAULT_SPEED);
  }

  public void moveHoodBack(){
    setServoSpeed(TurretSubsystemConstants.HOOD_DEFAULT_SPEED - 1.0);
  }

  public void stopHood(){
    setServoSpeed(0.5);
  }

  //TODO add other getter methods

  //------------------------------Update Methods------------------------------

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
