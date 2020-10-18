#ifndef _APP_H
#define _APP_H

#include "MotorController.h"
#include "VoltageSensor.h"
#include "CommandIO.h"
#include "NeoPixelController.h"

class App
{
public:
    App(
        MotorController* motorController,
        NeoPixelController* neoPixels,
        VoltageSensor* batteryVoltageSensor
    );
    void Preprocess();
    void Process();
private:
    void ProcessCommand(Command* command);
    void ProcessGetVoltage();
    void ProcessSetLight(byte* data, int byteCount);
    void ProcessSetMotors(byte* data, int byteCount);
    void ProcessUnknownCommand(CommandType unknownType);
    MotorController* _motorController;
    NeoPixelController* _neoPixels;
    VoltageSensor* _batteryVoltageSensor;
    CommandIO* _commandIO;
    Command _response;
    Command* _pResponse;
};

#endif // _APP_H
