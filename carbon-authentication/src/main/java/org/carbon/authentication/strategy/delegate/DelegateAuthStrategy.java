package org.carbon.authentication.strategy.delegate;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.authentication.AuthIdentity;
import org.carbon.authentication.strategy.AbstractSessionAuthStrategy;
import org.carbon.authentication.strategy.AuthStrategy;
import org.carbon.authentication.strategy.request.AuthRequest;
import org.carbon.web.context.session.SessionContext;

/**
 * @author garden 2018/02/12.
 */
public class DelegateAuthStrategy<IDENTITY extends AuthIdentity> extends AbstractSessionAuthStrategy<IDENTITY> {
    private /*static*/ interface Should<I> extends Function<I, Boolean> {
    }

    private /*static*/ interface OptionMapper<I, O> extends Function<I, Optional<O>> {
    }

    private /*static*/ interface ResponseWriter extends Consumer<HttpServletResponse> {
    }

    public /*static*/ interface ShouldHandle extends Should<HttpServletRequest> {
    }

    public /*static*/ interface ShouldPermitNoAuth extends Should<HttpServletRequest> {
    }

    public /*static*/ interface ShouldExpire extends Should<HttpServletRequest> {
    }

    public /*static*/ interface ShouldTryAuth extends Should<HttpServletRequest> {
    }

    public /*static*/ interface MapRequest extends OptionMapper<HttpServletRequest, AuthRequest> {
    }

    public /*static*/ interface Find<IDENTITY extends AuthIdentity> extends OptionMapper<AuthRequest, IDENTITY> {
    }

    public /*static*/ interface Confirm extends Should<AuthIdentity> {
    }

    public /*static*/ interface OnPermitNoAuth extends ResponseWriter {
    }

    public /*static*/ interface OnExpire extends ResponseWriter {
    }

    public /*static*/ interface OnExistSession extends ResponseWriter {
    }

    public /*static*/ interface OnProhibitNoAuth extends ResponseWriter {
    }

    public /*static*/ interface OnIllegalAuthRequest extends ResponseWriter {
    }

    public /*static*/ interface OnNoFoundIdentity extends ResponseWriter {
    }

    public /*static*/ interface OnAuth extends ResponseWriter {
    }

    public /*static*/ interface OnNoMatchSecret extends ResponseWriter {
    }

    private ShouldHandle shouldHandle;
    private ShouldPermitNoAuth shouldPermitNoAuth;
    private ShouldExpire shouldExpire;
    private ShouldTryAuth shouldTryAuth;
    private MapRequest mapRequest;
    private Find<AuthIdentity> find;
    private Confirm confirm;

    private OnPermitNoAuth onPermitNoAuth;
    private OnExpire onExpire;
    private OnExistSession onExistSession;
    private OnProhibitNoAuth onProhibitNoAuth;
    private OnIllegalAuthRequest onIllegalAuthRequest;
    private OnNoFoundIdentity onNoFoundIdentity;
    private OnAuth onAuth;
    private OnNoMatchSecret onNoMatchSecret;

    private HttpServletRequest request;
    private HttpServletResponse response;

    public DelegateAuthStrategy(
            Class<IDENTITY> identityClass,
            SessionContext sessionContext) {
        super(identityClass, sessionContext);
    }

    /**
     * for Prototype
     */
    private DelegateAuthStrategy(
            Class<IDENTITY> identityClass,
            SessionContext sessionContext,
            HttpServletRequest request,
            HttpServletResponse response) {
        super(identityClass, sessionContext);
        this.request = request;
        this.response = response;
    }

    @Override
    public AuthStrategy prototype(HttpServletRequest request, HttpServletResponse response) {
        return new DelegateAuthStrategy<>(
                identityClass,
                sessionContext,
                request,
                response);
    }

    // ===================================================================================
    //                                                                          delegate
    //                                                                          ==========
    public void delegateShouldHandle(ShouldHandle shouldHandle) {
        this.shouldHandle = shouldHandle;
    }

    public void delegateShouldPermitNoAuth(ShouldPermitNoAuth shouldPermitNoAuth) {
        this.shouldPermitNoAuth = shouldPermitNoAuth;
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

    public void delegateFind(Find<AuthIdentity> find) {
        this.find = find;
    }

    public void delegateConfirm(Confirm confirm) {
        this.confirm = confirm;
    }

    public void delegateOnPermitNoAuth(OnPermitNoAuth onPermitNoAuth) {
        this.onPermitNoAuth = onPermitNoAuth;
    }

    public void delegateOnExpire(OnExpire onExpire) {
        this.onExpire = onExpire;
    }

    public void delegateOnExistSession(OnExistSession onExistSession) {
        this.onExistSession = onExistSession;
    }

    public void delegateOnProhibitNoAuth(OnProhibitNoAuth onProhibitNoAuth) {
        this.onProhibitNoAuth = onProhibitNoAuth;
    }

    public void delegateOnIllegalAuthRequest(OnIllegalAuthRequest onIllegalAuthRequest) {
        this.onIllegalAuthRequest = onIllegalAuthRequest;
    }

    public void delegateOnNoFoundIdentity(OnNoFoundIdentity onNoFoundIdentity) {
        this.onNoFoundIdentity = onNoFoundIdentity;
    }

    public void delegateOnAuth(OnAuth onAuth) {
        this.onAuth = onAuth;
    }

    public void delegateOnNoMatchSecret(OnNoMatchSecret onNoMatchSecret) {
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
        return shouldPermitNoAuth.apply(request);
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
    public Optional<AuthIdentity> find(AuthRequest authRequest) {
        return find.apply(authRequest);
    }

    @Override
    public boolean confirm(AuthIdentity authIdentity) {
        return confirm.apply(authIdentity);
    }

    @Override
    public void onPermitNoAuth() {
        onPermitNoAuth.accept(response);

    }

    @Override
    public void onExpire() {
        super.onExpire();
        onExpire.accept(response);
    }

    @Override
    public void onExistSession() {
        onExistSession.accept(response);
    }

    @Override
    public void onProhibitNoAuth() {
        onProhibitNoAuth.accept(response);
    }

    @Override
    public void onIllegalAuthRequest() {
        onIllegalAuthRequest.accept(response);
    }

    @Override
    public void onNoFoundIdentity() {
        onNoFoundIdentity.accept(response);
    }

    @Override
    public void onAuth(AuthIdentity identity) {
        super.onAuth(identity);
        onAuth.accept(response);
    }

    @Override
    public void onNoMatchSecret() {
        onNoMatchSecret.accept(response);
    }
}
