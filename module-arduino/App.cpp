#include "App.h"
#include "ArrayUtils.h"

App::App(
    MotorController* motorController,
    NeoPixelController* neoPixels,
    VoltageSensor* batteryVoltageSensor
)
{
    _motorController = motorController;
    _neoPixels = neoPixels;
    _batteryVoltageSensor = batteryVoltageSensor;
    _messageIO = new MessageIO();
    _pMessage = &_message;
}

void App::Preprocess()
{
    _motorController->Process();
    _neoPixels->Process();
}

void App::Process()
{
    Message msg;
    if(_messageIO->TryReadMessage(&msg))
    {
        ProcessMessage(&msg);
    }
}

void App::ProcessMessage(Message* message)
{
    switch (message->messageType)
    {
    case MessageType::GET_VOLTAGE:
        ProcessGetVoltage();
        break;
    case MessageType::SET_LIGHT:
        ProcessSetLight(message->data, message->dataByteCount);
        break;
    case MessageType::SET_MOTORS:
        ProcessSetMotors(message->data, message->dataByteCount);
        break;
    default:
        ProcessUnknownMessage(message->messageType);
        break;
    }
}

void App::ProcessGetVoltage()
{
    byte responseBody[2];
    auto voltage = _batteryVoltageSensor->ReadMillivolts();
    auto size = PutToArray(responseBody, 0, voltage);
    _messageIO->SendMessage(MessageType::GET_VOLTAGE, responseBody, size);
}

void App::ProcessSetLight(byte* data, int byteCount)
{
    byte responseBody[1] = { (byte)MessageType::SET_LIGHT };
    if(byteCount == 0)
    {
        _neoPixels->Clear();
        _messageIO->SendMessage(MessageType::OK, responseBody, 1);
    }
    if(byteCount == 3)
    {
        _neoPixels->Fill((Color*)data);
        _messageIO->SendMessage(MessageType::OK, responseBody, 1);
    }
    else if(byteCount == 12)
    {
        _neoPixels->Write((Color*)data);
        _messageIO->SendMessage(MessageType::OK, responseBody, 1);
    }
    else
    {
        _messageIO->SendMessage(MessageType::ERROR_PARAMS_INVALID, responseBody, 1);
    }
}

void App::ProcessSetMotors(byte* data, int byteCount)
{
    byte responseBody[1] = { (byte)MessageType::SET_MOTORS };
    if(byteCount == 0)
    {
        _motorController->Stop();
        _messageIO->SendMessage(MessageType::OK, responseBody, 1);
    }
    else if(byteCount == 4)
    {
        int left = ReadInt16(data, 0);
        int right = ReadInt16(data, 2);
        _motorController->SetSpeed(left, right);
        _messageIO->SendMessage(MessageType::OK, responseBody, 1);
    }
    else
    {
        _messageIO->SendMessage(MessageType::ERROR_PARAMS_INVALID, responseBody, 1);
    }
}

void App::ProcessUnknownMessage(MessageType unknownType)
{
    byte responseBody[1] = { (byte)unknownType };
    _messageIO->SendMessage(MessageType::ERROR_MESSAGE_INVALID, responseBody, 1);
}
