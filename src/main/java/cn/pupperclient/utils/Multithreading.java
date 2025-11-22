package cn.pupperclient.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.minecraft.client.Minecraft;

import java.util.concurrent.*;

public class Multithreading {

	private static final ExecutorService executorService = Executors
			.newCachedThreadPool(new ThreadFactoryBuilder().setNameFormat("PupperClient-%d").build());
	private static final ScheduledExecutorService runnableExecutor = new ScheduledThreadPoolExecutor(
			Runtime.getRuntime().availableProcessors() + 1);

	public static void runAsync(Runnable runnable) {
		submit(runnable);
	}

    public static void runMainThread(Runnable runnable) {
        Minecraft.getInstance().execute(runnable);
    }

	public static void submit(Runnable runnable) {
        executorService.submit(runnable);
    }

	public static void schedule(Runnable runnable, long delay, TimeUnit timeUnit) {
		submitScheduled(runnable, delay, timeUnit);
	}

	public static void submitScheduled(Runnable runnable, long delay, TimeUnit timeUnit) {
        runnableExecutor.schedule(runnable, delay, timeUnit);
    }
}
