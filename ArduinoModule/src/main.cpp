#include <Arduino.h>
#include <Adafruit_NeoPixel.h>
#include <Adafruit_TiCoServo.h>
#include "Pins.h"
#include "AnalogIn.h"
#include "Output.h"
#include "Motor.h"
#include "PixelController.h"

#define MAX_CMD_CHARS 64
#define MAX_CMD_PARAMS 4
#define MAX_PARAM_CHARS 16

Periph::AnalogIn batVolt(Pins::BATTERY_VOLTAGE);
Periph::Output laser(Pins::LASER);
Periph::Motor motorL(Pins::LMOTOR_DIR, Pins::LMOTOR_PWM);
Periph::Motor motorR(Pins::RMOTOR_DIR, Pins::RMOTOR_PWM);
Adafruit_TiCoServo servoH;
Adafruit_TiCoServo servoV;
Adafruit_NeoPixel neopixels(4, Pins::LEDS);
Pixel::PixelController pixels;

void tryProcessCommand(char*);
int splitCmd(char*, char*, char**);

void setup() {
    servoH.attach(Pins::SERVO_HOR);
    servoV.attach(Pins::SERVO_VER);
    pixels.attach(&neopixels);
    Serial.begin(115200);
}

void loop() {
    static char command[MAX_CMD_CHARS] = {};
    static int cmdCharCnt = 0;

    if(Serial.available())
    {
        char ch = Serial.read();
        command[cmdCharCnt++] = (ch == '\n' ? '\0' : ch);
        if(cmdCharCnt > MAX_CMD_CHARS)
        {
            cmdCharCnt = 0;
            Serial.println(F("ERR:CMDTOOLONG"));
        }
        else
        {
            if(ch == '\n')
            {
                char onlyCmd[MAX_CMD_CHARS] = {};
                char params[MAX_CMD_PARAMS][MAX_PARAM_CHARS] = {};
                int paramCnt = splitCmd(command, onlyCmd, (char**)params);
            }
                tryProcessCommand(command);
        }
    }

}

void tryProcessCommand(char* cmd)
{

}

int splitCmd(char* str, char* cmd, char** params)
{
    int idx;
    int paramsCnt = 0;
    for(idx = 0; idx < MAX_CMD_CHARS; idx++)
    {
        char ch = str[idx];
        if(ch= '\0' && ch != ':')
        {
            // TODO
        }
        else
        {
            break;
        }
    }
}
