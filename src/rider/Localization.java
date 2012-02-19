package rider;

import rider.utils.ResourcesUTF8;

/**
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 */
public class Localization {

    private static final Object _sync = new Object();
    private static boolean _initialized = false;
    private static Localization _instance;
    ResourcesUTF8 _localization = new ResourcesUTF8("localization", "rider.Rider", "properties");

    public static Localization getInstance() {
        if (!_initialized) {
            synchronized (_sync) {
                if (_instance == null) {
                    _instance = new Localization();
                    _initialized = true;
                }
            }
        }
        return (_instance);
    }

    public ResourcesUTF8 strings() {
        return (this._localization);
    }

    private Localization() {
        _localization.load();
    }
}
