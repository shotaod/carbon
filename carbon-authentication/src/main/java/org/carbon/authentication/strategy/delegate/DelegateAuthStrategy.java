package org.carbon.authentication.strategy.delegate;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.authentication.AuthIdentity;
import org.carbon.authentication.strategy.AbstractSessionAuthStrategy;
import org.carbon.authentication.strategy.AuthStrategy;
import org.carbon.authentication.strategy.request.AuthRequest;
import org.carbon.authentication.translator.SignedTranslatable;
import org.carbon.web.context.session.SessionPool;

/**
 * @author Shota.Oda 2018/02/12.
 */
public class DelegateAuthStrategy<IDENTITY extends AuthIdentity> extends AbstractSessionAuthStrategy<IDENTITY> {
    private ShouldHandle shouldHandle;
    private ShouldPermitAnonymous shouldPermitAnonymous;
    private ShouldExpire shouldExpire;
    private ShouldTryAuth shouldTryAuth;
    private MapRequest mapRequest;
    private Find<? extends AuthIdentity> find;
    private ResponseAware onPermitNoAuth;
    private ResponseAware onExistSession;
    private ResponseAware onAuth;
    private SignedTranslator onExpire;
    private SignedTranslator onProhibitNoAuth;
    private SignedTranslator onIllegalAuthRequest;
    private SignedTranslator onNoFoundIdentity;
    private SignedTranslator onNoMatchSecret;
    private HttpServletRequest request;
    private HttpServletResponse response;

    public DelegateAuthStrategy(
            Class<IDENTITY> identityClass,
            SessionPool sessionContext) {
        super(identityClass, sessionContext);
    }

    /**
     * for Prototype
     */
    private DelegateAuthStrategy(
            DelegateAuthStrategy<IDENTITY> self,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        super(self.identityClass, self.sessionContext);
        this.shouldHandle = self.shouldHandle;
        this.shouldPermitAnonymous = self.shouldPermitAnonymous;
        this.shouldExpire = self.shouldExpire;
        this.shouldTryAuth = self.shouldTryAuth;
        this.mapRequest = self.mapRequest;
        this.find = self.find;
        this.onPermitNoAuth = self.onPermitNoAuth;
        this.onExpire = self.onExpire;
        this.onExistSession = self.onExistSession;
        this.onProhibitNoAuth = self.onProhibitNoAuth;
        this.onIllegalAuthRequest = self.onIllegalAuthRequest;
        this.onNoFoundIdentity = self.onNoFoundIdentity;
        this.onAuth = self.onAuth;
        this.onNoMatchSecret = self.onNoMatchSecret;
        this.request = request;
        this.response = response;
    }

    @Override
    public AuthStrategy prototype(HttpServletRequest request, HttpServletResponse response) {
        return new DelegateAuthStrategy<>(
                this,
                request,
                response);
    }

    // ===================================================================================
    //                                                                          delegate
    //                                                                          ==========
    public void delegateShouldHandle(ShouldHandle shouldHandle) {
        this.shouldHandle = shouldHandle;
    }

    public void delegateShouldPermitAnonymous(ShouldPermitAnonymous shouldPermitAnonymous) {
        this.shouldPermitAnonymous = shouldPermitAnonymous;
    }

    public void delegateShouldExpire(ShouldExpire shouldExpire) {
        this.shouldExpire = shouldExpire;
    }

    public void delegateShouldTryAuth(ShouldTryAuth shouldTryAuth) {
        this.shouldTryAuth = shouldTryAuth;
    }

    public void delegateMapRequest(MapRequest mapRequest) {
        this.mapRequest = mapRequest;
    }

    public void delegateFind(Find<IDENTITY> find) {
        this.find = find;
    }

    public void delegateOnPermitNoAuth(ResponseAware onPermitNoAuth) {
        this.onPermitNoAuth = onPermitNoAuth;
    }

    public void delegateOnExistSession(ResponseAware onExistSession) {
        this.onExistSession = onExistSession;
    }

    public void delegateOnAuth(ResponseAware onAuth) {
        this.onAuth = onAuth;
    }

    public void delegateOnExpire(SignedTranslator onExpire) {
        this.onExpire = onExpire;
    }

    public void delegateOnProhibitNoAuth(SignedTranslator onProhibitNoAuth) {
        this.onProhibitNoAuth = onProhibitNoAuth;
    }

    public void delegateOnIllegalAuthRequest(SignedTranslator onIllegalAuthRequest) {
        this.onIllegalAuthRequest = onIllegalAuthRequest;
    }

    public void delegateOnNoFoundIdentity(SignedTranslator onNoFoundIdentity) {
        this.onNoFoundIdentity = onNoFoundIdentity;
    }

    public void delegateOnNoMatchSecret(SignedTranslator onNoMatchSecret) {
        this.onNoMatchSecret = onNoMatchSecret;
    }

    // ===================================================================================
    //                                                                          Delegate
    //                                                                          ==========
    @Override
    public boolean shouldHandle(HttpServletRequest request) {
        return shouldHandle.apply(request);
    }

    @Override
    public boolean shouldPermitNoAuth() {
        return shouldPermitAnonymous.apply(request);
    }

    @Override
    public boolean shouldExpire() {
        return shouldExpire.apply(request);
    }

    @Override
    public boolean existSession() {
        return super.existSession();
    }


    @Override
    public boolean shouldTryAuth() {
        return shouldTryAuth.apply(request);
    }

    @Override
    public Optional<AuthRequest> mapRequest() {
        return mapRequest.apply(request);
    }

    @Override
    public Optional<? extends AuthIdentity> find(AuthRequest authRequest) {
        return find.apply(authRequest);
    }

    @Override
    public void onPermitNoAuth() {
        if (onPermitNoAuth == null) return;
        onPermitNoAuth.accept(response);
    }

    @Override
    public void onExistSession() {
        if (onExistSession == null) return;
        onExistSession.accept(response);
    }

    @Override
    protected void doOnAuth() {
        if (onAuth == null) return;
        onAuth.accept(response);
    }

    // ===================================================================================
    //                                                                          translation
    //                                                                          ==========
    @Override
    protected SignedTranslatable<?> doOnExpire() {
        return onExpire.get();
    }

    @Override
    public SignedTranslatable<?> translateProhibitNoAuth() {
        return onProhibitNoAuth.get();
    }

    @Override
    public SignedTranslatable<?> translateIllegalAuthRequest() {
        return onIllegalAuthRequest.get();
    }

    @Override
    public SignedTranslatable<?> translateNoFoundIdentity() {
        return onNoFoundIdentity.get();
    }

    @Override
    public SignedTranslatable<?> translateNoMatchSecret() {
        return onNoMatchSecret.get();
    }

    private /*static*/ interface Should<I> extends Function<I, Boolean> {
    }

    private /*static*/ interface OptionMapper<I, O> extends Function<I, Optional<O>> {
    }

    public /*static*/ interface ResponseAware extends Consumer<HttpServletResponse> {
    }

    public /*static*/ interface SignedTranslator extends Supplier<SignedTranslatable<?>> {
    }

    public /*static*/ interface ShouldHandle extends Should<HttpServletRequest> {
    }

    public /*static*/ interface ShouldPermitAnonymous extends Should<HttpServletRequest> {
    }

    public /*static*/ interface ShouldExpire extends Should<HttpServletRequest> {
    }

    public /*static*/ interface ShouldTryAuth extends Should<HttpServletRequest> {
    }

    public /*static*/ interface MapRequest extends OptionMapper<HttpServletRequest, AuthRequest> {
    }

    public /*static*/ interface Find<IDENTITY extends AuthIdentity> extends OptionMapper<AuthRequest, IDENTITY> {
    }
}
