#ifndef MOTOR_H
#define MOTOR_H

#include <Arduino.h>

namespace Periph
{
	class Motor
	{
	public:
		Motor(byte dirPin, byte pwmPin, bool reverse = false)
		{
			_dirPin = dirPin;
			_pwmPin = pwmPin;
			_reverse = reverse;
			pinMode(_dirPin, OUTPUT);
			pinMode(_pwmPin, OUTPUT);
		}

		void stop()
		{
			digitalWrite(_dirPin, 0);
			digitalWrite(_pwmPin, 0);
		}

		void run(int dirSpeed)
		{
			boolean fw = dirSpeed >= 0;
			uint8_t pwm = constrain(abs(dirSpeed), 0, 255);
			if(fw ^ _reverse)
			{
				digitalWrite(_dirPin, 0);
				analogWrite(_pwmPin, pwm);
			}
			else
			{
				digitalWrite(_dirPin, 1);
				analogWrite(_pwmPin, 255 - pwm);
			}
		}

	private:
		byte _dirPin;
		byte _pwmPin;
		boolean _reverse;
	};
}

#endif // MOTOR_H
