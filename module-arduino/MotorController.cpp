#include "MotorController.h"

#define SPEED_UPDATE_PERIOD_MILLIS 5
#define SPEED_UPDATE_STEP_PER_PERIOD 10

MotorController::MotorController(Motor* left, Motor* right, unsigned long switchOffMillis)
{
    _left = left;
    _right = right;
    _switchOffMillis = switchOffMillis;
    _currentLeftSpeed = 0;
    _currentRightSpeed = 0;
    _targetLeftSpeed = 0;
    _targetRightSpeed = 0;
    _lastUpdateTime = millis();
    _lastSpeedChangeMillis = millis();
}

void MotorController::Process()
{
    auto now = millis();
    if(now > _lastUpdateTime + _switchOffMillis)
    {
        Stop();
        return;
    }

    if(now > _lastSpeedChangeMillis + SPEED_UPDATE_PERIOD_MILLIS)
    {
        _lastSpeedChangeMillis = now;
        if(UpdateValue(&_currentLeftSpeed, _targetLeftSpeed))
            _left->Run(_currentLeftSpeed);
        if(UpdateValue(&_currentRightSpeed, _targetRightSpeed))
            _right->Run(_currentRightSpeed);
    }
}

void MotorController::SetSpeed(int left, int right)
{
    _targetLeftSpeed = constrain(left, -255, 255);
    _targetRightSpeed = constrain(right, -255, 255);
    _lastUpdateTime = millis();
}

void MotorController::Stop()
{
    _lastUpdateTime = millis();
    _currentLeftSpeed = 0;
    _currentRightSpeed = 0;
    _targetLeftSpeed = 0;
    _targetRightSpeed = 0;
    _left->Stop();
    _right->Stop();
}

bool MotorController::UpdateValue(int* current, int target)
{
    if(target < *current)
    {
        auto newValue = *current - SPEED_UPDATE_STEP_PER_PERIOD;
        *current = max(newValue, target);
        return true;
    }
    else if(target > *current)
    {
        auto newValue = *current + SPEED_UPDATE_STEP_PER_PERIOD;
        *current = min(newValue, target);
        return true;
    }
    return false;
}