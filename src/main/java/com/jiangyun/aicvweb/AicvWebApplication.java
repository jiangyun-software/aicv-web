package com.jiangyun.aicvweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class AicvWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(AicvWebApplication.class, args);
		System.out.println("(\033[0;31m♥\033[0;32m◠‿◠)ﾉﾞ  匠韵智能网站启动成功   ლ(´ڡ`ლ)ﾞ  \n"
				+ "          _                                 __     \n"
				+ "  ____ _ (_)_____ _   __   _      __ ___   / /_    \n"
				+ " / __ `// // ___/| | / /  | | /| / // _ \\ / __ \\ \n"
				+ "/ /_/ // // /__  | |/ /   | |/ |/ //  __// /_/ /   \n"
				+ "\\__,_//_/ \\___/  |___/    |__/|__/ \\___//_.___/ \n"
				+ "                                              \033[0m");
	}

}
