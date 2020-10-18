#ifndef _MOTOR_CONTROLLER_H
#define _MOTOR_CONTROLLER_H

#include "Motor.h"

class MotorController
{
public:
    MotorController(Motor* left, Motor* right, unsigned long switchOffMillis);
    void Process();
    void SetSpeed(int left, int right);

private:
    Motor* _left;
    Motor* _right;
    unsigned long _switchOffMillis;
};

#endif // _MOTOR_CONTROLLER_H
