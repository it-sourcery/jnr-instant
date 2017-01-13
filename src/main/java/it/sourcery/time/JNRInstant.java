package it.sourcery.time;

import jnr.ffi.LibraryLoader;
import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import jnr.ffi.Struct;
import jnr.ffi.annotations.Out;
import jnr.ffi.annotations.Transient;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;


public class JNRInstant {
    public static final class Timeval extends Struct {
        public final time_t tv_sec = new time_t();
        public final SignedLong tv_usec = new SignedLong();

        public Timeval(Runtime runtime) {
            super(runtime);
        }
    }

    public interface LibC {
        public int gettimeofday(@Out @Transient Timeval tv, Pointer unused);

    }

    static LibC libc = LibraryLoader.create(LibC.class).load("c");
    static Runtime runtime = Runtime.getRuntime(libc);
    static Timeval time = new Timeval(runtime);

    public static Instant now() {
        libc.gettimeofday(time, null);
        long seconds= time.tv_sec.get();
        long micros = time.tv_usec.get() & 4294967295L;
        return Instant.ofEpochSecond(seconds, micros * 1000);
    }
}
