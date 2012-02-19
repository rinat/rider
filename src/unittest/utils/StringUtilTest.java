package unittest.utils;

import j2meunit.framework.TestCase;
import rider.utils.StringUtil;


public class StringUtilTest extends TestCase {

    public StringUtilTest() {
    }

    public void runTest() {
        removeHtmlFromEmpty();
        removeHtmlTest();
        removeHtmlTestOne();
        removeHtmlTestTwo();
    }

    private void removeHtmlFromEmpty() {
        String htmlText = "";
        htmlText = StringUtil.removeHtml(htmlText);
        this.assertEquals(htmlText, "");
    }

    private void removeHtmlTest() {
        String htmlText = "<br>Text<br>";
        htmlText = StringUtil.removeHtml(htmlText);
        this.assertEquals(htmlText, "Text");
    }

    private void removeHtmlTestOne() {
        String htmlText = "<b><b>cat</b></b>";
        htmlText = StringUtil.removeHtml(htmlText);
        this.assertEquals(htmlText, "cat");
    }

    private void removeHtmlTestTwo() {
        String htmlText = "<b><b>cl</b></b><b><b>ose</b></b>";
        htmlText = StringUtil.removeHtml(htmlText);
        this.assertEquals(htmlText, "close");
    }
}
