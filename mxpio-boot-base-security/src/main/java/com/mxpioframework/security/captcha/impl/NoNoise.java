package com.mxpioframework.security.captcha.impl;

import java.awt.image.BufferedImage;

import com.mxpioframework.security.captcha.NoiseProducer;
import com.mxpioframework.security.captcha.util.Configurable;

/**
 * Imlemention of NoiseProducer that does nothing.
 * 
 * @author Yuxing Wang
 */
public class NoNoise extends Configurable implements NoiseProducer
{
	/**
	 */
	public void makeNoise(BufferedImage image, float factorOne,
			float factorTwo, float factorThree, float factorFour)
	{
		//Do nothing.
	}
}
