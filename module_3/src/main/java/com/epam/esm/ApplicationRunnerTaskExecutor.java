package com.epam.esm;

import com.epam.esm.util.generator.CommonGenerate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRunnerTaskExecutor implements ApplicationRunner {
    private CommonGenerate commonGenerate;

    @Autowired
    public ApplicationRunnerTaskExecutor(CommonGenerate commonGenerate) {
        this.commonGenerate = commonGenerate;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        commonGenerate.execute();
    }
}
