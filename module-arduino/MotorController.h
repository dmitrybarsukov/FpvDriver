#ifndef _MOTOR_CONTROLLER_H
#define _MOTOR_CONTROLLER_H

#include "Motor.h"

class MotorController
{
public:
    MotorController(Motor* left, Motor* right, unsigned long switchOffMillis);
    void Process();
    void SetSpeed(int left, int right);
    void Stop();

private:
    bool UpdateValue(int* current, int target);
    Motor* _left;
    Motor* _right;
    int _targetLeftSpeed;
    int _targetRightSpeed;
    int _currentLeftSpeed;
    int _currentRightSpeed;
    unsigned long _switchOffMillis;
    unsigned long _lastUpdateTime;
    unsigned long _lastSpeedChangeMillis;
};

#endif // _MOTOR_CONTROLLER_H
