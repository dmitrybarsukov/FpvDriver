#ifndef _COMMANDS_H
#define _COMMANDS_H

typedef enum : byte
{
    NONE = 0x00,
    OK = 0xA0,
    GET_VOLTAGE = 0xC0,
    SET_MOTORS = 0xC1,
    SET_LIGHT = 0xC2,
    ERROR_TOO_MANY_BYTES = 0xE0,
    ERROR_COMMAND_INVALID = 0xE1,
    ERROR_PARAMS_INVALID = 0xE2
} CommandType;

typedef struct
{
    CommandType commandType;
    byte* data;
    int dataByteCount;
} Command;

#pragma pack(push, 1)
// typedef struct
// {

// };
#pragma pack(pop)


#endif // _COMMANDS_H