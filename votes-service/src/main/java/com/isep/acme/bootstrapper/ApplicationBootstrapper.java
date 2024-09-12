package com.isep.acme.bootstrapper;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("bootstrap")
@RequiredArgsConstructor
public class ApplicationBootstrapper implements CommandLineRunner {

    private final UserBootstrapper userBootstrapper;

    private final ReviewBootstrapper reviewBootstrapper;

    private final VoteBootstrapper voteBootstrapper;

    @Override
    public void run(String... args) throws Exception {
        this.userBootstrapper.run();
        this.reviewBootstrapper.run();
        this.voteBootstrapper.run();
    }
}
