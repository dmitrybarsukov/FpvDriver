#include <Arduino.h>
#include <Adafruit_NeoPixel.h>
#include <Adafruit_TiCoServo.h>
#include "Pins.h"
#include "AnalogIn.h"
#include "Output.h"
#include "Motor.h"
#include "PixelController.h"

Periph::AnalogIn batVolt(Pins::BATTERY_VOLTAGE);
Periph::Output laser(Pins::LASER);
Periph::Motor motorL(Pins::LMOTOR_DIR, Pins::LMOTOR_PWM);
Periph::Motor motorR(Pins::RMOTOR_DIR, Pins::RMOTOR_PWM);
Adafruit_TiCoServo servoH;
Adafruit_TiCoServo servoV;
Adafruit_NeoPixel neopixels(4, Pins::LEDS);
Pixel::PixelController pixels;

void tryProcessCommand(char*);
void notifyCommandTooLong();

void setup() {
    servoH.attach(Pins::SERVO_HOR);
    servoV.attach(Pins::SERVO_VER);
    pixels.attach(&neopixels);
    Serial.begin(115200);
}

void loop() {
    static char command[64] = {};
    static int cmdCharCnt = 0;

    if(Serial.available())
    {
        char ch = Serial.read();
        if(ch == '\n')
        {
            command[cmdCharCnt++] = '\0';
            tryProcessCommand(command);
        }
    }

}

void tryProcessCommand(char* cmd)
{

}

void notifyCommandTooLong()
{

}