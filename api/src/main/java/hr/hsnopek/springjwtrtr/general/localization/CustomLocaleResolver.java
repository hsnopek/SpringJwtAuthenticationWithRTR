package hr.hsnopek.springjwtrtr.general.localization;

import java.util.Comparator;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;


public class CustomLocaleResolver implements LocaleResolver {

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        final String acceptLanguageHeader = request.getHeader("Accept-Language");
		final String lang = !org.apache.commons.lang3.StringUtils.isBlank(acceptLanguageHeader)
				? acceptLanguageHeader
				: request.getParameter("lang");
		
		Locale locale = Locale.US;
		try {
			locale = StringUtils.parseLocaleString(lang.toLowerCase());
		} catch (Exception e) {
			if(lang != null) {
				locale = Locale.LanguageRange.parse(lang.toLowerCase())
						.stream()
						.sorted(Comparator.comparing(Locale.LanguageRange::getWeight).reversed())
						.map(range -> new Locale(range.getRange()))
						.findFirst()
						.orElseThrow(() -> new IllegalArgumentException("Invalid language set in HTTP request."));
			}
		}
		return locale;
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		response.setLocale(locale);
    }
}
