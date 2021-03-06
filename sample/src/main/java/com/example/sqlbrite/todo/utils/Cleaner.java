package com.example.sqlbrite.todo.utils;

import android.util.Log;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Like {@link java.lang.ref.Cleaner}, but compat for Android.
 * <p>
 * <pre>{@code
 * public class CleaningExample implements AutoCloseable {
 *        // A cleaner, preferably one shared within a library
 *        private static final Cleaner cleaner = <cleaner>;
 *
 *        static class State implements Runnable {
 *
 *            State(...) {
 *                // initialize State needed for cleaning action
 *            }
 *
 *            public void run() {
 *                // cleanup action accessing State, executed at most once
 *            }
 *        }
 *
 *        private final State;
 *        private final Cleaner.Cleanable cleanable
 *
 *        public CleaningExample() {
 *            this.state = new State(...);
 *            this.cleanable = cleaner.register(this, state);
 *        }
 *
 *        public void close() {
 *            cleanable.clean();
 *        }
 *    }
 * }</pre>
 *
 * @author Guang1234567
 * @date 2018/4/16 13:50
 */

public class Cleaner {

    private static final String TAG = "Cleaner";

    private final ReferenceQueue<Object> mDummyQueue;

    private final List<PhantomCleanable> mPhantomCleanableList;

    private Cleaner() {
        mDummyQueue = new ReferenceQueue();
        mPhantomCleanableList = Collections.synchronizedList(new LinkedList<>());
    }

    public static Cleaner create() {
        Cleaner cleaner = new Cleaner();
        cleaner.start();
        return cleaner;
    }

    private void start() {
        ThreadGroup tg = Thread.currentThread().getThreadGroup();
        for (ThreadGroup tgn = tg;
             tgn != null;
             tg = tgn, tgn = tg.getParent())
            ;
        Thread handler = new ReferenceHandler(tg, "a.b.c.Cleaner for Android");
        /* If there were a special system-only priority greater than
         * MAX_PRIORITY, it would be used here
         */
        handler.setPriority(Thread.MAX_PRIORITY);
        handler.setDaemon(true);
        handler.start();
    }

    private class ReferenceHandler extends Thread {

        public ReferenceHandler(ThreadGroup tg, String s) {
            super(tg, s);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Log.i(TAG, "Awaiting for GC");
                    Cleanable cleanable = (Cleanable) mDummyQueue.remove();
                    if (cleanable != null) {
                        cleanable.clean();
                    }
                    Log.i(TAG, "Referenced GC'd");
                } catch (Throwable ignore) {
                    ignore.printStackTrace();
                }
            }
        }
    }

    public Cleanable register(Object obj, Runnable action) {
        Objects.requireNonNull(obj, "obj");
        Objects.requireNonNull(action, "action");
        PhantomCleanable cleanable = new PhantomCleanable(obj, action);
        return cleanable;
    }

    public interface Cleanable {

        /**
         * Unregisters the cleanable and invokes the cleaning action.
         * The cleanable's cleaning action is invoked at most once
         * regardless of the number of calls to {@code clean}.
         */
        void clean();
    }

    private class PhantomCleanable<T> extends PhantomReference<T>
            implements Cleanable {

        private Runnable mAction;

        private PhantomCleanable(T referent, Runnable action) {
            super(referent, mDummyQueue);
            mAction = action;
            mPhantomCleanableList.add(this);
        }

        @Override
        public void clean() {
            if (mAction != null) {
                mAction.run();
                mAction = null;
            }
            mPhantomCleanableList.remove(this);
        }
    }
}