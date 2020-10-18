#include "MotorController.h"

MotorController::MotorController(Motor* left, Motor* right, unsigned long switchOffMillis)
{
    _left = left;
    _right = right;
    _switchOffMillis = switchOffMillis;
}

void MotorController::Process()
{
    
}

void MotorController::SetSpeed(int left, int right)
{

}