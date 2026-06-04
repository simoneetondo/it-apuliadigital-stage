package it.exprivia.Scuola.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect // classe che contiene dei pezzi che vanno usati altrove
@Component // permette a spring di trovare la classe e gestirla come un bean
@Slf4j // best practice lombok per creare automaticamente l'ggetto log
public class LoggingAspect {


    // POINTCUT è la regola di SELEZIONE
    // definiamo un nome per un filtro, in questo caso controllerMethods
    // execution : qualsiasi tipo di ritorno, in quel pacchetto specifico, in qualsiasi classe
    @Pointcut("execution(* it.exprivia.Scuola.controller.*.*(..))")
    public void controllerMethods() { // metodo vuoto usato solo come etichetta
    }

    @Before("controllerMethods()") // è un ADVICE, dice a spring di eseguire questo PRIMA del metodo reale
    public void logBefore(JoinPoint joinPoint) {
        // JoinPoint è un oggetto della reflection che contiene i metadati del metodo chiamato
        log.info("Called controller method: {}", joinPoint.getSignature().getName());
        // JoinPoint.getArgs() recupera i valori che l'utente ha inviato al controller
        log.info("Arguments: {}", Arrays.toString(joinPoint.getArgs()));

    }

    // * @AfterReturning viene eseguito solo se il metodo termina CON SUCCESSO.
    // 'returning = "result"' dice a Spring di iniettare il valore di ritorno del controller
    // dentro il parametro 'Object result' di questo metodo.
    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("Controller method {} returned: {}", joinPoint.getSignature().getName(), result);
    }

    @AfterThrowing(pointcut = "controllerMethods()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        log.error("Il metodo {} ha lanciato un'eccezione: {}",
                joinPoint.getSignature().getName(),
                exception.getMessage());
    }

}
