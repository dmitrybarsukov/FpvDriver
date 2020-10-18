#include <Arduino.h>
#include "VoltageSensor.h"

#define VCC_MILLIVOLTS 5000
#define MAX_ADC 1023
#define ADC_TO_MILLIVOLTS ((float)VCC_MILLIVOLTS / MAX_ADC)

VoltageSensor::VoltageSensor(byte pin)
{
    _pin = pin;
    pinMode(_pin, INPUT);
}

int VoltageSensor::ReadMillivolts()
{
    return analogRead(_pin) * ADC_TO_MILLIVOLTS;
}
