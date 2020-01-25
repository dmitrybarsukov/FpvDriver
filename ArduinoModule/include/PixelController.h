#include <Arduino.h>
#include <Adafruit_NeoPixel.h>

#ifndef PIXELCONTROLLER_H
#define PIXELCONTROLLER_H

namespace Pixel
{
    typedef enum : byte
    {
        SOLID = 0,
        BLINK = 1,
        SMOOTH = 2,
        WALKING = 3,
        RAINBOW = 4,
    } PixelMode;

	uint32_t RED = 0xFF0000;
	uint32_t ORANGE = 0xFF3300;
	uint32_t YELLOW = 0xFF7700;
	uint32_t LIME = 0xFFFF00;
	uint32_t GREEN = 0x00FF00;
	uint32_t AQUA = 0x00FFFF;
	uint32_t BLUE = 0x0000FF;
	uint32_t PINK = 0xFF00FF;
	uint32_t BLACK = 0x000000;
	uint32_t WHITE = 0xFFFFFF;

	class PixelController
	{
	public:
        void attach(Adafruit_NeoPixel* px)
        {
			_px = px;
            _currMode = SOLID;
            _px->begin();
            _px->clear();
            _px->show();
        }

        void setMode(PixelMode mode, uint32_t clr0, uint32_t clr1 = 0, uint32_t clr2 = 0, uint32_t clr3 = 0)
        {
            colors[0] = clr0;
            colors[1] = clr1;
            colors[2] = clr2;
            colors[3] = clr3;
            // TODO
        }

		void process()
        {
            // TODO
        }

	private:
		Adafruit_NeoPixel* _px;
        PixelMode _currMode;
        uint32_t colors[4];

        static uint32_t interpolate(uint32_t clr1, uint32_t clr2, uint8_t cft)
        {
            uint32_t r1 = ((clr1 >> 16) & 0xFF) * cft / 255;
            uint32_t g1 = ((clr1 >> 8) & 0xFF) * cft / 255;
            uint32_t b1 = ((clr1 >> 0) & 0xFF) * cft / 255;
            uint32_t r2 = ((clr2 >> 16) & 0xFF) * (255 - cft) / 255;
            uint32_t g2 = ((clr2 >> 8) & 0xFF) * (255 - cft) / 255;
            uint32_t b2 = ((clr2 >> 0) & 0xFF) * (255 - cft) / 255;
            return ((r1 + r2) << 16) | ((g1 + g2) << 8) | ((b1 + b2) << 0);
        }
	};
}

#endif // PIXELCONTROLLER_H