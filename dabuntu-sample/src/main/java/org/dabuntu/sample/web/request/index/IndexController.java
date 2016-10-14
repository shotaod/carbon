package org.dabuntu.sample.web.request.index;

import org.dabuntu.component.classFactory.ClassFactory;
import org.dabuntu.component.classFactory.annotation.InOutLogging;
import org.dabuntu.component.classFactory.aop.interceptor.InOutLoggingInterceptor;
import org.dabuntu.component.instanceFactory.InstanceFactory;
import org.dabuntu.component.instanceFactory.annotation.Component;
import org.dabuntu.component.instanceFactory.annotation.Inject;
import org.dabuntu.component.scan.ClassBaseScanner;
import org.dabuntu.sample.ScanBase;
import org.dabuntu.sample.prop.RootProp;
import org.dabuntu.sample.web.repository.Products;
import org.dabuntu.sample.web.service.ProductService;
import org.dabuntu.sample.web.session.UserSession;
import org.dabuntu.util.format.StringLineBuilder;
import org.dabuntu.web.annotation.Action;
import org.dabuntu.web.annotation.Controller;
import org.dabuntu.web.annotation.PathVariable;
import org.dabuntu.web.annotation.RequestBody;
import org.dabuntu.web.annotation.RequestCookie;
import org.dabuntu.web.annotation.Session;
import org.dabuntu.web.def.HttpMethod;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ubuntu 2016/10/01
 */
@Component
@Controller
public class IndexController {

	// ===================================================================================
	//                                                                          Dep Inject
	//                                                                          ==========
	@Inject
	private RootProp prop;
	@Inject
	private ProductService service;

	// -----------------------------------------------------
	//                                               Basic
	//                                               -------
	@InOutLogging
	@Action(url = "/products", method = HttpMethod.GET)
	public String productsGet() {
		List<Products.Product> products = service.getProductsAll();
		return "call /products \n" + getProductInfo(products);
	}

	@InOutLogging
	@Action(url = "/products/{productId}", method = HttpMethod.GET)
	public String productGet(@PathVariable("productId") String productId) {
		Integer id = Integer.parseInt(productId);
		Products.Product product = service.getProduct(id);
		return "call /products/" + id + "\n" + getProductInfo(product);
	}

	// -----------------------------------------------------
	//                                               Wiki
	//                                               -------
	@InOutLogging
	@Action(url = "/wiki/{target}", method = HttpMethod.GET)
	public String wikiGet (@PathVariable("target") String target) {
		switch (target.toLowerCase()) {
			case "java":
				return aboutJava();
			case "scala":
				return aboutScala();
			default:
				return String.format("Sorry... wiki about '%s' is not ready m(_ _)m", target);
		}
	}

	// -----------------------------------------------------
	//                                               Info
	//                                               -------
	@Action(url = "/deps/result", method = HttpMethod.GET)
	public String dependenciesGet () throws IOException{
		// find classes
		ClassBaseScanner scanner = new ClassBaseScanner();
		List<Class> classes = scanner.run(ScanBase.class);

		// create Object
		ClassFactory classFactory = new ClassFactory();
		classFactory.setCallbacks(Collections.singletonList(new InOutLoggingInterceptor()));
		classFactory.setAcceptAnnotations(Arrays.asList(Component.class, Controller.class));
		Map<Class, Object> instanceMap = classFactory.initialize(classes);

		// inject Dependency
		InstanceFactory instanceFactory = new InstanceFactory(instanceMap);
		instanceFactory.run();
		return instanceFactory.getDependencyText();
	}

	// -----------------------------------------------------
	//                                               Session
	//                                               -------
	@Action(url = "/request/test", method = HttpMethod.GET)
	public String requestTestGet() {
		StringLineBuilder sb = new StringLineBuilder();
		sb.appendLine("<form action='/request/test/10' method='post'>");
		sb.appendLine("<input type='text' name='data1'>");
		sb.appendLine("<input type='text' name='data2'>");
		sb.appendLine("<input type='submit' value='そーしん'>");
		sb.appendLine("</form>");
		return sb.toString();
	}
	@Action(url = "/request/test/{userId}", method = HttpMethod.POST)
	public String authPost(// @Session UserSession userSession,
							@PathVariable("userId") String userId,
							@RequestCookie IndexCookie cookie,
							@RequestBody IndexForm form) {
		return String.format("here is auth<br><p>data1 is %s</p><br><p>data2 is %s</p><p>cookie('key1') is %s</p><br><p>cookie('key2') is %s</p>",form.getData1(), form.getData2(), cookie.getKey1(), cookie.getKey2());
	}

	private String getProductInfo(Products.Product product) {
		return String.format("プロダクト名称: %s, 価格: %d", product.getName(), product.getPrice());
	}

	private String getProductInfo(List<Products.Product> products) {
		return products.stream().map(this::getProductInfo).collect(Collectors.joining("\n"));
	}

	private String aboutScala() {
		return "Scala（スカーラー([ˈskɑːlɑː] skah-lah[1])[2]）は\nオブジェクト指向言語と関数型言語の特徴を統合したマルチパラダイムのプログラミング言語である。\n名前の「Scala」は英語の「scalable language」に由来するものである。\n";
	}

	private String aboutJava() {
		return "Java（ジャバ）は、狭義ではオブジェクト指向プログラミング言語Javaであり、\n広義ではプログラミング言語Javaのプログラムの実行環境および開発環境をいう。\n本稿ではプログラミング言語としての Java、および関連する技術や設計思想、\nおよびJava言語の実行環境として見たJavaプラットフォームについて解説する。\nクラスライブラリなどを含めた、Javaバイトコードの実行環境と開発環境（広義のJava）については、\nJavaプラットフォームを参照。また、言語の文法に関してはJavaの文法を参照。";
	}
}
