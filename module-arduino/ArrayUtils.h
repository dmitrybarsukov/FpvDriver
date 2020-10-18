#ifndef _ARRAY_UTILS_H
#define _ARRAY_UTILS_H

#include <Arduino.h>

int PutToArray(byte* array, int position, uint8_t data)
{
    array[position] = data;
    return 1;
}

int PutToArray(byte* array, int position, int8_t data)
{
    array[position] = data;
    return 1;
}

int PutToArray(byte* array, int position, uint16_t data)
{
    array[position + 0] = data >> 0;
    array[position + 1] = data >> 8;
    return 2;
}

int PutToArray(byte* array, int position, int16_t data)
{
    array[position + 0] = data >> 0;
    array[position + 1] = data >> 8;
    return 2;
}

int PutToArray(byte* array, int position, uint32_t data)
{
    array[position + 0] = data >> 0;
    array[position + 1] = data >> 8;
    array[position + 2] = data >> 16;
    array[position + 3] = data >> 24;
    return 4;
}

int PutToArray(byte* array, int position, int32_t data)
{
    array[position + 0] = data >> 0;
    array[position + 1] = data >> 8;
    array[position + 2] = data >> 16;
    array[position + 3] = data >> 24;
    return 4;
}

int16_t ReadInt16(byte* array, int position)
{
    return ((int16_t)array[position + 0] << 0)
         | ((int16_t)array[position + 1] << 8);
}

uint16_t ReadUInt16(byte* array, int position)
{
    return ((uint16_t)array[position + 0] << 0)
         | ((uint16_t)array[position + 1] << 8);
}

int32_t ReadInt32(byte* array, int position)
{
    return ((int32_t)array[position + 0] << 0)
         | ((int32_t)array[position + 1] << 8)
         | ((int32_t)array[position + 2] << 16)
         | ((int32_t)array[position + 3] << 24);
}

uint32_t ReadUInt32(byte* array, int position)
{
    return ((uint32_t)array[position + 0] << 0)
         | ((uint32_t)array[position + 1] << 8)
         | ((uint32_t)array[position + 2] << 16)
         | ((uint32_t)array[position + 3] << 24);
}

#endif // _ARRAY_UTILS_H
