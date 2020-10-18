#include <Arduino.h>
#include "Motor.h"

Motor::Motor(byte dirPin, byte pwmPin, enum Reverse reverse)
{
    _dirPin = dirPin;
    _pwmPin = pwmPin;
    _reverse = (boolean)reverse;
    pinMode(_dirPin, OUTPUT);
    pinMode(_pwmPin, OUTPUT);
}

void Motor::Stop()
{
    digitalWrite(_dirPin, 0);
    digitalWrite(_pwmPin, 0);
}

void Motor::Run(int dirSpeed)
{
    boolean fw = dirSpeed >= 0;
    uint8_t pwm = constrain(abs(dirSpeed), 0, 255);
    if(fw ^ _reverse)
    {
        digitalWrite(_dirPin, 0);
        analogWrite(_pwmPin, pwm);
    }
    else
    {
        digitalWrite(_dirPin, 1);
        analogWrite(_pwmPin, 255 - pwm);
    }
}
