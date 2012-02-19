package unittest.utils;

import j2meunit.framework.TestCase;
import rider.utils.Settings;

import javax.microedition.rms.RecordStoreException;

/**
 * Created by IntelliJ IDEA.
 * User: Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 * Date: 18.05.2010
 * Time: 23:08:04
 * To change this template use File | Settings | File Templates.
 */
public class SettingsTest extends TestCase {

    public SettingsTest() {
    }

    public void runTest() {
        getPutTest();
    }

    private void getPutTest() {
        try {
            Settings settings = loadSettings("settings");
            settings.put("key", "value");
            settings.put("key1", "value1");
            settings.put("key2", "value2");
            settings.save();

            Settings settingsEqual = loadSettings("settings");
            this.assertEquals(settingsEqual.get("key"), settings.get("key"));
            this.assertEquals(settingsEqual.get("key1"), settings.get("key1"));
            this.assertEquals(settingsEqual.get("key2"), settings.get("key2"));
            this.assertTrue(true);
        } catch (RecordStoreException e) {
            this.assertTrue(false);
        }
        catch (NullPointerException e) {
            this.assertTrue(false);
        }
    }

    private Settings loadSettings(String settings) throws RecordStoreException {
        return (new Settings("settings"));
    }
}
