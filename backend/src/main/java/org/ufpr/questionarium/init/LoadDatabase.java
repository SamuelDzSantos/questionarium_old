package org.ufpr.questionarium.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.stereotype.Component;
import org.ufpr.questionarium.services.UserService;

@Component
public class LoadDatabase implements CommandLineRunner {

    private final FilterChainProxy filterChainProxy;
    private final UserService userService;

    public LoadDatabase(FilterChainProxy filterChainProxy, UserService userService) {
        this.filterChainProxy = filterChainProxy;
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        filterChainProxy.getFilterChains()
                .stream()
                .forEach(filterChain -> {
                    System.out.println("Chain1");
                    filterChain.getFilters().stream().forEach(filter -> {
                        System.out.println(filter);
                    });
                });
        this.userService.register("user1", "myemail@gmail.com", "1234");
    };

}
