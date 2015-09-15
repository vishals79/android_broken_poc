package io.appium.uiautomator2.server;

import com.google.common.collect.MapMaker;

import java.util.Map;

public abstract class AndroidElementsHash {
    // Use weak hash map from Guava.
    private final Map<Integer, AndroidElement> elements = new MapMaker().weakKeys().makeMap();

    public AndroidElement addElement(AndroidElement element) {
        elements.put(element.getId(), element);
        return element;
    }

    public AndroidElement getElement(int id) {
        return elements.get(id).refresh();
    }
}
