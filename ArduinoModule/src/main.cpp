#include <Arduino.h>
#include <Adafruit_NeoPixel.h>
#include <Adafruit_TiCoServo.h>
#include "Pins.h"
#include "AnalogIn.h"
#include "Output.h"
#include "Motor.h"

#define PRINTSERIAL(a) Serial.println(F(a))

#define MAX_CMD_CHARS 64
#define MAX_CMD_PARAMS 4
#define CMD_END '\0'
#define NEWLINE '\n'
#define CMD_DELIMITER ':'
#define PARAMS_DELIMITER ';'

Periph::AnalogIn batVolt(Pins::BATTERY_VOLTAGE);
Periph::Output laser(Pins::LASER);
Periph::Motor motorL(Pins::LMOTOR_DIR, Pins::LMOTOR_PWM);
Periph::Motor motorR(Pins::RMOTOR_DIR, Pins::RMOTOR_PWM);
Adafruit_TiCoServo servoH;
Adafruit_TiCoServo servoV;
Adafruit_NeoPixel pixels(4, Pins::LEDS);

int tryPreprocessCommand(char*, int*);
void processCommand(char*, int*, int);
void processGetVoltage(char*, int*, int);
void processSetMotors(char*, int*, int);
void processSetServos(char*, int*, int);
void processSetLaser(char*, int*, int);
void processSetLight(char*, int*, int);

void setup() {
    servoH.attach(Pins::SERVO_HOR);
    servoV.attach(Pins::SERVO_VER);
    pixels.begin();
    Serial.begin(115200);
}

void loop() {
    static char command[MAX_CMD_CHARS] = {};
    static int cmdCharCnt = 0;

    if(Serial.available())
    {
        char ch = Serial.read();
        command[cmdCharCnt++] = (ch == NEWLINE ? CMD_END : ch);
        if(cmdCharCnt > MAX_CMD_CHARS)
        {
            cmdCharCnt = 0;
            PRINTSERIAL("ERR:CMDTOOLONG");
        }
        else
        {
            if(ch == '\n')
            {
                cmdCharCnt = 0;
                int paramIdx[MAX_CMD_PARAMS] = {};
                int paramCnt = tryPreprocessCommand(command, paramIdx);
                if(paramCnt >= 0)
                    processCommand(command, paramIdx, paramCnt);
                else
                    PRINTSERIAL("ERR:TOOMANYPARAMS");
            }
        }
    }
}

int tryPreprocessCommand(char* str, int* paramIdx)
{
    int count = 0;
    for(int i = 0; i < MAX_CMD_CHARS; i++)
    {
        if(str[i] == CMD_END)
            break;
        
        if(str[i] == CMD_DELIMITER || str[i] == PARAMS_DELIMITER)
        {
            if(count < MAX_CMD_PARAMS)
            {
                paramIdx[count++] = i + 1;
                str[i] = CMD_END;
            }
            else
            {
                count = -1;
                break;
            }
        }
    }
    return count;
}

void processCommand(char* str, int* paramIdx, int paramCnt)
{
    if(strcasecmp(str, "GETVOLTAGE") == 0)
        processGetVoltage(str, paramIdx, paramCnt);
    else if(strcasecmp(str, "SETMOTORS") == 0)
        processSetMotors(str, paramIdx, paramCnt);
    else if(strcasecmp(str, "SETSERVOS") == 0)
        processSetServos(str, paramIdx, paramCnt);
    else if(strcasecmp(str, "SETLASER") == 0)
        processSetLaser(str, paramIdx, paramCnt);
    else if(strcasecmp(str, "SETLIGHT") == 0)
        processSetLight(str, paramIdx, paramCnt);
    else
        PRINTSERIAL("ERR:UNKNOWNCMD");
}

void processGetVoltage(char* params, int* paramIdx, int paramCnt)
{
    char answer[MAX_CMD_CHARS] = {};
    snprintf(answer, MAX_CMD_CHARS, "GETVOLTAGE:%d", (int)(batVolt.getValue() * 4.96));
    Serial.println(answer);
}

void processSetMotors(char* params, int* paramIdx, int paramCnt)
{
	if(paramCnt < 2)
    {
        PRINTSERIAL("ERR:TOOFEWPARAMS");
        return;
    }

    int leftMotor;
    int rightMotor;

    if(sscanf(params + paramIdx[0], "%d", &leftMotor) != 1
        || sscanf(params + paramIdx[1], "%d", &rightMotor) != 1)
    {
        PRINTSERIAL("ERR:PARAMSINCORRECT");
        return;
    }

    motorL.run(leftMotor);
    motorR.run(rightMotor);

    char answer[MAX_CMD_CHARS] = {};
    snprintf(answer, MAX_CMD_CHARS, "SETMOTORS:%d;%d", leftMotor, rightMotor);
    Serial.println(answer);
}

void processSetServos(char* params, int* paramIdx, int paramCnt)
{
    if(paramCnt < 2)
    {
        PRINTSERIAL("ERR:TOOFEWPARAMS");
        return;
    }

    int hServo;
    int vServo;

    if(sscanf(params + paramIdx[0], "%d", &hServo) != 1
        || sscanf(params + paramIdx[1], "%d", &vServo) != 1)
    {
        PRINTSERIAL("ERR:PARAMSINCORRECT");
        return;
    }

    hServo = constrain(hServo, -90, 90);
    vServo = constrain(vServo, -90, 90);

    servoH.write(hServo + 90);
    servoV.write(vServo + 90);

    char answer[MAX_CMD_CHARS] = {};
    snprintf(answer, MAX_CMD_CHARS, "SETSERVOS:%d;%d", hServo, vServo);
    Serial.println(answer);
}

void processSetLaser(char* params, int* paramIdx, int paramCnt)
{
	if(paramCnt < 1)
    {
        PRINTSERIAL("ERR:TOOFEWPARAMS");
        return;
    }

    int val;

    if(sscanf(params + paramIdx[0], "%d", &val) != 1)
    {
        PRINTSERIAL("ERR:PARAMSINCORRECT");
        return;
    }

    laser.set(val != 0);

    char answer[MAX_CMD_CHARS] = {};
    snprintf(answer, MAX_CMD_CHARS, "SETLASER:%s", val != 0 ? "ON" : "OFF");
    Serial.println(answer);
}

void processSetLight(char* params, int* paramIdx, int paramCnt)
{
    if(paramCnt == 1)
    {
        uint32_t color = 0;
        if(sscanf(params + paramIdx[0], "%lx", &color) != 1)
        {
            PRINTSERIAL("ERR:PARAMSINCORRECT");
            return;
        }

        pixels.fill(color);
        pixels.show();
        
        char answer[MAX_CMD_CHARS] = {};
        snprintf(answer, MAX_CMD_CHARS, "SETLIGHT:0x%06lx", color);
        Serial.println(answer);
    }
    else if(paramCnt == 4)
    {
        uint32_t colors[4] = {};
        for(int i = 0; i < 4; i++)
        {
            if(sscanf(params + paramIdx[i], "%lx", colors + i) != 1)
            {
                PRINTSERIAL("ERR:PARAMSINCORRECT");
                return;
            }
        }

        for(int i = 0; i < 4; i++)
            pixels.setPixelColor(i, colors[i]);            

        pixels.show();
        
        char answer[MAX_CMD_CHARS] = {};
        snprintf(answer, MAX_CMD_CHARS, "SETLIGHT:0x%06lx;0x%06lx;0x%06lx;0x%06lx", colors[0], colors[1], colors[2], colors[3]);
        Serial.println(answer);
    }
    else
    {
        PRINTSERIAL("ERR:INVALIDPARAMCOUNT");
        return;
    }
}
