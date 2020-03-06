/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import frc.robot.Constants.ControlPanelRotatorSubsystemConstants;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ControlPanelRotatorSubsystem extends SubsystemBase {
  // Color Sensor - Resource:
  // https://github.com/REVrobotics/Color-Sensor-v3-Examples/blob/master/Java/Color%20Match/src/main/java/frc/robot/Robot.java
  private final I2C.Port i2cPort = I2C.Port.kOnboard;
  private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);
  private final ColorMatch m_colorMatcher = new ColorMatch();

  private final CANSparkMax colorManipulatorMotor = new CANSparkMax(4, MotorType.kBrushed);

  private final Color kBlueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
  private final Color kGreenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
  private final Color kRedTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);
  private final Color kYellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);
  Joystick bStick = new Joystick(0);

  private final int solenoidChannel = ControlPanelRotatorSubsystemConstants.CONTROL_PANEL_SOLENOID_ID;

  private final Solenoid solenoid = new Solenoid(solenoidChannel);

  /**
   * Creates a new ControlPanelRotatorSubsystem.
   */
  public ControlPanelRotatorSubsystem() {
    /*---------------------------------------------------------------------------Color sensor code--*/
    // Gets the normalized color value from the sensor
    m_colorMatcher.addColorMatch(kBlueTarget);
    m_colorMatcher.addColorMatch(kGreenTarget);
    m_colorMatcher.addColorMatch(kRedTarget);
    m_colorMatcher.addColorMatch(kYellowTarget);
    /*----------------------------------------------------------------------------------------------*/
    SmartDashboard.putString("Color to Get", "Red");
    if (SmartDashboard.getString("Color to Get", "120") == "120") {
      SmartDashboard.putString("Color to Get", "Red");
      SmartDashboard.updateValues();
    }
    setName("Control Panel Subsysyem");
    SmartDashboard.updateValues();
  }

  @Override
  public void periodic() {
  }

  public void deployControlPanelRotator() {
    solenoid.set(true);
  }

  public void retractControlPanelRotator() {
    solenoid.set(false);
  }

  /**
   * moves the control panel rotator motor
   */
  public void moveControlPanelRotator() {
    // motor code goes here
    SmartDashboard.putString("Color manipulator rotating?", "True");
    double manipulatorSpeed = .7;
    colorManipulatorMotor.set(manipulatorSpeed);

  }

  /**
   * starts the control panel rotator, starts searching for color
   */
  public void startControlPanelRotator() {
    deployControlPanelRotator();
    moveControlPanelRotator();
    System.out.println("button pressed, checking for right color");
  }

  /**
   * stops the control panel rotator
   */
  public void stopControlPanelRotator() {
    retractControlPanelRotator();
    String colToGet = SmartDashboard.getString("Color to Get", "120");
    System.out.println("the robot now detects " + colToGet);
    SmartDashboard.putString("Color manipulator rotating?", "False");
    colorManipulatorMotor.set(0);

  }

  /**
   * checks if the current color detected equals the color wanted;
   * 
   * @return true or false
   */
  public boolean checkColor() {
    Color detectedColor = m_colorSensor.getColor();

    String currentColor;
    ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);

    String colorToCheck = colorToGet();

    // Match the color detected
    if (match.color == kBlueTarget) {
      currentColor = "Blue";
    } else if (match.color == kRedTarget) {
      currentColor = "Red";
    } else if (match.color == kGreenTarget) {
      currentColor = "Green";
    } else if (match.color == kYellowTarget) {
      currentColor = "Yellow";
    } else {
      currentColor = "Unknown";
    }
    // Displays the color in SmartDashboard
    SmartDashboard.putNumber("Red", detectedColor.red);
    SmartDashboard.putNumber("Green", detectedColor.green);
    SmartDashboard.putNumber("Blue", detectedColor.blue);
    SmartDashboard.putNumber("Confidence", match.confidence);
    SmartDashboard.putString("Detected Color", currentColor);
    if (currentColor.compareTo(colorToCheck) == 0) {
      return true;
    } else {
      return false;
    }

  }

  /**
   * Checks what color is wanted from the smartdashboard
   */
  public String colorToGet() {

    return SmartDashboard.getString("Color to Get", "120");
  }
}
