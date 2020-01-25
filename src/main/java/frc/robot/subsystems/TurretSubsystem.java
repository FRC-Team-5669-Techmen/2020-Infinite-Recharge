/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class TurretSubsystem extends SubsystemBase {
  /**
   * Creates a new TurretSubsystem.
   */

  private static double limelightX = 0.0;
  private static double limelightY = 0.0;
  private static double limgelightArea = 0.0;
  private static boolean limelightTargetVisible = false;

  public TurretSubsystem() {

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    updateLimelightValues();
    displayLimelightTelemetry();
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
    limgelightArea = table.getEntry("ta").getDouble(0.0);

    limelightTargetVisible = table.getEntry("tv").getDouble(0.0) < 1.00 ? false : true;

  }
}
