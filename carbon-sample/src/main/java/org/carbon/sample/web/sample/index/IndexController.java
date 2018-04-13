package org.carbon.sample.web.sample.index;

import java.util.Arrays;
import java.util.List;

import org.carbon.component.annotation.Assemble;
import org.carbon.sample.domain.service.ProductService;
import org.carbon.sample.domain.service.UserRoleService;
import org.carbon.sample.ext.jooq.tables.pojos.Product;
import org.carbon.sample.ext.jooq.tables.pojos.User;
import org.carbon.sample.prop.RootProp;
import org.carbon.web.annotation.Action;
import org.carbon.web.annotation.Controller;
import org.carbon.web.annotation.PathVariable;
import org.carbon.web.annotation.RequestBody;
import org.carbon.web.annotation.Cookie;
import org.carbon.web.translate.dto.Html;
import org.carbon.web.def.HttpMethod;
import org.carbon.web.def.Logo;

/**
 * @author Shota Oda 2016/10/01
 */
@Controller
public class IndexController {

    @Assemble
    private RootProp prop;
    @Assemble
    private ProductService productService;
    @Assemble
    private UserRoleService userRoleService;

    // ===================================================================================
    //                                                                          Request
    //                                                                          ==========
    // -----------------------------------------------------
    //                                               Basic
    //                                               -------
    @Action(path = "/about", method = HttpMethod.POST)
    public Html carbon() {
        String htmlString = new Logo().logo.replace("<", "・").replace(">", "・").replace("\n", "<br>").replaceAll("\\s", "&nbsp;");
        Html response = new Html("about");
        response.putData("model", htmlString);

        return response;
    }

    // -----------------------------------------------------
    //                                               DB integration
    //                                               -------
    @Action(path = "/users", method = HttpMethod.GET)
    public List<User> usersGet() {
        return userRoleService.findUsers();
    }

    @Action(path = "/products", method = HttpMethod.GET)
    public Html productsGet() {
        List<Product> products = productService.getProductsAll();

        Html response = new Html("/index/product_list");
        response.putData("models", products);
        return response;
    }

    @Action(path = "/products/{productId}", method = HttpMethod.GET)
    public Html productGet(@PathVariable("productId") String productId) {
        Long id = Long.parseLong(productId);
        Product product = productService.getProduct(id);

        Html response = new Html("/index/product_detail");
        response.putData("model", product);
        return response;
    }

    // -----------------------------------------------------
    //                                               Node Variable
    //                                               -------
    @Action(path = "/wiki/{target}/{target2}", method = HttpMethod.GET)
    public String wikiGet(@PathVariable("target") String target, @PathVariable("target2") String target2) {
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
    @Action(path = "/request/test", method = HttpMethod.GET)
    public Html requestTestGet(@Cookie IndexCookie cookie) {
        Html response = new Html("sample");
        TestResponseModel model = new TestResponseModel();

        // of course can't get body, It is testing for miss
        model.setCookie1(cookie.getKey1());
        model.setCookie2(cookie.getKey2());
        response.putData("model", model);
        UserInfoForm form = new UserInfoForm();
        form.setJobs(Arrays.asList(new JobForm(), new JobForm(), new JobForm()));
        response.putData("form", form);
        return response;
    }

    @Action(path = "/request/test", method = HttpMethod.POST)
    public Html requestTestPost(@Cookie IndexCookie cookie,
                                @RequestBody UserInfoForm form) {
        Html response = new Html("sample");
        TestResponseModel model = new TestResponseModel();
        model.setCookie1(cookie.getKey1());
        model.setCookie2(cookie.getKey2());

        response.putData("model", model);

        return response;
    }

    // ===================================================================================
    //                                                                          Private
    //                                                                          ==========
    private String aboutScala() {
        return "Scala（スカーラー([ˈskɑːlɑː] skah-lah[1])[2]）は\nオブジェクト指向言語と関数型言語の特徴を統合したマルチパラダイムのプログラミング言語である。\n名前の「Scala」は英語の「scalable language」に由来するものである。\n";
    }

    private String aboutJava() {
        return "Java（ジャバ）は、狭義ではオブジェクト指向プログラミング言語Javaであり、\n広義ではプログラミング言語Javaのプログラムの実行環境および開発環境をいう。\n本稿ではプログラミング言語としての Java、および関連する技術や設計思想、\nおよびJava言語の実行環境として見たJavaプラットフォームについて解説する。\nクラスライブラリなどを含めた、Javaバイトコードの実行環境と開発環境（広義のJava）については、\nJavaプラットフォームを参照。また、言語の文法に関してはJavaの文法を参照。";
    }
}
