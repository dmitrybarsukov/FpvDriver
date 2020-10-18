#ifndef _VOLTAGE_SENSOR_H
#define _VOLTAGE_SENSOR_H

class VoltageSensor
{
public:
    VoltageSensor(byte pin);
    int ReadMillivolts();

private:
    byte _pin;
};

#endif // _VOLTAGE_SENSOR_H
