#include "MessageIO.h"

MessageIO::MessageIO()
{
    ClearBuffer();
}

boolean MessageIO::TryReadMessage(Message* message)
{
    auto now = millis();
    if(Serial.available())
    {
        byte b = Serial.read();
        lastByteTime = now;
        if(expectedBufferLength == NO_BUFFER)
        {
            expectedBufferLength = b;
        }
        else
        {
            packetBuffer[bufferByteCount++] = b;
            if(bufferByteCount == expectedBufferLength)
            {
                InitMessage(message, (MessageType)packetBuffer[0], &packetBuffer[1], bufferByteCount - 1);
                ClearBuffer();
                return true;
            }
            else if(bufferByteCount >= MAX_PACKET_BYTES)
            {
                ClearBuffer();
                SendMessage(MessageType::ERROR_TOO_MANY_BYTES);
                InitMessage(message, MessageType::NONE);
                return false;
            }
        }
    }

    if(now - lastByteTime > PACKET_TIMEOUT && expectedBufferLength != NO_BUFFER)
    {
        ClearBuffer();
        SendMessage(MessageType::ERROR_PACKET_TIMEOUT);
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

void MessageIO::ClearBuffer()
{
    expectedBufferLength = NO_BUFFER;
    bufferByteCount = 0;
}
