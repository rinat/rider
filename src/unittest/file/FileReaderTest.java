package unittest.file;

import j2meunit.framework.TestCase;
import rider.core.file.FileReader;
import rider.core.file.FileReaderListener;
import unittest.UnitTestsRegistry;

public class FileReaderTest extends TestCase {

    class FileListener implements FileReaderListener {

        private String _expectedContent;
        private boolean _isErrorExpected;

        public FileListener(String expectedContent, boolean isErrorExpected) {
            _expectedContent = expectedContent;
            _isErrorExpected = isErrorExpected;
        }

        public void onFileRead(String content) {
            assertTrue(!_isErrorExpected);
            assertEquals(content, _expectedContent);
        }

        public void onFileReadError(Exception e) {
            assertTrue(_isErrorExpected);
        }
    }

    public FileReaderTest() {
    }

    public void runTest() {
        constructorExceptionCheck();
        emptyFileReaderCheck();
        contentFileReaderCheck();
    }

    private void constructorExceptionCheck() {
        try {
            FileReader reader = new FileReader(null, null);
        }
        catch (NullPointerException e) {
            this.assertTrue(true);
            return;
        }
        this.assertTrue(false);
    }

    private void emptyFileReaderCheck() {
        try {
            FileReader reader = new FileReader(UnitTestsRegistry.kUnitTestsFilesRootDirectory +
                    "unitTestFiles/FileReaderTest_emptyFileReaderCheck.txt",
                    new FileListener("", false));
            reader.start();
        }
        catch (NullPointerException e) {
            this.assertTrue(false);
            return;
        }
        this.assertTrue(true);
    }

    private void contentFileReaderCheck() {
        try {
            FileReader reader = new FileReader(UnitTestsRegistry.kUnitTestsFilesRootDirectory +
                    "unitTestFiles/FileReaderTest_contentFileReaderCheck.txt",
                    new FileListener("Separation of interface from implementation", false));
            reader.start();
        }
        catch (NullPointerException e) {
            this.assertTrue(false);
            return;
        }
        this.assertTrue(true);
    }
}
