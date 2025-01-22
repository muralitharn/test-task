package softsuave.placeOrder;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Iterator;
import java.util.Map;

@SpringBootApplication
public class PlaceOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlaceOrderApplication.class, args);
		ConfigurableApplicationContext ApplicationContext = SpringApplication.run(PlaceOrderApplication.class, args);
		ConfigurableListableBeanFactory ConfigurableListableBeanFactory = ApplicationContext.getBeanFactory();
		Iterator<String> iterator = ConfigurableListableBeanFactory.getBeanNamesIterator();

		while (iterator.hasNext()) {
			String sample = iterator.next();
			System.out.println(sample);
		}
		Map.Entry<String, String> entry;
	}
}