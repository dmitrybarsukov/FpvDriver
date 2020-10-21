#ifndef _MESSAGES_H
#define _MESSAGES_H

typedef enum : byte
{
    NONE = 0x00,
    OK = 0xA0,
    GET_VOLTAGE = 0xC0,
    SET_MOTORS = 0xC1,
    SET_LIGHT = 0xC2,
    ERROR_TOO_MANY_BYTES = 0xE0,
    ERROR_MESSAGE_INVALID = 0xE1,
    ERROR_PARAMS_INVALID = 0xE2,
    ERROR_PACKET_TIMEOUT = 0xE3
} MessageType;

typedef struct
{
    MessageType messageType;
    byte* data;
    int dataByteCount;
} Message;

#endif // _MESSAGES_H