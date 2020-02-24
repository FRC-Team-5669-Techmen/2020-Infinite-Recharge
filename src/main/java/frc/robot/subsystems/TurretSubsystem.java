/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FollowerType;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
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
import static frc.robot.Constants.TurretSubsystemConstants;

public class TurretSubsystem extends SubsystemBase {
  /**
   * Creates a new TurretSubsystem.
   **/

 
  private static final double SHOOTER_MAX_SPEED = TurretSubsystemConstants.SHOOTER_MAX_SPEED;
  private static final double ROTATOR_MAX_SPEED = TurretSubsystemConstants.ROTATOR_MAX_SPEED;
  
  private static final int SHOOTER_MOTOR_CAN_ID = TurretSubsystemConstants.SHOOTER_MOTOR_CAN_ID;
  private static final int FOLLOWER_SHOOTER_MOTOR_CAN_ID = TurretSubsystemConstants.FOLLOWER_SHOOTER_MOTOR_CAN_ID;
  private static final int TURRET_ROTATOR_CAN_ID = TurretSubsystemConstants.TURRET_ROTATOR_MOTOR_CAN_ID;

  private final WPI_TalonFX shooterMotor = new WPI_TalonFX(SHOOTER_MOTOR_CAN_ID);
  private final WPI_TalonFX followerShooterMotor = new WPI_TalonFX(FOLLOWER_SHOOTER_MOTOR_CAN_ID);

  private final WPI_TalonFX turretRotatorMotor = new WPI_TalonFX(TURRET_ROTATOR_CAN_ID);

  private final Servo hoodAdjusterServo = new Servo(0);
  private final Servo hoodAdjusterFollowerServo = new Servo (1);

  //also need limit swtiches for the turret. Those will be digital inputs

  private final TalonFXInvertType shooterMotorInvert = TalonFXInvertType.CounterClockwise; //might be overkill, but just here for readability
  private final TalonFXInvertType followerShooterMotorInvert = TalonFXInvertType.Clockwise;
  

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
    // This method will be called once per scheduler run
   

    //followerShooterMotor.follow(shooterMotor);
    //followerShooterMotor.
    
    
  }

  

  public void setShooterMotorSpeed(double speed) {  //ball
    if (speed >= -SHOOTER_MAX_SPEED || speed <= SHOOTER_MAX_SPEED)
      shooterMotor.set(ControlMode.PercentOutput, speed);
  }

  public void setTurretRotatorMotorSpeed(double speed) {  //ball
    if (speed >= -ROTATOR_MAX_SPEED || speed <= ROTATOR_MAX_SPEED)
      turretRotatorMotor.set(speed);
  }

 
  private void configShooterMotors(){
    followerShooterMotor.follow(shooterMotor); //kind of dumb the Phoenix requires the follow call every time. Possible to set flag, Phoenix?  
    shooterMotor.setInverted(shooterMotorInvert);
    followerShooterMotor.setInverted(followerShooterMotorInvert);
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




}
