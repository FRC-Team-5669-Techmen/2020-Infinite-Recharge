/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants.MagazineSubsystemConstants;

public class MagazineSubsystem extends SubsystemBase {
  /**
   * Creates a new Magazine.
   */

  private final WPI_VictorSPX magazineRotatorMotor = new WPI_VictorSPX(MagazineSubsystemConstants.MAGAZINE_ROTATOR_CAN_ID);
  private final PowerDistributionPanel m_PDP = new PowerDistributionPanel();
  
  
  public MagazineSubsystem() {
    super();
    magazineRotatorMotor.set(0.0);

    setName("Magazine Subsysyem");//default name
    addChild("Magazie Rotator Motor", magazineRotatorMotor);


  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    preventJams();
  }

  public void turnOnRotatorClockwise(double speed){
    Math.abs(speed);
    magazineRotatorMotor.set(speed);
  }

  public void turnOnRotatorClockwise(){
    magazineRotatorMotor.set(0.3);
  }

  public void turnOnRotatorCounterClockwise(double speed){
    Math.abs(speed);
    magazineRotatorMotor.set(-speed);
  }

  public void turnOnRotatorCounterClockwise(){
    magazineRotatorMotor.set(-0.3);
  }

  public void feedTurret(){
    turnOnRotatorClockwise(0.75);
  }

  public void feedMagazine(){
    turnOnRotatorClockwise(0.2);


  }


  public void revereseMagazine(){
    turnOnRotatorCounterClockwise(0.5);
  }


  public void turnOffRotator(){
    magazineRotatorMotor.set(0.0);
   // magazineRotatorMotor.stopMotor(); //TODO see if this does the same thing
  }

  public double getMagazineCurrentDrawFromPDP(){
    return m_PDP.getCurrent(2); //peter says it is a voltage dti
  }

  public void preventJams(){
    
/*
    if (m_PDP.getCurrent(3) > normalCurrent)
    {
      getCurrentCommand().cancel(); //stop moving the turret
          //go other way for some time
     turnOnRotatorClockwise();
     Timer.delay(1.0);
     turnOffRotator();

    }
    */
 
  }





}
