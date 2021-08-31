/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeSubsystemConstants;

public class IntakeSubsystem extends SubsystemBase {
  /**
   * Creates a new IntakeSubsystem.
   */


  private final WPI_VictorSPX intakeWheelMotor = new WPI_VictorSPX(IntakeSubsystemConstants.INTAKE_MOTOR_CAN_ID);

  //Pneuamtics not yet installed, commenting out untill installed
  private final Solenoid deployerSoleniod = 
  new Solenoid(IntakeSubsystemConstants.SOLENOID_CHANNEL);
  private final double MAX = IntakeSubsystemConstants.MAX_INTAKE_MOTOR_SPEED;
  
  //Pneumatics: 2x double

  public IntakeSubsystem() {
    intakeWheelMotor.set(0.0);
    setName("Intake Subsystem");
    addChild("Intake Motor", intakeWheelMotor);

    
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }  

  //Find implementations for these methods!

  public void setIntakeMotorOn(){
    intakeWheelMotor.set(-1.0);
  }

  public void setIntakeMotorOff(){
    intakeWheelMotor.set(0.0);
  }

  public void deployIntake(){
    deployerSoleniod.set(true);
  }

 public void retractIntake(){
    deployerSoleniod.set(false);
  } 

  public void loadMagazine(){

  }

  public boolean isIntakeDeployed(){
    return true;

    
  }

  public boolean isIntakeRetracted(){
    return false;

  }


}

