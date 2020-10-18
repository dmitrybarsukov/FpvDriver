#include <Arduino.h>
#include <Adafruit_NeoPixel.h>
#include "App.h"
#include "Pins.h"
#include "VoltageSensor.h"
#include "Motor.h"
#include "MotorController.h"
#include "NeoPixelController.h"

#define BAUDRATE 115200
#define SWITCH_OFF_TIMEOUT 15000

App* app;

void setup()
{
    Serial.begin(BAUDRATE);
    auto leftMotor = new Motor(Pins::MOTOR1_DIRECTION, Pins::MOTOR1_PWM, Reverse::FALSE);
    auto rightMotor = new Motor(Pins::MOTOR2_DIRECTION, Pins::MOTOR2_PWM, Reverse::TRUE);
    auto motorController = new MotorController(leftMotor, rightMotor, SWITCH_OFF_TIMEOUT);
    auto adafruitNeoPixels = new Adafruit_NeoPixel(4, Pins::NEOPIXELS);
    auto neoPixels = new NeoPixelController(adafruitNeoPixels, SWITCH_OFF_TIMEOUT);
    auto batteryVoltageSensor = new VoltageSensor(Pins::BATTERY_VOLTAGE_SENSE);
    app = new App(
        motorController,
        neoPixels,
        batteryVoltageSensor
    );
}

void loop()
{
    app->Preprocess();
    app->Process();
}
