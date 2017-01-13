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

/**
 * Created by michaelandrepearce on 12/01/2017.
 */
public class JNRInstant {
    public static final class Timeval extends Struct {
        public final time_t tv_sec = new time_t();
        public final SignedLong tv_usec = new SignedLong();

        public Timeval(Runtime runtime) {
            super(runtime);
        }
    }

    public interface LibC  {
        public int gettimeofday(@Out @Transient Timeval tv, Pointer unused);

    }

    static LibC libc = LibraryLoader.create(LibC.class).load("c");
    static Runtime runtime = Runtime.getRuntime(libc);
    static Timeval time = new Timeval(runtime);

    public static Instant now(){
        libc.gettimeofday(time, null);
        long seconds= time.tv_sec.get();
        long micros = time.tv_usec.get() & 4294967295L;
        return Instant.ofEpochSecond(seconds, micros * 1000);
    }

    public static void main(String... args) throws InterruptedException {
        System.out.println(JNRInstant.now());
        System.out.println(JNRInstant.now());
        System.out.println(JNRInstant.now());
        System.out.println(JNRInstant.now());
        System.out.println(JNRInstant.now());
        System.out.println(JNRInstant.now());
        System.out.println(JNRInstant.now());
        System.out.println(JNRInstant.now());
        Instant instant = JNRInstant.now();

        Thread.sleep(1000);
        Instant instant2 = JNRInstant.now();

        Duration duration = Duration.between(instant, instant2);
        long micro = TimeUnit.SECONDS.toMicros(duration.getSeconds()) + TimeUnit.NANOSECONDS.toMicros(duration.getNano());
        System.out.println(micro);
    }




}
