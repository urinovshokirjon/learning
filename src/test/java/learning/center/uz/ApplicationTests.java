package learning.center.uz;

import learning.center.uz.util.MD5Util;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {

	@Test
	void contextLoads() {
		System.out.println(MD5Util.encode("123123"));
	}

}
