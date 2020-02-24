/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.MecanumDriveConstants;

public class MecanumDriveSubsystem extends SubsystemBase {
  private final WPI_TalonSRX m_frontLeftMotor = new WPI_TalonSRX(MecanumDriveConstants.FRONT_LEFT_MOTOR);
  private final WPI_TalonSRX m_rearLeftMotor = new WPI_TalonSRX(MecanumDriveConstants.REAR_LEFT_MOTOR);
  private final WPI_TalonSRX m_frontRightMotor = new WPI_TalonSRX(MecanumDriveConstants.FRONT_RIGHT_MOTOR);
  private final WPI_TalonSRX m_rearRightMotor = new WPI_TalonSRX(MecanumDriveConstants.REAR_RIGHT_MOTOR);

  MecanumDrive m_drive = new MecanumDrive(m_frontLeftMotor, m_rearLeftMotor,
   m_frontRightMotor, m_rearRightMotor);

  public MecanumDriveSubsystem() {
    super();
    setName("Mecanum Drive Subsystem");
    addChild("Mecanum Drive", m_drive);
    driveInit();
  }

  protected void driveInit() {
    
  }

 /**
   * Drive method for Mecanum platform.
   *
   * <p>Angles are measured clockwise from the positive X axis. The robot's 
   * speed is independent from its angle or rotation rate.
   *
   * @param ySpeed    The robot's speed along the Y axis [-1.0..1.0]. Right is 
   *                  positive.
   * @param xSpeed    The robot's speed along the X axis [-1.0..1.0]. Forward is
   *                  positive.
   * @param zRotation The robot's rotation rate around the Z axis [-1.0..1.0]. 
   *                  Clockwise is positive.
   */
  public void driveCartesian(double xSpeed, double ySpeed, double zRotation) {
    m_drive.driveCartesian(ySpeed, xSpeed, zRotation);
  }

  /**
   * Drive method for Mecanum platform.
   *
   * <p>Angles are measured counter-clockwise from straight ahead. The speed at
   * which the robot drives (translation) is independent from its angle or 
   * rotation rate.
   *
   * @param magnitude The robot's speed at a given angle [-1.0..1.0]. Forward is
   *                  positive.
   * @param angle     The angle around the Z axis at which the robot drives in 
   *                  degrees [-180..180].
   * @param zRotation The robot's rotation rate around the Z axis [-1.0..1.0]. 
   *                  Clockwise is positive.
   */
  public void drivePolar(double magnitude, double angle, double zRotation) {
    m_drive.drivePolar(magnitude, angle, zRotation);
  }
}
