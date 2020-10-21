#ifndef _MESSAGE_READER_H
#define _MESSAGE_READER_H

#include <Arduino.h>
#include "Messages.h"

#define HEADER_SIZE sizeof(MessageHeader)
#define PACKET_TIMEOUT 1000
#define MAX_PACKET_BYTES 64
#define NO_BUFFER (MAX_PACKET_BYTES + 1)

class MessageIO
{
public:
    MessageIO();
    bool TryReadMessage(Message* message);
    void SendMessage(MessageType type, byte* data = nullptr, int byteCount = 0);
private:
    void InitMessage(Message* message, MessageType type, byte* data = nullptr, int byteCount = 0);
    void ClearBuffer();
    byte expectedBufferLength;
    byte packetBuffer[MAX_PACKET_BYTES];
    byte bufferByteCount = 0;
    long lastByteTime;
};

#endif // _MESSAGE_READER_H
