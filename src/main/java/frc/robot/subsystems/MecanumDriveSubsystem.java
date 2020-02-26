/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.MecanumDriveConstants;

public class MecanumDriveSubsystem extends SubsystemBase {
  private final WPI_TalonSRX m_frontLeftMotor = new WPI_TalonSRX(MecanumDriveConstants.FRONT_LEFT_MOTOR);
  private final WPI_TalonSRX m_rearLeftMotor = new WPI_TalonSRX(MecanumDriveConstants.REAR_LEFT_MOTOR);
  private final WPI_TalonSRX m_frontRightMotor = new WPI_TalonSRX(MecanumDriveConstants.FRONT_RIGHT_MOTOR);
  private final WPI_TalonSRX m_rearRightMotor = new WPI_TalonSRX(MecanumDriveConstants.REAR_RIGHT_MOTOR);

  MecanumDrive m_drive;

  public MecanumDriveSubsystem() {
    
  }

  protected void driveInit() {
    m_drive = new MecanumDrive(m_frontLeftMotor, m_rearLeftMotor, m_frontRightMotor, m_rearRightMotor);
  }

  public void drive(double xSpeed, double ySpeed, double zRotation) {
    m_drive.driveCartesian(ySpeed, xSpeed, zRotation);
  }

  @Override
  public void periodic() {
    // TODO Auto-generated method stub
    super.periodic();
    SmartDashboard.putNumber("Encoder Ticks", m_frontLeftMotor.getSelectedSensorPosition());
  }
}
