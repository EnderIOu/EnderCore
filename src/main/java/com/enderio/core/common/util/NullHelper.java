package com.enderio.core.common.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public final class NullHelper {

    private NullHelper() {}

    @NotNull
    public static <P> P notnull(@Nullable P o, @NotNull String message) {
        return notnull(o, (Object) message);
    }

    @NotNull
    public static <P> P notnullJ(@Nullable P o, @NotNull String message) {
        return notnullJ(o, (Object) message);
    }

    @NotNull
    public static <P> P notnullM(@Nullable P o, @NotNull String message) {
        return notnullM(o, (Object) message);
    }

    @NotNull
    public static <P> P notnullF(@Nullable P o, @NotNull String message) {
        return notnullF(o, (Object) message);
    }

    @NotNull
    public static <P> P untrusted(@NotNull P o, @NotNull String message) {
        return untrusted(o, (Object) message);
    }

    @NotNull
    public static <P> P notnull(@Nullable P o, @NotNull Object... message) {
        if (o == null) {
            throw new NullPointerException(
                    "Houston we have a problem: '" + join(message) + "'. " +
                            "Please report that on our bugtracker unless you are using some old version. Thank you.");
        }
        return o;
    }

    @NotNull
    public static <P> P notnullJ(@Nullable P o, @NotNull Object... message) {
        if (o == null) {
            throw new NullPointerException(
                    "There was a problem with Java: The call '" + join(message) +
                            "' returned null even though it should not be able to do that. Is your Java broken?");
        }
        return o;
    }

    @NotNull
    public static <P> P notnullM(@Nullable P o, @NotNull Object... message) {
        if (o == null) {
            throw new NullPointerException("There was a problem with Minecraft: The call '" + join(message) +
                    "' returned null even though it should not be able to do that. Is your Minecraft broken? Did some other mod break it?");
        }
        return o;
    }

    @NotNull
    public static <P> P notnullF(@Nullable P o, @NotNull Object... message) {
        if (o == null) {
            throw new NullPointerException("There was a problem with Forge: The call '" + join(message) +
                    "' returned null even though it should not be able to do that. Is your Forge broken? Did some other mod break it?");
        }
        return o;
    }

    @SuppressWarnings({ "null", "unused" })
    @NotNull
    public static <P> P untrusted(@NotNull P o, @NotNull Object... message) {
        if (o == null) {
            throw new NullPointerException(
                    "There was a problem with Minecraft: The call '" + join(message) +
                            "' returned null even though it says it is not be able to do that. " //
                            + "Your Minecraft is broken. This mod is NOT(!) the cause of this crash!");
        }
        return o;
    }

    /**
     * Returns its {@link NotNull} argument unchanged as {@link Nullable}. Use this if you want to null-check values
     * that are annotated non-null but are known not
     * to be.
     */
    public static @Nullable <P> P untrust(@NotNull P o) {
        return o;
    }

    /**
     * Returns the first non-<code>null</code> parameter or thrown a
     * {@link NullPointerException} if there is none.
     */
    @SafeVarargs
    public static @NotNull <P> P first(@Nullable P... o) {
        for (P on : notnull(o, (Object) "... param is null")) {
            if (on != null) {
                return on;
            }
        }
        throw new NullPointerException(
                "Houston we have a problem. Please report that on our bugtracker unless you are using some old version. Thank you.");
    }

    @SafeVarargs
    public static @NotNull <P> P first(@NotNull Supplier<P>... o) {
        for (Supplier<P> on : notnull(o, (Object) "... param is null")) {
            P p = notnull(on, (Object) "... param value is null").get();
            if (p != null) {
                return p;
            }
        }
        throw new NullPointerException(
                "Houston we have a problem. Please report that on our bugtracker unless you are using some old version. Thank you.");
    }

    @SafeVarargs
    public static @Nullable <P> P firstOrNull(@NotNull Supplier<P>... o) {
        for (Supplier<P> on : notnull(o, (Object) "... param is null")) {
            P p = notnull(on, (Object) "... param value is null").get();
            if (p != null) {
                return p;
            }
        }
        return null;
    }

    public static @Nullable <P> P firstWithDefault(@NotNull NNList<Supplier<P>> o, @Nullable P d) {
        for (Supplier<P> on : o) {
            P p = notnull(on, (Object) "NNList.get() is null").get();
            if (p != null) {
                return p;
            }
        }
        return d;
    }

    @SafeVarargs
    public static @NotNull <P> P first(@Nullable P value, @NotNull Supplier<P>... o) {
        if (value != null) {
            return value;
        }
        for (Supplier<P> on : notnull(o, (Object) "... param is null")) {
            P p = notnull(on, (Object) "... param value is null").get();
            if (p != null) {
                return p;
            }
        }
        throw new NullPointerException(
                "Houston we have a problem. Please report that on our bugtracker unless you are using some old version. Thank you.");
    }

    public static @Nullable <P> P firstWithDefault(@Nullable P value, @NotNull Supplier<P> on, @Nullable P d) {
        if (value != null) {
            return value;
        }
        P p = notnull(on, (Object) "... param value is null").get();
        if (p != null) {
            return p;
        }
        return d;
    }

    @SafeVarargs
    public static @Nullable <P> P firstWithDefault(@Nullable P d, @NotNull Supplier<P>... o) {
        for (Supplier<P> on : notnull(o, (Object) "... param is null")) {
            P p = notnull(on, (Object) "... param value is null").get();
            if (p != null) {
                return p;
            }
        }
        return d;
    }

    private static String join(@NotNull Object... data) {
        StringBuilder b = new StringBuilder();
        for (Object object : data) {
            b.append(object);
        }
        return b.toString();
    }
}
