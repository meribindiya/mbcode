package com.mb.executor;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.mb.info.utilities.Utilities;

@Component
@Scope("prototype")
public class CustomExecutor implements Runnable {

	private Utilities utilities;

	private String url;

	public CustomExecutor(Object object, String url) {
		this.utilities = (Utilities) object;
		this.url = url;
	}

	public void run() {

		try {
			utilities.sendGet(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
