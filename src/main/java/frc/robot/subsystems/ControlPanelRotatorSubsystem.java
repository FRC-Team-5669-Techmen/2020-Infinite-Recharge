/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ControlPanelRotatorSubsystem extends SubsystemBase {
  // Color Sensor - Resource:
  // https://github.com/REVrobotics/Color-Sensor-v3-Examples/blob/master/Java/Color%20Match/src/main/java/frc/robot/Robot.java
  private final I2C.Port i2cPort = I2C.Port.kOnboard;
  private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);
  private final ColorMatch m_colorMatcher = new ColorMatch();

  private final Color kBlueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
  private final Color kGreenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
  private final Color kRedTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);
  private final Color kYellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);
  private boolean buttonEnabled = false;
  private boolean buttonPressed = false;

  Joystick bStick = new Joystick(0);

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
    if(SmartDashboard.getString("Color to Get", "120") == "120"){
      SmartDashboard.putString("Color to Get", "Red");
      SmartDashboard.updateValues();
    }
    
    SmartDashboard.updateValues();
  }

  @Override
  public void periodic() {
    if (bStick.getRawButton(1)) {
      displayCurrentColor();
      if (buttonEnabled == false) {
        buttonEnabled = true;
        System.out.println(buttonEnabled);
      }
    }
    displayCurrentColor();
  }

  /**
   * Gets current color sensor color and outputs it to SmartDashboard
   */
  public void displayCurrentColor() {
    if (buttonEnabled) {
      Color detectedColor = m_colorSensor.getColor();
      String colToGet = SmartDashboard.getString("Color to Get", "120");

      String colorString;
      ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);

      // Match the color detected
      if (match.color == kBlueTarget) {
        colorString = "Blue";
      } else if (match.color == kRedTarget) {
        colorString = "Red";
      } else if (match.color == kGreenTarget) {
        colorString = "Green";
      } else if (match.color == kYellowTarget) {
        colorString = "Yellow";
      } else {
        colorString = "Unknown";
      }
      // Displays the color in SmartDashboard
      SmartDashboard.putNumber("Red", detectedColor.red);
      SmartDashboard.putNumber("Green", detectedColor.green);
      SmartDashboard.putNumber("Blue", detectedColor.blue);
      SmartDashboard.putNumber("Confidence", match.confidence);
      SmartDashboard.putString("Detected Color", colorString);
      if (colorString.compareTo(colToGet) == 0) {
        buttonEnabled = false;
        System.out.println("the robot now detects " + colToGet);
      }
    }
  }
}
