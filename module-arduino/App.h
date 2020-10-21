#ifndef _APP_H
#define _APP_H

#include "MotorController.h"
#include "VoltageSensor.h"
#include "MessageIO.h"
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
    void ProcessMessage(Message* message);
    void ProcessGetVoltage();
    void ProcessSetLight(byte* data, int byteCount);
    void ProcessSetMotors(byte* data, int byteCount);
    void ProcessUnknownMessage(MessageType unknownType);
    MotorController* _motorController;
    NeoPixelController* _neoPixels;
    VoltageSensor* _batteryVoltageSensor;
    MessageIO* _messageIO;
    Message _message;
    Message* _pMessage;
};

#endif // _APP_H
