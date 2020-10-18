#include <Arduino.h>

#ifndef _PINS_H
#define _PINS_H

enum Pins : byte
{
    MOTOR1_DIRECTION = 6,
    MOTOR1_PWM = 5,
    MOTOR2_DIRECTION = 4,
    MOTOR2_PWM = 3,
    SERVO1 = 9,
    SERVO2 = 10,
    NEOPIXELS = 8,
    LASER = 7,
    BATTERY_VOLTAGE_SENSE = A0
};

#endif // _PINS_H
