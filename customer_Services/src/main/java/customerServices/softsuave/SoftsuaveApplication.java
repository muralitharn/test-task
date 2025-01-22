package customerServices.softsuave;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SoftsuaveApplication {

	public static void main(String[] args) {
		SpringApplication.run(SoftsuaveApplication.class, args);
//		ConfigurableApplicationContext context = SpringApplication.run(SoftsuaveApplication.class, args);
//		ConfigurableListableBeanFactory ConfigurableListableBeanFactory = context.getBeanFactory();
//		String []arg=ConfigurableListableBeanFactory.getBeanDefinitionNames();
//
//		for (String local :arg){
//		System.out.println(arg);
//			//
//			}
		}

	}


