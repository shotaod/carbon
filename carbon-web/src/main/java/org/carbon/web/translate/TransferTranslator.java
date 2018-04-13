package org.carbon.web.translate;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.web.context.request.RequestPool;
import org.carbon.web.translate.dto.Forward;
import org.carbon.web.translate.dto.Redirect;
import org.carbon.web.translate.dto.Transfer;
import org.carbon.web.util.ResponseUtil;

/**
 * @author Shota Oda 2016/11/28.
 */
@Component
public class TransferTranslator implements HttpTranslator<Transfer> {

    @Assemble
    private RequestPool requestContext;

    @Override
    public Class<Transfer> targetType() {
        return Transfer.class;
    }

    @Override
    public void translate(HttpServletRequest request, HttpServletResponse response, Transfer transfer) throws Throwable {
        String transferTo = transfer.to();
        if (transfer instanceof Redirect) {
            ResponseUtil.redirect(response, transferTo);
            return;
        }
        if (transfer instanceof Forward) {
            RequestDispatcher dispatcher = request.getRequestDispatcher(transferTo);
            dispatcher.forward(request, response);
        }
    }
}
