package org.progmatic.webshop.helpers;

public class JsonBuilder {

    StringBuilder seed = new StringBuilder();

    public static JsonBuilder newBuilder() {
        return new JsonBuilder();
    }

    public JsonBuilder add(String name, String value) {
        seed.append("\"").append(name).append("\":");
        seed.append("\"").append(value).append("\",");
        return this;
    }

    public JsonBuilder add(String name, float value) {
        seed.append("\"").append(name).append("\":");
        seed.append(value).append(",");
        return this;
    }

    public JsonBuilder addEmptyList(String name) {
        seed.append("\"").append(name).append("\":");
        seed.append("[]").append(",");
        return this;
    }

    public String build() {
        String s = seed.substring(0, seed.lastIndexOf(","));
        return "{" + s + "}";
    }

}
