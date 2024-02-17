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

import lombok.NonNull;

import javax.annotation.Nonnull;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 *
 */
public class RejectedExecutionHandlers {
    private RejectedExecutionHandlers() {
    }

    /**
     * @return a {@link RejectedExecutionHandler} that uses/blocks the caller thread to re-submit the rejected task
     *         until it is accepted, or drop the task if the executor has been shut down.
     */
    public static @Nonnull RejectedExecutionHandler blockingResubmitPolicy() {
        return new BlockingResubmitPolicy();
    }

    /**
     *
     */
    public static class BlockingResubmitPolicy implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(@NonNull Runnable r, @NonNull ThreadPoolExecutor executor) {
            if (executor.isShutdown()) {
                return;
            }
            try {
                executor.getQueue().put(r);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
