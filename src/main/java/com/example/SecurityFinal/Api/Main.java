package com.example.SecurityFinal.Api;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private static final int MAX_THREADS = 4;
    private static final long NUMBER_OF_OBJECTS = (long) Math.pow(10,7);
    private static AtomicInteger testCase = new AtomicInteger(0);
    public static void main(String [] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InterruptedException, ExecutionException {
      /*  Method method = Main.class.getMethod("singleThreadTest",long.class);
        Method method2 = Main.class.getMethod("multiThreadTest",long.class);

        long elapsedTime = test(method);
        long elapsedTime2 = test(method2);

        System.out.println("4 Threads is faster than a single thread : " + elapsedTime / elapsedTime2);*/

    }

    public static long test(Method method) throws InvocationTargetException, IllegalAccessException {
        System.out.println("TEST " + testCase.getAndIncrement());
        long startTime = System.currentTimeMillis();
        method.invoke(Main.class,NUMBER_OF_OBJECTS);
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println("Start Time : " + startTime);
        System.out.println("End Time : " + endTime);
        System.out.println("Elapsed Time : " + elapsedTime);
        System.out.println();
        return elapsedTime;
    }
    public static List<Object> singleThreadTest(long nObject){
        List<Object> list = new ArrayList<>();
        for(long i = 0 ; i < nObject ; i ++){
            list.add(new Object());
        }
        return list;
    }
    public static List<Object> multiThreadTest(long nObject) throws InterruptedException {
        List<Object> result = new ArrayList<>();

        ExecutorService service = Executors.newFixedThreadPool(MAX_THREADS);
        long batch = nObject / MAX_THREADS;
        for(int i = 0 ; i < MAX_THREADS; i ++){
            result.add(service.submit( () -> singleThreadTest(batch)));
        }

        return result;
    }





}
