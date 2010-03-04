package org.tellurium.test;

import java.sql.Date;

import org.junit.Before;
import org.junit.BeforeClass;
import java.util.Locale;

import org.tellurium.i18n.InternationalizationManager;

import groovy.util.GroovyTestCase;

public class InternationalizationManager_UT extends GroovyTestCase {

	public void testTranslateWithEnglishLocale()
	{
		InternationalizationManager i18nManager = new InternationalizationManager()
		i18nManager.setLocale(new Locale("en" , "US"))
		i18nManager.addResourceBundle("TestMessagesBundle")

		//translating of strings
		String messageFromResourceBundle = i18nManager.translate("i18nManager.testString")
		assertEquals("This is a testString in English", messageFromResourceBundle)
		
		//translation of number data types
		Double amount = new Double(345987.246);
		String translatedValue = i18nManager.translate(amount, false)
		assertEquals("345,987.246" , translatedValue)

		//translation of currency data types
		amount = new Double(9876543.21);
		translatedValue = i18nManager.translate(amount, true)
		assertEquals("\$9,876,543.21" , translatedValue)

		//translation of dates - date is 2009, Jan 1
		Date date = new Date(109 , 0 , 1)
		translatedValue = i18nManager.translate(date, false)
		assertEquals("Jan 1, 2009" , translatedValue)

	}

	public void testTranslateWithFrenchLocale()
	{
		InternationalizationManager i18nManager = new InternationalizationManager()
		i18nManager.setLocale(new Locale("fr" , "FR"))
		i18nManager.addResourceBundle("TestMessagesBundle")		
		
		//String messageFromResourceBundle = i18nManager.translate("i18nManager.testString")
		//assertEquals("c'est une corde d'essai en fran�ais", messageFromResourceBundle)

		//translation of number data types
		Double amount = new Double(21.26);
		String translatedValue = i18nManager.translate(amount, false)
		assertEquals("21,26" , translatedValue)

		//translation of currency data types
		amount = new Double(21.26);
		translatedValue = i18nManager.translate(amount, true)
		assertEquals("21,26 �" , translatedValue)
		
		//translation of dates - date is 2009, Jan 1
		Date date = new Date(109 , 0, 1)
		translatedValue = i18nManager.translate(date, false)
		assertEquals("1 janv. 2009" , translatedValue)
	}

}