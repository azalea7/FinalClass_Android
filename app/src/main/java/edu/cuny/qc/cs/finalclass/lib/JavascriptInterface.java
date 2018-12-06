package edu.cuny.qc.cs.finalclass.lib;

import java.util.function.Consumer;

public class JavascriptInterface {
    private final Consumer<String[]> func;

    public JavascriptInterface(Consumer<String[]> acceptFormData) {
        this.func = acceptFormData;
    }

    @android.webkit.JavascriptInterface
    public void sendFormValues(String username, String password) {
        func.accept(new String[] {username, password});
    }
}
