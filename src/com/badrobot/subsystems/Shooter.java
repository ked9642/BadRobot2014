/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.badrobot.subsystems;

import com.badrobot.OI;
import com.badrobot.RobotMap;
import com.badrobot.commands.Shoot;
import com.badrobot.subsystems.interfaces.IShooter;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;

/**
 *
 * @author Isaac
 */
public class Shooter extends BadSubsystem implements IShooter
{
    private static Shooter instance;
    
    Solenoid engageSolenoid, disengageSolenoid;
    SpeedController winch;
    Encoder encoder;
    DigitalInput opticalSensorTop, opticalSensorMiddle, opticalSensorBottom;
    
    public int[] range = new int[4]; 
    public int aboveTop = 0, topToMid = 1, midToBottom = 2;
    public String area;
    public static Shooter getInstance()
    {
        if (instance == null)
        {
            instance = new Shooter();
        }
        return instance;
    }
    
    private Shooter()
    {
        
    }
    
    protected void initialize() 
    {
        winch = new Talon(RobotMap.winchController);
        engageSolenoid = new Solenoid(RobotMap.engageWinchSolenoid);
        disengageSolenoid = new Solenoid(RobotMap.disengageWinchSolenoid);
    }

    public String getConsoleIdentity() 
    {
        return "Shooter";
    }

    protected void initDefaultCommand() 
    {
        this.setDefaultCommand(new Shoot());
    }

    public void cockBack(double speed) 
    {
        
        if(opticalSensorBottom.get())
        {
            area = "belowBottom";
            range[midToBottom] =  encoder.get();
            encoder.reset();
            encoder.start();
        }
        else if(opticalSensorMiddle.get())
        {
            area = "midToBottom";
            range[topToMid] = encoder.get();
            encoder.reset();
            encoder.start();
        }
        else if(opticalSensorTop.get())
        {
            area = "topToMid";
            range[aboveTop] = encoder.get();
            encoder.start();    
        }
        
        winch.set(-speed);   
    }
    
    /**
     * 
     * @param area
     * @return 
     */
    public int getRangeLength(int area)
    {
        return range[area];
    }
    
    public String getCurrentArea()
    {
        return area;
    }

    public void disengageWinch() 
    {
        encoder.reset();
        area = "aboveTop";
        engageSolenoid.set(false);
        disengageSolenoid.set(true);
    }
    
    public void engageWinch()
    {
        disengageSolenoid.set(false);
        engageSolenoid.set(true);
    }
    
}
