package kaptainwutax.minemap.util.data;

public final class Str {

    public static String formatName(String name) {
        char[] chars = name.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);

        for(int i = 1; i < chars.length; i++) {
            if(chars[i] == '_') {
                chars[i] = ' ';
                chars[i + 1] = Character.toUpperCase(chars[i + 1]);
            }
        }

        return new String(chars);
    }

    public static String capitalize(String s){
        if(s == null) return null;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

}