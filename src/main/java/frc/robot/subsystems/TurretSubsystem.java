/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.TurretSubsystemConstants;

public class TurretSubsystem extends SubsystemBase {
  /**
   * Creates a new TurretSubsystem.
   **/

  private static double limelightX = 0.0;
  private static double limelightY = 0.0;
  private static double limgelightArea = 0.0;
  private static boolean limelightTargetVisible = false;
  private static final double SHOOTER_MAX_SPEED = TurretSubsystemConstants.SHOOTER_MAX_SPEED;
  private static final double ROTATOR_MAX_SPEED = TurretSubsystemConstants.ROTATOR_MAX_SPEED;
  
  private static final int SHOOTER_MOTOR_CAN_ID = TurretSubsystemConstants.SHOOTER_MOTOR_CAN_ID;
  private static final int FOLLOWER_SHOOTER_MOTOR_CAN_ID = TurretSubsystemConstants.FOLLOWER_SHOOTER_MOTOR_CAN_ID;
  private static final int TURRET_ROTATOR_CAN_ID = TurretSubsystemConstants.TURRET_ROTATOR_MOTOR_CAN_ID;

  private final CANSparkMax shooterMotor =  
    new CANSparkMax(SHOOTER_MOTOR_CAN_ID, MotorType.kBrushless);
  
 
  private final CANSparkMax followerShooterMotor =  
    new CANSparkMax(FOLLOWER_SHOOTER_MOTOR_CAN_ID, MotorType.kBrushless);

  private final WPI_TalonFX turretRotatorMotor = new WPI_TalonFX(TURRET_ROTATOR_CAN_ID);

  public TurretSubsystem() {
    
    shooterMotor.restoreFactoryDefaults(); //not to sure about how this works, but it is used a lot.
    followerShooterMotor.restoreFactoryDefaults();
    

    followerShooterMotor.follow(shooterMotor, true);

    shooterMotor.set(0.0);
    turretRotatorMotor.set(0.0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    //updateLimelightValues();
    //displayLimelightTelemetry();
    
  }

  private void displayLimelightTelemetry(){ //internal; no need to expose this method outside
    //Test code for the Limelight from: http://docs.limelightvision.io/en/latest/getting_started.html#basic-programming 
    SmartDashboard.putNumber("LimelightX", limelightX);
    SmartDashboard.putNumber("LimelightY", limelightY);
    SmartDashboard.putNumber("LimelightArea", limgelightArea);
    SmartDashboard.putBoolean("LimelightTargetValid", limelightTargetVisible);

  }

  private void updateLimelightValues(){
    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    limelightX = table.getEntry("tx").getDouble(0.0);
    limelightY = table.getEntry("ty").getDouble(0.0);
    limgelightArea = table.getEntry("ta").getDouble(0.0);//hi

    limelightTargetVisible = table.getEntry("tv").getDouble(0.0) < 1.00 ? false : true;
  }

  public void setShooterMotorSpeed(double speed) {  //ball
    if (speed >= -SHOOTER_MAX_SPEED || speed <= SHOOTER_MAX_SPEED)
      shooterMotor.set(speed);
  }

  public void setTurretRotatorMotorSpeed(double speed) {  //ball
    if (speed >= -ROTATOR_MAX_SPEED || speed <= ROTATOR_MAX_SPEED)
      turretRotatorMotor.set(speed);
  }

  public double getLimeLightX(){ //make command
    return limelightX;
  }

  public double getLimeLightY(){
    return limelightY;
  }

  public boolean targetInView(){
    return limelightTargetVisible;
  }

  public boolean targetLocked(){
    //needs implementation
      return false;
  }


}
