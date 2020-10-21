#ifndef _MESSAGE_READER_H
#define _MESSAGE_READER_H

#include <Arduino.h>
#include "Messages.h"
#define MAX_PACKET_BYTES 64

class MessageIO
{
public:
    bool TryReadMessage(Message* message);
    void SendMessage(MessageType type, byte* data = nullptr, int byteCount = 0);
private:
    void InitMessage(Message* message, MessageType type, byte* data = nullptr, int byteCount = 0);
    byte packetBuffer[MAX_PACKET_BYTES];
    int bufferByteCount = 0;
    long lastByteTime;
};

#endif // _MESSAGE_READER_H
