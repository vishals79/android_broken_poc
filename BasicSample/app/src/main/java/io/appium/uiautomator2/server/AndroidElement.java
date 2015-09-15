package io.appium.uiautomator2.server;

import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.StaleObjectException;
import android.support.test.uiautomator.UiObject2;

import static io.appium.uiautomator2.util.Device.device;

// android.support.test.uiautomator.StaleObjectException
// android.support.test.uiautomator.BySelector
//	android.support.test.uiautomator.UiObject2
public class AndroidElement {
    private UiObject2 uiobject2;
    private BySelector byselector;
    private int id;
    private static int nextElementId = 0;

    public AndroidElement(UiObject2 uiobject2, BySelector byselector) {
        this.uiobject2 = uiobject2;
        this.byselector = byselector;
        this.id = nextElementId++;
    }

    public UiObject2 getObject() {
        return uiobject2;
    }

    public AndroidElement refresh() {
        try {
            // Invoke UiObject2 getAccessibilityNodeInfo to refresh mCachedNode
            // If the object is in an illegal state or stale then re-find the object.
            uiobject2.hashCode();
        } catch (IllegalStateException e) {
            uiobject2 = device().findObject(byselector);
        } catch (StaleObjectException e) {
            uiobject2 = device().findObject(byselector);
        }

        return this;
    }

    public BySelector getSelector() {
        return byselector;
    }

    public int getId() {
        return id;
    }
}
