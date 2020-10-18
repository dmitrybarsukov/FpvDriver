#ifndef _COMMAND_READER_H
#define _COMMAND_READER_H

#include <Arduino.h>
#include "Commands.h"
#define MAX_CMD_BYTES 32

class CommandIO
{
public:
    bool TryReadCommand(Command* command);
    void SendResponse(CommandType type, byte* data = nullptr, int byteCount = 0);
private:
    void InitCommand(Command* command, CommandType type, byte* data = nullptr, int byteCount = 0);
    byte commandBuffer[MAX_CMD_BYTES];
    int cmdByteCount = 0;
    long lastByteTime;
};

#endif // _COMMAND_READER_H
