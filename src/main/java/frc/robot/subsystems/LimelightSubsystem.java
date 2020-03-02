/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.LimelightSubsystemConstants;

public class LimelightSubsystem extends SubsystemBase {

  private static double limelightXTargetAngleOffset = 0.0;
  private static double limelightYTargetAngleOffset = 0.0;
  private static double limgelightArea = 0.0;
  private static boolean limelightTargetVisible = false;
  private static NetworkTable limelighTable = NetworkTableInstance.getDefault().getTable("limelight");
  private FieldTarget fieldTarget;
  private CameraMode cameraMode;

  /**
   * Creates a new Limelight Subsystem. By defualt, it will be in VISION_PROCESSOR mode and have the target set to the PowerPort
   * This class emulates: https://github.com/Team395/limelight-api and uses it in the new WPILIBJ2
   * It is currently unde development. Only functionaly core to our usage is made.
   */
  public LimelightSubsystem() {

    setCameraMode(CameraMode.VISION_PROCESSOR);
    setFieldVisionTarget(FieldTarget.POWER_PORT);

    //limelighTableInstance.getEntry("pipeline")

  }

  
  public enum FieldTarget{
    POWER_PORT (LimelightSubsystemConstants.POWERPORT_VISION_PIPELINE),
    LOADING_BAY (LimelightSubsystemConstants.LOADING_BAY_VISION_PIPELINE);

    private final int PIPELINE;
    
    FieldTarget(int PIPELINE){
      this.PIPELINE = PIPELINE;
    }
 }

 
 public enum CameraMode{
    VISION_PROCESSOR (0),
    DRIVER_CAMERA (1);

    private final int CAMMODE;
  
  CameraMode(int CAMMODE){
    this.CAMMODE = CAMMODE;
  }
} 

//public enum 

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    updateLimelightValues();
    displayLimelightTelemetry();
  }

  public double getXTargetAngleOffset(){ //make command
    return limelightXTargetAngleOffset;
  }

  public double getYTargetAngleOffset(){
    return limelightYTargetAngleOffset;
  }

  public boolean targetInView(){
    return limelightTargetVisible;
  }

  public boolean targetLocked(){
    //needs implementation
      return false;
  }

  public void setFieldVisionTarget(FieldTarget fieldTarget){
    this.fieldTarget = fieldTarget;
    limelighTable.getEntry("pipeline").setDouble(this.fieldTarget.PIPELINE);
  }

  public void setCameraMode(CameraMode camMode){
    this.cameraMode = camMode;
    limelighTable.getEntry("camMode").setDouble(this.cameraMode.CAMMODE);
  }

  public FieldTarget getFieldVisionTarget(){
    return this.fieldTarget;
  }

  private void displayLimelightTelemetry(){ //internal; no need to expose this method outside
    //Test code for the Limelight from: http://docs.limelightvision.io/en/latest/getting_started.html#basic-programming 
    SmartDashboard.putNumber("LimelightX", limelightXTargetAngleOffset);
    SmartDashboard.putNumber("LimelightY", limelightYTargetAngleOffset);
    SmartDashboard.putNumber("LimelightArea", limgelightArea);
    SmartDashboard.putBoolean("LimelightTargetValid", limelightTargetVisible);
    SmartDashboard.putString("Current Target", fieldTarget.toString().replace('_', ' ').toLowerCase());
    SmartDashboard.putString("Camera Mode", cameraMode.toString()); //want to see if toString() makes a difference
  }

  private void updateLimelightValues(){
    limelightXTargetAngleOffset = limelighTable.getEntry("tx").getDouble(0.0);
    limelightYTargetAngleOffset = limelighTable.getEntry("ty").getDouble(0.0);
    limgelightArea = limelighTable.getEntry("ta").getDouble(0.0);//hi
    limelightTargetVisible = limelighTable.getEntry("tv").getDouble(0.0) < 1.00 ? false : true;
  }

}
