#include "MessageIO.h"

#define PACKET_TIMEOUT 10 // millis

boolean MessageIO::TryReadMessage(Message* message)
{
    auto now = millis();
    if(Serial.available())
    {
        lastByteTime = now;
        byte b = Serial.read();
        packetBuffer[bufferByteCount++] = b;
        if(bufferByteCount > MAX_PACKET_BYTES)
        {
            bufferByteCount = 0;
            SendMessage(MessageType::ERROR_TOO_MANY_BYTES);
            InitMessage(message, MessageType::NONE);
            return false;
        }
    }

    if(now - lastByteTime > PACKET_TIMEOUT && bufferByteCount > 0)
    {
        InitMessage(message, (MessageType)packetBuffer[0], packetBuffer + 1, bufferByteCount - 1);
        bufferByteCount = 0;
        return true;
    }

    InitMessage(message, MessageType::NONE);
    return false;
}

void MessageIO::SendMessage(MessageType type, byte* data, int byteCount)
{
    Serial.write((byte)(byteCount + 1));
    Serial.write((byte)type);
    if(data != nullptr)
        Serial.write(data, byteCount);
}

void MessageIO::InitMessage(Message* message, MessageType type, byte* data, int byteCount)
{
    message->messageType = type;
    message->data = data;
    message->dataByteCount = byteCount;
}
