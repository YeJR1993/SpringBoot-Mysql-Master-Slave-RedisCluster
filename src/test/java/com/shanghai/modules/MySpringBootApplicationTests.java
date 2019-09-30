package com.shanghai.modules;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.shanghai.common.utils.YmlLoadUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MySpringBootApplicationTests {
	
	@Test
	public void test() {
		System.err.println(YmlLoadUtil.getInstance().getProperty("zooKeeper"));
	}
}
