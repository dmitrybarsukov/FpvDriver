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
    _commandIO = new CommandIO();
    _pResponse = &_response;
}

void App::Preprocess()
{
    _motorController->Process();
    _neoPixels->Process();
}

void App::Process()
{
    Command cmd;
    if(_commandIO->TryReadCommand(&cmd))
    {
        ProcessCommand(&cmd);
    }
}

void App::ProcessCommand(Command* command)
{
    switch (command->commandType)
    {
    case CommandType::GET_VOLTAGE:
        ProcessGetVoltage();
        break;
    case CommandType::SET_LIGHT:
        ProcessSetLight(command->data, command->dataByteCount);
        break;
    case CommandType::SET_MOTORS:
        ProcessSetMotors(command->data, command->dataByteCount);
        break;
    default:
        ProcessUnknownCommand(command->commandType);
        break;
    }
}

void App::ProcessGetVoltage()
{
    byte responseBody[2];
    auto voltage = _batteryVoltageSensor->ReadMillivolts();
    auto size = PutToArray(responseBody, 0, voltage);
    _commandIO->SendResponse(CommandType::GET_VOLTAGE, responseBody, size);
}

void App::ProcessSetLight(byte* data, int byteCount)
{
    byte responseBody[1] = { (byte)CommandType::SET_LIGHT };
    if(byteCount == 0)
    {
        _neoPixels->Clear();
        _commandIO->SendResponse(CommandType::OK, responseBody, 1);
    }
    if(byteCount == 3)
    {
        _neoPixels->Fill((Color*)data);
        _commandIO->SendResponse(CommandType::OK, responseBody, 1);
    }
    else if(byteCount == 12)
    {
        _neoPixels->Write((Color*)data);
        _commandIO->SendResponse(CommandType::OK, responseBody, 1);
    }
    else
    {
        _commandIO->SendResponse(CommandType::ERROR_PARAMS_INVALID, responseBody, 1);
    }
}

void App::ProcessSetMotors(byte* data, int byteCount)
{
    byte responseBody[1] = { (byte)CommandType::SET_MOTORS };
    if(byteCount == 0)
    {
        _motorController->Stop();
        _commandIO->SendResponse(CommandType::OK, responseBody, 1);
    }
    else if(byteCount == 4)
    {
        int left = ReadInt16(data, 0);
        int right = ReadInt16(data, 2);
        _motorController->SetSpeed(left, right);
        _commandIO->SendResponse(CommandType::OK, responseBody, 1);
    }
    else
    {
        _commandIO->SendResponse(CommandType::ERROR_PARAMS_INVALID, responseBody, 1);
    }
}

void App::ProcessUnknownCommand(CommandType unknownType)
{
    byte responseBody[1] = { (byte)unknownType };
    _commandIO->SendResponse(CommandType::ERROR_COMMAND_INVALID, responseBody, 1);
}
