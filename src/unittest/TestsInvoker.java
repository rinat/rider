package unittest;

import j2meunit.midletui.TestRunner;
import unittest.bookmarks.BookmarkImportExportTest;
import unittest.bookmarks.BookmarkTest;
import unittest.file.FileReaderTest;
import unittest.file.FileWriterTest;
import unittest.rss.RssFeedCollectionTest;
import unittest.rss.RssFeedTest;
import unittest.rss.RssItemCollectionTest;
import unittest.rss.RssItemTest;
import unittest.rss.parser.ParserProviderTest;
import unittest.rss.parser.RssVOneParserTest;
import unittest.rss.parser.RssVTwoParserTest;
import unittest.utils.SettingsTest;
import unittest.utils.StringUtilTest;

public class TestsInvoker extends TestRunner {

    protected void startApp() {
        // rss
        this.doRun(new RssItemTest());
        this.doRun(new RssFeedTest());
        this.doRun(new RssFeedCollectionTest());
        this.doRun(new RssItemCollectionTest());
        // bookmarks
        this.doRun(new BookmarkTest());
        this.doRun(new BookmarkImportExportTest());
        // utils
        this.doRun(new StringUtilTest());
        this.doRun(new SettingsTest());
        // file
        this.doRun(new FileReaderTest());
        this.doRun(new FileWriterTest());
        // parser
        this.doRun(new RssVOneParserTest());
        this.doRun(new RssVTwoParserTest());
        this.doRun(new ParserProviderTest());
    }
}
