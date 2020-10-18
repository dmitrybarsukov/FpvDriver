#include "CommandIO.h"

#define COMMAND_TIMEOUT 10 // millis

boolean CommandIO::TryReadCommand(Command* command)
{
    auto now = millis();
    if(Serial.available())
    {
        lastByteTime = now;
        byte b = Serial.read();
        commandBuffer[cmdByteCount++] = b;
        if(cmdByteCount > MAX_CMD_BYTES)
        {
            cmdByteCount = 0;
            InitCommand(command, CommandType::ERROR_TOO_MANY_BYTES);
            return true;
        }
    }

    if(now - lastByteTime > COMMAND_TIMEOUT && cmdByteCount > 0)
    {
        InitCommand(command, (CommandType)commandBuffer[0], commandBuffer + 1, cmdByteCount - 1);
        cmdByteCount = 0;
        return true;
    }

    InitCommand(command, CommandType::NONE);
    return false;
}

void CommandIO::SendResponse(CommandType type, byte* data, int byteCount)
{
    Serial.write((byte)type);
    if(data != nullptr)
        Serial.write(data, byteCount);
}

void CommandIO::InitCommand(Command* command, CommandType type, byte* data, int byteCount)
{
    command->commandType = type;
    command->data = data;
    command->dataByteCount = byteCount;
}
