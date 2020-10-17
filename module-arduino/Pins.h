#include <Arduino.h>

#ifndef _PINS_H
#define _PINS_H

namespace Pins
{
    const byte LMOTOR_DIR = 3;
    const byte LMOTOR_PWM = 5;
    const byte RMOTOR_DIR = 4;
    const byte RMOTOR_PWM = 6;
    const byte SERVO_HOR = 10;
    const byte SERVO_VER = 9;
    const byte LEDS = 8;
    const byte LASER = 7;
    const byte BATTERY_VOLTAGE = A0;
}

#endif // _PINS_H
