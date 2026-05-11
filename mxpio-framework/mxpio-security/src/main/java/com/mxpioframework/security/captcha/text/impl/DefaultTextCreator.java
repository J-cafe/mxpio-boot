package com.mxpioframework.security.captcha.text.impl;

import java.security.SecureRandom;
import java.util.Random;

import com.mxpioframework.security.captcha.text.TextProducer;
import com.mxpioframework.security.captcha.util.Configurable;

/**
 * {@link DefaultTextCreator} creates random text from an array of characters
 * with specified length.
 */
public class DefaultTextCreator extends Configurable implements TextProducer
{
	/**
	 * @return the random text
	 */
	public String getText()
	{
		int length = getConfig().getTextProducerCharLength();
		char[] chars = getConfig().getTextProducerCharString();
		Random rand = new SecureRandom();
		StringBuilder text = new StringBuilder();
		for (int i = 0; i < length; i++)
		{
			text.append(chars[rand.nextInt(chars.length)]);
		}

		return text.toString();
	}
}
