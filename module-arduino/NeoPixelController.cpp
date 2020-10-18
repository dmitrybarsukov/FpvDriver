#include "NeoPixelController.h"

NeoPixelController::NeoPixelController(Adafruit_NeoPixel* neoPixel, unsigned long switchOffMillis)
{
    _neoPixel = neoPixel;
    _switchOffMillis = switchOffMillis;
    _lastUpdateTime = millis();
    _neoPixel->begin();
    _neoPixel->clear();
    _neoPixel->show();
}

void NeoPixelController::Fill(Color* color)
{
    _lastUpdateTime = millis();
    _neoPixel->fill(ColorToUint32(color));
    _neoPixel->show();
}

void NeoPixelController::Write(Color* colorArray)
{
    _lastUpdateTime = millis();
    for(int i = 0; i < 4; i++)
        _neoPixel->setPixelColor(i, ColorToUint32(colorArray + i));
    _neoPixel->show();
}

void NeoPixelController::Clear()
{
    _lastUpdateTime = millis();
    _neoPixel->clear();
    _neoPixel->show();
}

void NeoPixelController::Process()
{
    if(millis() > _lastUpdateTime + _switchOffMillis)
    {
        Clear();
    }
}

uint32_t NeoPixelController::ColorToUint32(Color* color)
{
    return ((uint32_t)color->r << 16) | ((uint32_t)color->g << 8) | ((uint32_t)color->b << 0);
}
