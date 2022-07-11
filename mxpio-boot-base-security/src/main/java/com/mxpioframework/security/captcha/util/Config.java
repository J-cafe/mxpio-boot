package com.mxpioframework.security.captcha.util;

import java.awt.Color;
import java.awt.Font;
import java.util.Properties;

import com.mxpioframework.security.captcha.BackgroundProducer;
import com.mxpioframework.security.captcha.Constants;
import com.mxpioframework.security.captcha.GimpyEngine;
import com.mxpioframework.security.captcha.NoiseProducer;
import com.mxpioframework.security.captcha.Producer;
import com.mxpioframework.security.captcha.impl.DefaultBackground;
import com.mxpioframework.security.captcha.impl.DefaultCaptcha;
import com.mxpioframework.security.captcha.impl.DefaultNoise;
import com.mxpioframework.security.captcha.impl.WaterRipple;
import com.mxpioframework.security.captcha.text.TextProducer;
import com.mxpioframework.security.captcha.text.WordRenderer;
import com.mxpioframework.security.captcha.text.impl.DefaultTextCreator;
import com.mxpioframework.security.captcha.text.impl.DefaultWordRenderer;

/**
 * {@link Config} retrieves configuration values from properties file and
 * specifies default values when no value is specified.
 */
public class Config
{
	/** */
	private Properties properties;

	/** */
	private ConfigHelper helper;

	/** */
	public Config(Properties properties)
	{
		this.properties = properties;
		this.helper = new ConfigHelper();
	}

	/** */
	public boolean isBorderDrawn()
	{
		String paramName = Constants.CAPTCHA_BORDER;
		String paramValue = this.properties.getProperty(paramName);
		return this.helper.getBoolean(paramName, paramValue, true);
	}

	/** */
	public Color getBorderColor()
	{
		String paramName = Constants.CAPTCHA_BORDER_COLOR;
		String paramValue = this.properties.getProperty(paramName);
		return this.helper.getColor(paramName, paramValue, Color.BLACK);
	}

	/** */
	public int getBorderThickness()
	{
		String paramName = Constants.CAPTCHA_BORDER_THICKNESS;
		String paramValue = this.properties.getProperty(paramName);
		return this.helper.getPositiveInt(paramName, paramValue, 1);
	}

	/** */
	public Producer getProducerImpl()
	{
		String paramName = Constants.CAPTCHA_PRODUCER_IMPL;
		String paramValue = this.properties.getProperty(paramName);
		Producer producer = (Producer) this.helper.getClassInstance(paramName, paramValue, new DefaultCaptcha(), this);
		return producer;
	}

	/** */
	public TextProducer getTextProducerImpl()
	{
		String paramName = Constants.CAPTCHA_TEXTPRODUCER_IMPL;
		String paramValue = this.properties.getProperty(paramName);
		TextProducer textProducer = (TextProducer) this.helper.getClassInstance(paramName, paramValue,
				new DefaultTextCreator(), this);
		return textProducer;
	}

	/** */
	public char[] getTextProducerCharString()
	{
		String paramName = Constants.CAPTCHA_TEXTPRODUCER_CHAR_STRING;
		String paramValue = this.properties.getProperty(paramName);
		return this.helper.getChars(paramName, paramValue, "abcde2345678gfynmnpwx".toCharArray());
	}

	/** */
	public int getTextProducerCharLength()
	{
		String paramName = Constants.CAPTCHA_TEXTPRODUCER_CHAR_LENGTH;
		String paramValue = this.properties.getProperty(paramName);
		return this.helper.getPositiveInt(paramName, paramValue, 5);
	}

	/** */
	public Font[] getTextProducerFonts(int fontSize)
	{
		String paramName = Constants.CAPTCHA_TEXTPRODUCER_FONT_NAMES;
		String paramValue = this.properties.getProperty(paramName);
		return this.helper.getFonts(paramName, paramValue, fontSize, new Font[]{
				new Font("Arial", Font.BOLD, fontSize), new Font("Courier", Font.BOLD, fontSize)
		});
	}

	/** */
	public int getTextProducerFontSize()
	{
		String paramName = Constants.CAPTCHA_TEXTPRODUCER_FONT_SIZE;
		String paramValue = this.properties.getProperty(paramName);
		return this.helper.getPositiveInt(paramName, paramValue, 40);
	}

	/** */
	public Color getTextProducerFontColor()
	{
		String paramName = Constants.CAPTCHA_TEXTPRODUCER_FONT_COLOR;
		String paramValue = this.properties.getProperty(paramName);
		return this.helper.getColor(paramName, paramValue, Color.BLACK);
	}

	/** */
	public int getTextProducerCharSpace()
    {
		String paramName = Constants.CAPTCHA_TEXTPRODUCER_CHAR_SPACE;
		String paramValue = properties.getProperty(paramName);
		return this.helper.getPositiveInt(paramName, paramValue, 2);
	}

	/** */
	public NoiseProducer getNoiseImpl()
	{
		String paramName = Constants.CAPTCHA_NOISE_IMPL;
		String paramValue = this.properties.getProperty(paramName);
		NoiseProducer noiseProducer = (NoiseProducer) this.helper.getClassInstance(paramName, paramValue,
				new DefaultNoise(), this);
		return noiseProducer;
	}

	/** */
	public Color getNoiseColor()
	{
		String paramName = Constants.CAPTCHA_NOISE_COLOR;
		String paramValue = this.properties.getProperty(paramName);
		return this.helper.getColor(paramName, paramValue, Color.BLACK);
	}

	/** */
	public GimpyEngine getObscurificatorImpl()
	{
		String paramName = Constants.CAPTCHA_OBSCURIFICATOR_IMPL;
		String paramValue = this.properties.getProperty(paramName);
		GimpyEngine gimpyEngine = (GimpyEngine) this.helper.getClassInstance(paramName, paramValue, new WaterRipple(), this);
		return gimpyEngine;
	}

	/** */
	public WordRenderer getWordRendererImpl()
	{
		String paramName = Constants.CAPTCHA_WORDRENDERER_IMPL;
		String paramValue = this.properties.getProperty(paramName);
		WordRenderer wordRenderer = (WordRenderer) this.helper.getClassInstance(paramName, paramValue,
				new DefaultWordRenderer(), this);
		return wordRenderer;
	}

	/** */
	public BackgroundProducer getBackgroundImpl()
	{
		String paramName = Constants.CAPTCHA_BACKGROUND_IMPL;
		String paramValue = this.properties.getProperty(paramName);
		BackgroundProducer backgroundProducer = (BackgroundProducer) this.helper.getClassInstance(paramName, paramValue,
				new DefaultBackground(), this);
		return backgroundProducer;
	}

	/** */
	public Color getBackgroundColorFrom()
	{
		String paramName = Constants.CAPTCHA_BACKGROUND_CLR_FROM;
		String paramValue = this.properties.getProperty(paramName);
		return this.helper.getColor(paramName, paramValue, Color.LIGHT_GRAY);
	}

	/** */
	public Color getBackgroundColorTo()
	{
		String paramName = Constants.CAPTCHA_BACKGROUND_CLR_TO;
		String paramValue = this.properties.getProperty(paramName);
		return this.helper.getColor(paramName, paramValue, Color.WHITE);
	}

	/** */
	public int getWidth()
	{
		String paramName = Constants.CAPTCHA_IMAGE_WIDTH;
		String paramValue = this.properties.getProperty(paramName);
		return this.helper.getPositiveInt(paramName, paramValue, 200);
	}

	/** */
	public int getHeight()
	{
		String paramName = Constants.CAPTCHA_IMAGE_HEIGHT;
		String paramValue = this.properties.getProperty(paramName);
		return this.helper.getPositiveInt(paramName, paramValue, 50);
	}

	/**
	 * Allows one to override the key name which is stored in the users
	 * HttpSession. Defaults to Constants.CAPTCHA_SESSION_KEY.
	 */
	public String getSessionKey()
	{
		return this.properties.getProperty(Constants.CAPTCHA_SESSION_CONFIG_KEY, Constants.CAPTCHA_SESSION_KEY);
	}

	/**
	 * Allows one to override the date name which is stored in the
	 * users HttpSession. Defaults to Constants.CAPTCHA_SESSION_KEY.
	 */
	public String getSessionDate()
	{
		return this.properties.getProperty(Constants.CAPTCHA_SESSION_CONFIG_DATE, Constants.CAPTCHA_SESSION_DATE);
	}

	/** */
	public Properties getProperties()
	{
		return this.properties;
	}
}
