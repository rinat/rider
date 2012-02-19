package unittest.file;

import j2meunit.framework.TestCase;
import rider.core.file.FileReader;
import rider.core.file.FileWriter;
import rider.core.file.FileWriterListener;
import unittest.UnitTestsRegistry;

public class FileWriterTest extends TestCase {

    class FileListener implements FileWriterListener {

        private boolean _expectedError;
        private String _expectedContent;

        public FileListener(boolean expectedError, String expectedContent) {
            _expectedError = expectedError;
            _expectedContent = expectedContent;
        }

        public void OnFileWritten(String content) {
            assertTrue(!_expectedError);
            assertEquals(_expectedContent, content);
        }

        public void onFileWriteError(Exception e) {
            assertTrue(_expectedError);
        }
    }

    public FileWriterTest() {
    }

    public void runTest() {
        constructorExceptionCheck();
        emptyFileWriterCheck();
        contentFileWriterCheck();
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

    private void emptyFileWriterCheck() {
        try {
            FileWriter writer = new FileWriter(UnitTestsRegistry.kUnitTestsFilesRootDirectory +
                    "unitTestFiles/FileWriterTest_emptyFileWriterCheck.txt", "",
                    new FileListener(false, ""));
            writer.start();
        }
        catch (NullPointerException e) {
            this.assertTrue(false);
            return;
        }
        this.assertTrue(true);
    }

    private void contentFileWriterCheck() {
        try {
            final String content = "Separation of interface from implementation";
            FileWriter writer = new FileWriter(UnitTestsRegistry.kUnitTestsFilesRootDirectory +
                    "unitTestFiles/FileWriterTest_contentFileWriterCheck.txt", content,
                    new FileListener(false, content));
            writer.start();
        }
        catch (NullPointerException e) {
            this.assertTrue(false);
            return;
        }
        this.assertTrue(true);
    }
}
