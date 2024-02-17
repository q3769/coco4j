/*
 * MIT License
 *
 * Copyright (c) 2023 Qingtian Wang
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package coco4j;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.time.Duration;
import java.util.concurrent.CompletionException;
import lombok.NonNull;
import lombok.SneakyThrows;

public class Threads {
    private Threads() {}

    /**
     * Interruptible during sleep, in which case throws unchecked CompletionException wrapping the InterruptedException
     *
     * @param duration the current thread to be sleeping for
     * @throws CompletionException if interrupted
     */
    public static void sleepInterruptiblyUnchecked(@NonNull Duration duration) {
        try {
            NANOSECONDS.sleep(duration.toNanos());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CompletionException(e);
        }
    }

    /**
     * Interruptible during sleep, in which case the InterruptedException thrown is uncheckable/un-catchable
     * programmatically
     *
     * @param duration the current thread to sleep for
     */
    @SneakyThrows(InterruptedException.class)
    public static void sleepInterruptiblyUncheckable(@NonNull Duration duration) {
        NANOSECONDS.sleep(duration.toNanos());
    }

    /**
     * Uninterruptible during sleep. If interruption attempts/<code>InterruptedException</code>s happened during sleep,
     * the last of such InterruptedException will be wrapped in an unchecked CompletionException that is thrown after
     * the sleep completes.
     *
     * @param duration the current thread to be sleeping for
     */
    public static void sleepUninterruptiblyUnchecked(@NonNull Duration duration) {
        InterruptedException interrupted = null;
        try {
            long remainingNanos = duration.toNanos();
            long end = System.nanoTime() + remainingNanos;
            while (true) {
                try {
                    NANOSECONDS.sleep(remainingNanos);
                    break;
                } catch (InterruptedException e) {
                    interrupted = e;
                    remainingNanos = end - System.nanoTime();
                }
            }
            if (interrupted != null) {
                throw new CompletionException(interrupted);
            }
        } finally {
            if (interrupted != null) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Uninterruptible during sleep. If interruption attempts/<code>InterruptedException</code>s happened during sleep,
     * the last of such InterruptedException, uncheckable/un-catchable programmatically, will be thrown to the JVM
     * runtime after the sleep completes.
     *
     * @param duration the current thread to be sleeping for
     */
    @SneakyThrows
    public static void sleepUninterruptiblyUncheckable(@NonNull Duration duration) {
        InterruptedException interrupted = null;
        try {
            long remainingNanos = duration.toNanos();
            long end = System.nanoTime() + remainingNanos;
            while (true) {
                try {
                    NANOSECONDS.sleep(remainingNanos);
                    break;
                } catch (InterruptedException e) {
                    interrupted = e;
                    remainingNanos = end - System.nanoTime();
                }
            }
            if (interrupted != null) {
                throw interrupted;
            }
        } finally {
            if (interrupted != null) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
