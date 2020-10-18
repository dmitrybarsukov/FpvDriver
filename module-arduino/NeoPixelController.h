#ifndef _NEOPIXEL_CONTROLLER_H
#define _NEOPIXEL_CONTROLLER_H

#include <Adafruit_NeoPixel.h>

#pragma pack(push, 1)
typedef struct
{
    byte r;
    byte g;
    byte b;
} Color;
#pragma pack(pop)

class NeoPixelController
{
public:
    NeoPixelController(Adafruit_NeoPixel* neoPixel, unsigned long  switchOffMillis);
    void Fill(Color* color);
    void Write(Color* colorArray);
    void Clear();
    void Process();
private:
    uint32_t ColorToUint32(Color* color);
    Adafruit_NeoPixel* _neoPixel;
    unsigned long _switchOffMillis;
    unsigned long _lastUpdateTime;
};

#endif // _NEOPIXEL_CONTROLLER_H
