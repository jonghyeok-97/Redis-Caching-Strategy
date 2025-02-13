package cache.strategy.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ExternalService {

    public void callExternal() {
        log.info("[외부 서비스 호출 시작]");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("[외부 서비스 호출 끝]");
    }
}
