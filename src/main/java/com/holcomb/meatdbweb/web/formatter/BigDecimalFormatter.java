package com.holcomb.meatdbweb.web.formatter;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BigDecimalFormatter {
    private final NumberFormat moneyFormatter = NumberFormat.getCurrencyInstance(Locale.US);

    public String printMoney(BigDecimal money) {
        String formattedMoney = moneyFormatter.format(money);
        return formattedMoney;
    }

    public String printPounds(BigDecimal pounds) {
        String poundsWithoutTrailingZeros = pounds.stripTrailingZeros().toPlainString();
        return poundsWithoutTrailingZeros.concat(" lb");
    }

    public BigDecimal parseMoney(String formattedMoney) throws ParseException {
        BigDecimal money = null;
        try {
            String parsedMoney = moneyFormatter.parse(formattedMoney).toString();
            money = new BigDecimal(parsedMoney);
        } catch (ParseException e) {
            e.getStackTrace();
        }
        return money;
    }

    public BigDecimal parsePounds(String formattedPounds) {
        String regex = "(?<value>\\d+([.]\\d+)?)\\D*";
        Pattern pat = Pattern.compile(regex);
        Matcher mat = pat.matcher(formattedPounds);

        BigDecimal pounds = null;
        if (mat.matches()) {
            pounds = new BigDecimal(mat.group("value"));
        }
        return pounds;
    }
}
