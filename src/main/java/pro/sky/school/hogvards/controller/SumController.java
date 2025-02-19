package pro.sky.school.hogvards.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Stream;

@RestController
public class SumController {

    @GetMapping("/calculateSum")
    public ResponseEntity<Long> calculateSum() {
        long startTime = System.currentTimeMillis();

        //double sum = Stream
          //      .iterate(1, a -> a +1)
            //    .limit(1_000_000)
              //  .reduce(0, (a, b) -> a + b );
        // 45 ms.
//variant 2
        //double n = 1_000_000;
        //double sum = n * (n + 1) / 2;
        //0 ms.


        double sum = 0;
        for (double i = 1; i <= 1_000_000; i++) {
            sum += i;
            //5 ms.
        }
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;


        System.out.println("Время выполнения: " + responseTime + " мс");

        return ResponseEntity.ok()
                .header("X-Response-Time", responseTime + " ms")
                .body((long) sum);
    }

}



