package rider.utils;

public class StringUtil {

    /**
     * Method removes HTML tags from given string.
     *
     * @param text Input parameter containing HTML tags (eg. <b>cat</b>)
     * @return String without HTML tags (eg. cat)
     */
    public static String removeHtml(String text) {
        try {
            int idx = text.indexOf('<');
            if (idx == -1) return text;

            StringBuffer plainText = new StringBuffer();
            String htmlText = text;
            int htmlStartIndex = htmlText.indexOf('<');
            if (htmlStartIndex == -1) {
                return text;
            }
            while (htmlStartIndex >= 0) {
                plainText.append(htmlText.substring(0, htmlStartIndex));
                int htmlEndIndex = htmlText.indexOf('>', htmlStartIndex);
                // If we have unmatched '<' without '>' stop or we
                // get into infinite loop.
                if (htmlEndIndex < 0) {
                    break;
                }
                htmlText = htmlText.substring(htmlEndIndex + 1);
                htmlStartIndex = htmlText.indexOf('<');
            }
            return plainText.toString().trim();
        } catch (Exception e) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(e);
            //#endif
            return text;
        }
    }

}
