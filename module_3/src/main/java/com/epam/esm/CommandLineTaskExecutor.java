package com.epam.esm;

import com.epam.esm.util.generator.CommonGenerate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLineTaskExecutor implements CommandLineRunner {
    private CommonGenerate commonGenerate;

    @Autowired
    public CommandLineTaskExecutor(CommonGenerate commonGenerate) {
        this.commonGenerate = commonGenerate;
    }

    @Override
    public void run(String... args) throws Exception {
//        commonGenerate.execute();
    }
}
