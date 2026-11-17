package android.multiuser;

/** Build stand-in for aconfig flags of another module. */
public final class Flags {
    private Flags() {}
    public static boolean enableMovingContentIntoPrivateSpace() { return false; }
    public static final boolean ENABLEMOVINGCONTENTINTOPRIVATESPACE = false;
}
