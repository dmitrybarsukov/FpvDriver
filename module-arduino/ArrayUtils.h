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
    array[position + 0] = data;
    array[position + 1] = data >> 8;
    return 2;
}

int PutToArray(byte* array, int position, int16_t data)
{
    array[position + 0] = data;
    array[position + 1] = data >> 8;
    return 2;
}

int PutToArray(byte* array, int position, uint32_t data)
{
    array[position + 0] = data;
    array[position + 1] = data >> 8;
    array[position + 2] = data >> 16;
    array[position + 3] = data >> 24;
    return 4;
}

int PutToArray(byte* array, int position, int32_t data)
{
    array[position + 0] = data;
    array[position + 1] = data >> 8;
    array[position + 2] = data >> 16;
    array[position + 3] = data >> 24;
    return 4;
}

#endif // _ARRAY_UTILS_H
