package it.exprivia.Scuola.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ExecutionTrackerAspect {

    // AROUND metodo più completo, il pointcut cerca l'annotation personalizzata TrackExecution
    @Around("@annotation(it.exprivia.Scuola.annotation.TrackExecution)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        // prende il tempo iniziale
        long start = System.currentTimeMillis();

        // jointPoint.proceed è il comando che dice a spring:
        // esegui il metodo vero e proprio (il target)
        // senza questa riga, il metodo non verrebbe mai eseguito, il risultatio viene salvato in result
        Object result = joinPoint.proceed();

        // prende il tempo finale
        long end = System.currentTimeMillis();

        // ci calcoliamo il tempo
        final long time = end - start;
        final String methodName = joinPoint.getSignature().getName();
        final String className = joinPoint.getTarget().getClass().getName();
        // aggiungiamo il nome della classe qualora dovessimo avere due metodi uguali per due classi differenti

        log.info("Execution time for {}.{}: {} ms", className, methodName, time);
        // dobbiamo restituire il metodo originale, altrimenti chi lo chiama riceverà null
        return result;
    }

    // in LoggingAspect utilizzavamo il jointPoint, qui il ProceedJointPoint
    // 1. JointPoint ti permette di leggere i dati ( nome del metodo, argomenti)
    // 2. ProceedJointPoint invece è un'estensione che aggiunge il metodo proceed ed è l'unico che ti permette di
    // controllare il flusso temporale ( fermare il tempo prima e misurarlo dopo )

    // Il vantaggio delle annotation custom è che non logghiamo i tempi di tutti i metodi, ma soltanto
    // quelli che scegliamo noi, magari operazioni più dispensiose a livello di tempo, tipo una create, generazione pdf, calcoli etc.


}
