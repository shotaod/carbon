package org.carbon.sample.v2.app.api.rocketty.user;

import org.carbon.component.annotation.Assemble;
import org.carbon.sample.v2.app.api.rocketty.SuccessMessageDTO;
import org.carbon.sample.v2.conf.auth.api.RockettyClientIdentity;
import org.carbon.web.annotation.Action;
import org.carbon.web.annotation.Controller;
import org.carbon.web.annotation.RequestBody;
import org.carbon.web.annotation.Validate;
import org.carbon.web.annotation.scope.SessionScope;
import org.carbon.web.def.HttpMethod;
import org.carbon.web.translate.dto.Json;

/**
 * @author garden 2018/03/25.
 */
@Controller("/api/v1/rocketty/users")
public class RockettyUserController {

    @Assemble
    private RockettyUserAppService rockettyUserAppService;

    /**
     * @param clientIdentity from session
     */
    @Action(path = "me", method = HttpMethod.GET)
    public Json getUsersMe(
            @SessionScope RockettyClientIdentity clientIdentity
    ) {
        return rockettyUserAppService.fetchUserInfo(clientIdentity.getId());
    }

    /**
     * @param clientIdentity from session
     * @param postUserDTO    from request body
     * @throws UserDuplicateException when already registered
     */
    @Action(path = "", method = HttpMethod.PUT)
    public Json putUsers(
            @SessionScope RockettyClientIdentity clientIdentity,
            @Validate @RequestBody PostUserDTO postUserDTO
    ) throws UserDuplicateException {
        rockettyUserAppService.saveUser(clientIdentity.getId(), postUserDTO);
        return new SuccessMessageDTO("success to save user");
    }
}
