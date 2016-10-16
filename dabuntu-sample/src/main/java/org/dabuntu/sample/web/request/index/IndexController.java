package org.dabuntu.sample.web.request.index;

import org.dabuntu.component.annotation.Inject;
import org.dabuntu.component.generator.aop.annotation.InOutLogging;
import org.dabuntu.sample.prop.RootProp;
import org.dabuntu.sample.web.repository.Products;
import org.dabuntu.sample.web.service.ProductService;
import org.dabuntu.web.annotation.Action;
import org.dabuntu.web.annotation.Controller;
import org.dabuntu.web.annotation.PathVariable;
import org.dabuntu.web.annotation.RequestBody;
import org.dabuntu.web.annotation.RequestCookie;
import org.dabuntu.web.core.response.HtmlResponse;
import org.dabuntu.web.def.HttpMethod;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ubuntu 2016/10/01
 */
@Controller
public class IndexController {

	// ===================================================================================
	//                                                                          @Inject
	//                                                                          ==========
	@Inject
	private RootProp prop;
	@Inject
	private ProductService service;

	// ===================================================================================
	//                                                                          Request
	//                                                                          ==========
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
	//                                               Path Variable
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
	//                                               RequestBody and Cookie binding
	//                                               -------
	@Action(url = "/request/test", method = HttpMethod.GET)
	public HtmlResponse requestTestGet(@RequestCookie IndexCookie cookie,
									   @RequestBody IndexForm form) {
		HtmlResponse response = new HtmlResponse("sample");
		TestResponseModel model = new TestResponseModel();

		// of course can't get body, It is testing for miss
		Optional<IndexForm> opForm = Optional.ofNullable(form);
		model.setForm1(opForm.map(f -> f.getData1()).orElse("null"));
		model.setForm2(opForm.map(f -> f.getData2()).orElse("null"));

		model.setCookie1(cookie.getKey1());
		model.setCookie2(cookie.getKey2());
		response.putData("model", model);
		return response;
	}
	@Action(url = "/request/test", method = HttpMethod.POST)
	public HtmlResponse authPost(// @Session SessionInfo userSession,
								 // @PathVariable("userId") String userId,
								 @RequestCookie IndexCookie cookie,
								 @RequestBody IndexForm form) {
		HtmlResponse response = new HtmlResponse("sample");
		TestResponseModel model = new TestResponseModel();
		model.setForm1(form.getData1());
		model.setForm2(form.getData2());
		model.setCookie1(cookie.getKey1());
		model.setCookie2(cookie.getKey2());
		response.putData("model", model);

		return response;
	}

	// ===================================================================================
	//                                                                          Private
	//                                                                          ==========
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
