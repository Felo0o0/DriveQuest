/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.drivequest.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;
import javax.swing.SwingUtilities;

/**
 * Clase de utilidad para el manejo de hilos.
 * <p>
 * Proporciona metodos para crear y gestionar hilos,
 * facilitando la implementacion de concurrencia.
 * </p>
 * 
 * @author DriveQuest Team
 * @version 1.0
 * @since 2023-07-15
 */
public class ThreadUtils {
    
    /** Tiempo maximo de espera para la terminacion de hilos en segundos */
    private static final int MAX_WAIT_TIME = 60;
    
    /** Número máximo de hilos permitidos en un pool */
    private static final int MAX_THREADS = 10;
    
    /** Tamaño de la cola de tareas por defecto */
    private static final int DEFAULT_QUEUE_SIZE = 100;
    
    /**
    * Constructor privado para evitar instanciación.
    */
    private ThreadUtils() {
    // Clase de utilidad, no debe instanciarse
    }
    
    /**
    * Ejecuta una tarea en el Event Dispatch Thread (EDT) de Swing.
    * Si ya está en el EDT, ejecuta la tarea inmediatamente.
    * 
    * @param runnable Tarea a ejecutar
    */
    public static void runOnEDT(Runnable runnable) {
        if (runnable == null) {
            return;
        }
        
        if (SwingUtilities.isEventDispatchThread()) {
            runnable.run();
        } else {
            SwingUtilities.invokeLater(runnable);
        }
    }
    
    /**
    * Crea un pool de hilos de tamaño fijo.
    * 
    * @param threads Numero de hilos en el pool
    * @return ExecutorService configurado con el numero de hilos especificado
    */
    public static ExecutorService createFixedThreadPool(int threads) {
    return Executors.newFixedThreadPool(validateThreadCount(threads));
    }
    
    /**
    * Crea un pool de hilos de tamaño fijo con un ThreadFactory personalizado.
    * 
    * @param threads Numero de hilos en el pool
    * @param threadFactory Factory para crear hilos
    * @return ExecutorService configurado
    */
    public static ExecutorService createFixedThreadPool(int threads, ThreadFactory threadFactory) {
    return Executors.newFixedThreadPool(validateThreadCount(threads), threadFactory);
    }
    
    /**
    * Crea un pool de hilos que crea nuevos hilos según sea necesario.
    * 
    * @return ExecutorService configurado
    */
    public static ExecutorService createCachedThreadPool() {
    return Executors.newCachedThreadPool();
    }
    
    /**
    * Crea un pool de hilos con un solo hilo.
    * 
    * @return ExecutorService configurado con un solo hilo
    */
    public static ExecutorService createSingleThreadExecutor() {
    return Executors.newSingleThreadExecutor();
    }
    
    /**
    * Crea un pool de hilos programable.
    * 
    * @param corePoolSize Número de hilos a mantener en el pool
    * @return ScheduledExecutorService configurado
    */
    public static ScheduledExecutorService createScheduledThreadPool(int corePoolSize) {
    return Executors.newScheduledThreadPool(validateThreadCount(corePoolSize));
    }
    
    /**
    * Crea un ThreadPoolExecutor con parámetros personalizados.
    * 
    * @param corePoolSize Número de hilos a mantener en el pool
    * @param maxPoolSize Número máximo de hilos permitidos
    * @param keepAliveTime Tiempo de vida de hilos inactivos
    * @param unit Unidad de tiempo para keepAliveTime
    * @return ThreadPoolExecutor configurado
    */
    public static ThreadPoolExecutor createCustomThreadPool(
    int corePoolSize, int maxPoolSize, long keepAliveTime, TimeUnit unit) {
    return new ThreadPoolExecutor(
    validateThreadCount(corePoolSize),
    validateThreadCount(maxPoolSize),
    keepAliveTime,
    unit,
    new LinkedBlockingQueue<>(DEFAULT_QUEUE_SIZE));
    }
    
    /**
    * Valida y ajusta el número de hilos.
    * 
    * @param threads Número de hilos solicitado
    * @return Número de hilos ajustado
    */
    private static int validateThreadCount(int threads) {
    return Math.max(1, Math.min(threads, MAX_THREADS));
    }
    
    /**
    * Espera a que los hilos en un ExecutorService terminen.
    * 
    * @param executor El ExecutorService a esperar
    * @return true si todos los hilos terminaron correctamente, false en caso contrario
    */
    public static boolean waitForCompletion(ExecutorService executor) {
    return waitForCompletion(executor, MAX_WAIT_TIME, TimeUnit.SECONDS);
    }
    
    /**
    * Espera a que los hilos en un ExecutorService terminen con un tiempo personalizado.
    * 
    * @param executor El ExecutorService a esperar
    * @param timeout Tiempo máximo de espera
    * @param unit Unidad de tiempo para timeout
    * @return true si todos los hilos terminaron correctamente, false en caso contrario
    */
    public static boolean waitForCompletion(ExecutorService executor, long timeout, TimeUnit unit) {
    if (executor == null) {
    return true;
    }
    
    executor.shutdown();
    try {
    return executor.awaitTermination(timeout, unit);
    } catch (InterruptedException e) {
    Thread.currentThread().interrupt();
    return false;
    }
    }
    
    /**
    * Crea un grupo de hilos y los ejecuta.
    * 
    * @param runnables Array de Runnable a ejecutar
    * @return true si todos los hilos terminaron correctamente, false en caso contrario
    */
    public static boolean runThreads(Runnable[] runnables) {
    if (runnables == null || runnables.length == 0) {
    return true;
    }
    
    ExecutorService executor = createFixedThreadPool(runnables.length);
    
    for (Runnable runnable : runnables) {
    if (runnable != null) {
    executor.execute(runnable);
    }
    }
    
    return waitForCompletion(executor);
    }
    
    /**
    * Ejecuta una lista de tareas y espera a que todas terminen.
    * 
    * @param <T> Tipo de resultado de las tareas
    * @param tasks Lista de tareas a ejecutar
    * @return Lista de resultados de las tareas
    * @throws InterruptedException Si el hilo es interrumpido mientras espera
    * @throws ExecutionException Si alguna tarea lanza una excepción
    */
    public static <T> List<T> executeAll(List<Callable<T>> tasks) 
    throws InterruptedException, ExecutionException {
    if (tasks == null || tasks.isEmpty()) {
    return new ArrayList<>();
    }
    
    ExecutorService executor = createFixedThreadPool(tasks.size());
    try {
    List<Future<T>> futures = executor.invokeAll(tasks);
    List<T> results = new ArrayList<>(futures.size());
    
    for (Future<T> future : futures) {
    results.add(future.get());
    }
    
    return results;
    } finally {
    forceShutdown(executor);
    }
    }
    
    /**
    * Ejecuta una tarea de forma asíncrona.
    * 
    * @param <T> Tipo de resultado de la tarea
    * @param task Tarea a ejecutar
    * @return Future que representa el resultado pendiente
    */
    public static <T> CompletableFuture<T> executeAsync(Supplier<T> task) {
    return CompletableFuture.supplyAsync(task);
    }
    
    /**
    * Fuerza la terminacion de un ExecutorService.
    * 
    * @param executor El ExecutorService a terminar
    */
    public static void forceShutdown(ExecutorService executor) {
    if (executor != null && !executor.isShutdown()) {
    executor.shutdownNow();
    }
    }
    
    /**
    * Duerme el hilo actual por un tiempo determinado.
    * 
    * @param milliseconds Tiempo en milisegundos
    * @return true si se durmio correctamente, false si fue interrumpido
    */
    public static boolean sleep(long milliseconds) {
    try {
    Thread.sleep(Math.max(0, milliseconds));
    return true;
    } catch (InterruptedException e) {
    Thread.currentThread().interrupt();
    return false;
    }
    }
    
    /**
    * Crea un ThreadFactory que establece nombres personalizados para los hilos.
    * 
    * @param namePrefix Prefijo para los nombres de los hilos
    * @param daemon Si los hilos deben ser daemons
    * @return ThreadFactory configurado
    */
    public static ThreadFactory createNamedThreadFactory(String namePrefix, boolean daemon) {
    return r -> {
    Thread thread = new Thread(r, namePrefix + "-" + System.currentTimeMillis());
    thread.setDaemon(daemon);
    return thread;
    };
    }
    
    /**
    * Crea un ThreadFactory que maneja excepciones no capturadas.
    * 
    * @param handler Manejador de excepciones
    * @return ThreadFactory configurado
    */
    public static ThreadFactory createExceptionHandlingThreadFactory(Thread.UncaughtExceptionHandler handler) {
    return r -> {
    Thread thread = new Thread(r);
    thread.setUncaughtExceptionHandler(handler);
    return thread;
    };
    }
    
    /**
    * Ejecuta una tarea con un timeout.
    * 
    * @param <T> Tipo de resultado de la tarea
    * @param task Tarea a ejecutar
    * @param timeout Tiempo máximo de espera
    * @param unit Unidad de tiempo para timeout
    * @return Resultado de la tarea o null si se excedió el timeout
    * @throws InterruptedException Si el hilo es interrumpido mientras espera
    * @throws ExecutionException Si la tarea lanza una excepción
    * @throws TimeoutException Si se excede el timeout
    */
    public static <T> T executeWithTimeout(Callable<T> task, long timeout, TimeUnit unit)
    throws InterruptedException, ExecutionException, TimeoutException {
    ExecutorService executor = createSingleThreadExecutor();
    try {
    Future<T> future = executor.submit(task);
    return future.get(timeout, unit);
    } finally {
    forceShutdown(executor);
    }
    }
    
    /**
    * Crea un lock estándar.
    * 
    * @return ReentrantLock configurado
    */
    public static ReentrantLock createLock() {
    return new ReentrantLock();
    }
    
    /**
    * Crea un lock justo (fair lock).
    * 
    * @return ReentrantLock configurado como justo
    */
    public static ReentrantLock createFairLock() {
    return new ReentrantLock(true);
    }
    
    /**
    * Crea un semáforo.
    * 
    * @param permits Número de permisos
    * @return Semaphore configurado
    */
    public static Semaphore createSemaphore(int permits) {
    return new Semaphore(permits);
    }
    
    /**
    * Crea un semáforo justo (fair semaphore).
    * 
    * @param permits Número de permisos
    * @return Semaphore configurado como justo
    */
    public static Semaphore createFairSemaphore(int permits) {
    return new Semaphore(permits, true);
    }
    
    /**
    * Crea una barrera cíclica.
    * 
    * @param parties Número de hilos que deben esperar en la barrera
    * @param barrierAction Acción a ejecutar cuando todos los hilos llegan a la barrera
    * @return CyclicBarrier configurado
    */
    public static CyclicBarrier createBarrier(int parties, Runnable barrierAction) {
    return new CyclicBarrier(parties, barrierAction);
    }
    
}