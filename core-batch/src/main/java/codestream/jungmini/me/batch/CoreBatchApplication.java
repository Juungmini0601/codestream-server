package codestream.jungmini.me.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "codestream.jungmini.me")
@SpringBootApplication
public class CoreBatchApplication {
    public static void main(String[] args) {
        // JobLauncherApplicationRunner가 Batch Job 수행
        // 배치 작업의 성공/실패 상태를 exit code로 외부 시스템에 전달 하기 위함.
        System.exit(SpringApplication.exit(SpringApplication.run(CoreBatchApplication.class, args)));
    }
}
