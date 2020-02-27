/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeSubsystemConstants;

public class IntakeSubsystem extends SubsystemBase {
  /**
   * Creates a new IntakeSubsystem.
   */

  private final int INTAKE_DEPLOYER_PISTON_FORWARD_CHANNEL = IntakeSubsystemConstants.SOLENIOD_FORWARD_CHANNEL;
  private final int INTAKE_DEPLOYER_PISTON_REVERSE_CHANNEL = IntakeSubsystemConstants.SOLENIOD_REVERSE_CHANNEL;
  
  private final WPI_VictorSPX intakeWheelMotor = new WPI_VictorSPX(IntakeSubsystemConstants.INTAKE_MOTOR_CAN_ID);

  //Pneuamtics not yet installed, commenting out untill installed
  /*private final DoubleSolenoid doubleSoleniod = 
  new DoubleSolenoid(INTAKE_DEPLOYER_PISTON_FORWARD_CHANNEL, INTAKE_DEPLOYER_PISTON_REVERSE_CHANNEL);*/
  private final double MAX = IntakeSubsystemConstants.MAX_INTAKE_MOTOR_SPEED;
  
  //Pneumatics: 2x double

  public IntakeSubsystem() {
    intakeWheelMotor.set(IntakeSubsystemConstants.INTAKE_MOTOR_CAN_ID);
    setName("Intake Subsystem");
    addChild("Intake Motor", intakeWheelMotor);

    
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }  

  //Find implementations for these methods!

  public void setIntakeMotorOn(double speed){
    /*
    if(speed <= -MAX|| speed >= MAX)
      intakeWheelMotor.set(speed);
    */
  }

  public void deployIntake(){
   // doubleSoleniod.set(Value.kForward);
  }

  public void retrackIntake(){
    //doubleSoleniod.set(Value.kReverse);
  }

 public void turnOffPistons(){
    //doubleSoleniod.set(Value.kOff);
  } 

  public void loadMagazine(){

  }


}

