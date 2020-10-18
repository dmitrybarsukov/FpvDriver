#ifndef _MOTOR_H
#define _MOTOR_H

#include <Arduino.h>

enum class Reverse
{
    FALSE = 0,
    TRUE = 1
};

class Motor
{
public:
    Motor(byte dirPin, byte pwmPin, enum Reverse reverse);
    void Stop();
    void Run(int dirSpeed);

private:
    byte _dirPin;
    byte _pwmPin;
    boolean _reverse;
};

#endif // _MOTOR_H
