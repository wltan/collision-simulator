package i18n;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Contains internationalization (also known as i18n) utilities.
 * @author Wei Liang
 */
public final class I18nUtils {
	
	public transient ResourceBundle strRes;
	public final String strings;
	public final Locale[] languages;
	
	public I18nUtils(String strings, Locale[] languages){
		this.strings = strings;
		this.languages = languages;
		strRes = ResourceBundle.getBundle(strings);
	}
	
	/**
	 * Initializes the string resource bundle to the specified locale.
	 * Note that passing in an unsupported locale will result in unexpected results.
	 */
	public void changeLanguage(Locale l){
		strRes = ResourceBundle.getBundle(strings, l);
	}
	
	/**
	 * Retrieves a {@code String} from the currently loaded {@code ResourceBundle}.
	 * @param key The key of the string to obtain.
	 * @return The {@code String} specified by the key in the loaded {@code ResourceBundle}.
	 */
	public String getString(String key){
		return strRes.getString(key);
	}
	
}
